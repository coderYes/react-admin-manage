package com.illusiontour.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.illusiontour.common.core.domain.entity.SysMenu;
import com.illusiontour.common.core.domain.entity.SysRole;

import java.util.List;

/**
* @author zgw
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2025-03-07 11:12:20
* @Entity com.illusiontour.common.core/domain.SysMenu
*/
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> selectRolePermissionByUserId(Long userId);
}




