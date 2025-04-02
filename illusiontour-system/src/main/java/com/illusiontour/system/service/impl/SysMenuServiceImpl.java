package com.illusiontour.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.illusiontour.common.constant.UserConstants;
import com.illusiontour.common.core.domain.entity.SysMenu;
import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.common.utils.SecurityUtils;
import com.illusiontour.common.utils.StringUtils;
import com.illusiontour.system.domain.vo.MetaVo;
import com.illusiontour.system.domain.vo.RouterVo;
import com.illusiontour.system.mapper.SysRoleMenuMapper;
import com.illusiontour.system.service.SysMenuService;
import com.illusiontour.system.mapper.SysMenuMapper;
import com.illusiontour.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zgw
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 * @createDate 2025-03-07 11:12:20
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysMenu> perms = sysMenuMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysMenu perm : perms) {
            permsSet.addAll(Arrays.asList(perm.getPerms().trim()));
        }
        return permsSet;
    }

    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuList(Long userId) {
        IPage<SysMenu> page = new Page<>();
        IPage<SysMenu> sysMenuIPage = selectMenuList(page, new SysMenu(), userId);
        List<SysMenu> menus = sysMenuIPage.getRecords();
        return menus;
    }

    /**
     * 分页查询菜单列表
     *
     * @param page    分页对象
     * @param sysMenu 菜单查询条件对象
     * @param userId  用户ID
     * @return 包含菜单列表的分页对象
     */
    @Override
    public IPage<SysMenu> selectMenuList(IPage<SysMenu> page, SysMenu sysMenu, Long userId) {
        IPage<SysMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId)) {
            menuList = sysMenuMapper.selectMenuList(page, sysMenu);
        } else {
            sysMenu.getParams().put("userId", userId);
            menuList = sysMenuMapper.selectMenuListByUserId(page, sysMenu);
        }
        return menuList;
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return sysMenuMapper.selectMenuListByRoleId(roleId);
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId)) {
            menus = sysMenuMapper.selectMenuTreeAll();
        } else {
            menus = sysMenuMapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(Long menuId) {
        return sysMenuMapper.selectMenuById(menuId);
    }

    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenu menu) {
        return sysMenuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return sysMenuMapper.updateMenu(menu);
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int result = sysMenuMapper.hasChildByMenuId(menuId);
        return result > 0;
    }

    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int result = sysRoleMenuMapper.checkMenuExistRole(menuId);
        return result > 0;
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId) {
        return sysMenuMapper.deleteMenuById(menuId);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<>();
        List<Long> tempList = menus.stream().map(SysMenu::getId).toList();
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = (SysMenu) iterator.next();
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(StringUtils.convertToCamelCase(menu.getPath()));
            router.setPath("/" + menu.getPath());
            router.setComponent(menu.getComponent());
            router.setQuery(menu.getQuery());
            router.setMenuType(menu.getMenuType());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getIsCache() == 1, menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (StringUtils.isNotEmpty(cMenus) && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setChildren(buildMenus(cMenus));
            }
            routers.add(router);

        }
        return routers;
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean checkMenuNameUnique(SysMenu menu) {
        Long menuId = StringUtils.isNull(menu.getId()) ? -1L : menu.getId();
        SysMenu info = sysMenuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != menuId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }
}




