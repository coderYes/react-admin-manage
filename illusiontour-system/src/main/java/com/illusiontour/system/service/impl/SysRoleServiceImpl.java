package com.illusiontour.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.illusiontour.common.core.domain.entity.SysRole;
import com.illusiontour.common.core.domain.entity.SysRoleMenu;
import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.common.exception.TourException;
import com.illusiontour.common.result.HttpStatus;
import com.illusiontour.common.utils.SecurityUtils;
import com.illusiontour.common.utils.StringUtils;
import com.illusiontour.common.utils.spring.SpringUtils;
import com.illusiontour.system.mapper.SysRoleMapper;
import com.illusiontour.system.mapper.SysRoleMenuMapper;
import com.illusiontour.system.mapper.SysUserRoleMapper;
import com.illusiontour.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author zgw
 * @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
 * @createDate 2025-02-28 17:37:21
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 根据角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        return sysRoleMapper.selectRoleById(roleId);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRoleKeysByUserId(Long userId) {
        List<SysRole> roles = sysRoleMapper.selectRoleKeysByUserId(userId);
        Set<String> rolesSet = new HashSet<>();
        for (SysRole role : roles) {
            rolesSet.addAll(Arrays.asList(role.getRoleKey().trim().split(",")));
        }
        return rolesSet;
    }

    /**
     * 校验角色是否有数据权限
     *
     * @param roleIds 角色id
     */
    @Override
    public void checkRoleDataScope(Long... roleIds) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            for (Long roleId : roleIds) {
                SysRole role = SpringUtils.getAopProxy(this).selectRoleById(roleId);
                if (StringUtils.isNull(role)) {
                    throw new TourException("没有权限访问角色数据！", HttpStatus.ERROR);
                }
            }
        }
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param sysRole 角色信息
     */
    @Override
    public boolean checkRoleNameUnique(SysRole sysRole) {
        Long roleId = StringUtils.isNull(sysRole.getId()) ? -1L : sysRole.getId();
        SysRole info = sysRoleMapper.checkRoleNameUnique(sysRole.getRoleName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != roleId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param sysRole 角色信息
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole sysRole) {
        Long roleId = StringUtils.isNull(sysRole.getId()) ? -1L : sysRole.getId();
        SysRole info = sysRoleMapper.checkRoleKeyUnique(sysRole.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != roleId.longValue()) {
            return false;
        }
        return true;
    }


    /**
     * 根据条件分页查询用户列表
     *
     * @param sysRole 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public IPage<SysRole> selectRoleList(IPage<SysRole> page, SysRole sysRole) {
        return sysRoleMapper.selectRoleList(page, sysRole);
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int insertRole(SysRole role) {
        sysRoleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getId()) && role.isAdmin()) {
            throw new TourException("不允许操作超级管理员角色", HttpStatus.ERROR);
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(Long roleId) {
        return sysUserRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRole(SysRole role) {
        // 修改角色信息
        sysRoleMapper.updateRole(role);
        // 删除角色与菜单关联
        sysRoleMenuMapper.deleteRoleMenuByRoleId(role.getId());
        return insertRoleMenu(role);
    }


    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new TourException(String.format("%1$s已分配,不能删除", role.getRoleName()), HttpStatus.ERROR);
            }
        }
        // 删除角色与菜单关联
        sysRoleMenuMapper.deleteRoleMenu(roleIds);
        return sysRoleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        int rows = 1;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = sysRoleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }
}




