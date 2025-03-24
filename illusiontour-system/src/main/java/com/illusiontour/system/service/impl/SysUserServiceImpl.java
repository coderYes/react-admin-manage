package com.illusiontour.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.common.core.domain.entity.SysUserRole;
import com.illusiontour.common.exception.TourException;
import com.illusiontour.common.result.HttpStatus;
import com.illusiontour.common.utils.SecurityUtils;
import com.illusiontour.common.utils.StringUtils;
import com.illusiontour.common.utils.spring.SpringUtils;
import com.illusiontour.system.mapper.SysRoleMapper;
import com.illusiontour.system.mapper.SysUserMapper;
import com.illusiontour.system.mapper.SysUserRoleMapper;
import com.illusiontour.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zgw
 * @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
 * @createDate 2025-01-24 10:35:23
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    @Autowired
    private SysUserMapper systemUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    /**
     * 根据id查询用户
     *
     * @param id 用户id
     * @return 用户信息
     */
    public SysUser getUserById(Long id) {
        return systemUserMapper.selectById(id);
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public IPage<SysUser> selectUserList(IPage<SysUser> page, SysUser sysUser) {
        return systemUserMapper.selectUserList(page, sysUser);
    }

    /**
     * 根据用户ID查询用户对象
     *
     * @param userId    用户ID
     * @return 用户对象
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return systemUserMapper.selectUserById(userId);
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param sysUser 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUser sysUser) {
        Long userId = StringUtils.isNull(sysUser.getId()) ? -1L : sysUser.getId();
        SysUser info = systemUserMapper.checkUserNameUnique(sysUser.getUserName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param sysUser 用户信息
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUser sysUser) {
        Long userId = StringUtils.isNull(sysUser.getId()) ? -1L : sysUser.getId();
        SysUser info = systemUserMapper.checkPhoneUnique(sysUser.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 校验email是否唯一
     *
     * @param sysUser 用户信息
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser sysUser) {
        Long userId = StringUtils.isNull(sysUser.getId()) ? -1L : sysUser.getId();
        SysUser info = systemUserMapper.checkEmailUnique(sysUser.getEmail());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != userId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StringUtils.isNotNull(user.getId()) && user.isAdmin()) {
            throw new TourException("不允许操作超级管理员用户", HttpStatus.ERROR);
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysUser user = SpringUtils.getAopProxy(this).selectUserById(userId);
            if (StringUtils.isNull(user)) {
                throw new TourException("没有权限访问用户数据！", HttpStatus.ERROR);
            }
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        int rows = systemUserMapper.insertUser(user);
        insertUserRole(user);
        return rows;
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            SysUser user = new SysUser();
            user.setId(userId);
            checkUserAllowed(user);
            checkUserDataScope(userId);
        }
        sysUserRoleMapper.deleteUserRole(userIds);
        return systemUserMapper.deleteUserByIds(userIds);
    }

    @Override
    public int updateUser(SysUser user) {
        Long userId = user.getId();
        // 删除用户与角色关联
        sysUserRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        return systemUserMapper.updateUser(user);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        this.insertUserRole(user.getId(), user.getRoleIds());
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>(roleIds.length);
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            sysRoleMapper.batchUserRole(list);
        }
    }
}




