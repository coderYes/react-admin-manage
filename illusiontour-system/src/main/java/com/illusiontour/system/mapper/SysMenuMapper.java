package com.illusiontour.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.illusiontour.common.core.domain.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zgw
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
 * @createDate 2025-03-07 11:12:20
 * @Entity com.illusiontour.common.core/domain.SysMenu
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> selectRolePermissionByUserId(Long userId);

    IPage<SysMenu> selectMenuList(IPage<SysMenu> page, SysMenu sysMenu);

    IPage<SysMenu> selectMenuListByUserId(IPage<SysMenu> page, SysMenu sysMenu);

    SysMenu selectMenuById(Long menuId);

    List<Long> selectMenuListByRoleId(Long roleId);

    SysMenu checkMenuNameUnique(String menuName, Long parentId);

    int insertMenu(SysMenu menu);

    int updateMenu(SysMenu menu);

    int hasChildByMenuId(Long menuId);

    int deleteMenuById(Long menuId);
}




