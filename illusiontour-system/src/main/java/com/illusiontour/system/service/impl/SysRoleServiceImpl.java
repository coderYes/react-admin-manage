package com.illusiontour.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.illusiontour.common.core.domain.entity.SysRole;
import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.common.exception.TourException;
import com.illusiontour.common.result.HttpStatus;
import com.illusiontour.common.utils.SecurityUtils;
import com.illusiontour.common.utils.StringUtils;
import com.illusiontour.common.utils.spring.SpringUtils;
import com.illusiontour.system.mapper.SysRoleMapper;
import com.illusiontour.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public void checkRoleDataScope(Long... roleIds) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            for (Long roleId : roleIds) {
                SysRole role = new SysRole();
                role.setId(roleId);
                List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(role);
                if (StringUtils.isEmpty(roles)) {
                    throw new TourException("没有权限访问角色数据！", HttpStatus.ERROR);
                }
            }
        }
    }

    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        return sysRoleMapper.selectRoleList(role);
    }
}




