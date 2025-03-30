package com.illusiontour.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.illusiontour.common.core.domain.entity.SysMenu;

import java.util.List;
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

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuList(Long userId);

    /**
     * 分页查询菜单列表
     *
     * @param page    分页对象
     * @param sysMenu 菜单查询条件对象
     * @param userId  用户ID
     * @return 包含菜单列表的分页对象
     */
    IPage<SysMenu> selectMenuList(IPage<SysMenu> page, SysMenu sysMenu, Long userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<Long> selectMenuListByRoleId(Long roleId);

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    SysMenu selectMenuById(Long menuId);

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    boolean checkMenuNameUnique(SysMenu menu);

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    int insertMenu(SysMenu menu);

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    int updateMenu(SysMenu menu);


    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean hasChildByMenuId(Long menuId);

    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkMenuExistRole(Long menuId);

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int deleteMenuById(Long menuId);

}
