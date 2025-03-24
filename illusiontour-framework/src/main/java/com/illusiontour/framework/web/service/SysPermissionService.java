package com.illusiontour.framework.web.service;


import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SysPermissionService {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 获取角色数据权限
     *
     * @param sysUser 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser sysUser) {
        Set<String> roles = new HashSet<>();
        if (sysUser.isAdmin()) {
            roles.add("admin");
        } else {
            roles.addAll(sysRoleService.selectRoleKeysByUserId(sysUser.getId()));
        }
        return roles;
    }
}
