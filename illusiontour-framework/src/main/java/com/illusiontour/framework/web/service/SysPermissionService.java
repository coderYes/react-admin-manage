package com.illusiontour.framework.web.service;


import com.illusiontour.common.core.domain.entity.SysRole;
import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.system.service.SysMenuService;
import com.illusiontour.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SysPermissionService {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

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

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            perms.addAll(sysMenuService.selectRolePermissionByUserId(user.getId()));
        }
        return perms;
    }


}
