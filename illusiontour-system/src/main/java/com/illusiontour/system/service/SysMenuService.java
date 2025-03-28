package com.illusiontour.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.illusiontour.common.core.domain.entity.SysMenu;

import java.util.Set;

/**
 * @author zgw
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
 * @createDate 2025-03-07 11:12:20
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectRolePermissionByUserId(Long userId);

}
