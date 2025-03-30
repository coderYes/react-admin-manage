package com.illusiontour.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.illusiontour.common.core.controller.BaseController;
import com.illusiontour.common.core.domain.entity.SysMenu;
import com.illusiontour.common.result.Result;
import com.illusiontour.common.utils.StringUtils;
import com.illusiontour.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "全景后台管理系统菜单管理")
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {
    @Autowired
    private SysMenuService sysMenuService;

    @Operation(summary = "获取菜单列表")
    @GetMapping("/list")
    public Result<IPage<SysMenu>> list(@RequestParam long current, @RequestParam long size, SysMenu sysMenu) {
        IPage<SysMenu> page = new Page<>(current, size);
        IPage<SysMenu> sysMenuIPage = sysMenuService.selectMenuList(page, sysMenu, getUserId());
        return Result.success(sysMenuIPage);
    }

    @Operation(summary = "根据菜单编号获取菜单")
    @GetMapping("/{menuId}")
    public Result<SysMenu> getInfo(@PathVariable Long menuId) {
        return Result.success(sysMenuService.selectMenuById(menuId));
    }

    @Operation(summary = "获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public Result<List<SysMenu>> treeselect(SysMenu sysMenu) {
        IPage<SysMenu> page = new Page<>();
        IPage<SysMenu> sysMenuIPage = sysMenuService.selectMenuList(page, sysMenu, getUserId());
        List<SysMenu> menus = sysMenuIPage.getRecords();
        return Result.success(sysMenuService.buildMenuTree(menus));
    }

    @Operation(summary = "加载对应角色菜单列表树")
    @GetMapping("/roleMenuTreeselect/{roleId}")
    public Result<Map<String, Object>> roleMenuTreeselect(@PathVariable Long roleId) {
        Map<String, Object> menusMap = new HashMap<>();
        List<SysMenu> menus = sysMenuService.selectMenuList(getUserId());
        menusMap.put("menus", menus);
        menusMap.put("checkedKeys", sysMenuService.selectMenuListByRoleId(roleId));
        return Result.success(menusMap);
    }

    @Operation(summary = "新增菜单")
    @PostMapping("/add")
    public Result add(@RequestBody SysMenu sysMenu) {
        if (!sysMenuService.checkMenuNameUnique(sysMenu)) {
            return Result.fail("新增菜单'" + sysMenu.getMenuName() + "'失败，菜单名称已存在");
        } else if (sysMenu.getIsFrame() == 0 && !StringUtils.ishttp(sysMenu.getPath())) {
            return Result.fail("新增菜单'" + sysMenu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        sysMenu.setCreateBy(getUsername());
        return Result.success(sysMenuService.insertMenu(sysMenu));
    }

    @Operation(summary = "修改菜单")
    @PutMapping("/edit")
    public Result<Integer> edit(@RequestBody SysMenu sysMenu) {
        if (!sysMenuService.checkMenuNameUnique(sysMenu)) {
            return Result.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，菜单名称已存在");
        } else if (sysMenu.getIsFrame() == 0 && !StringUtils.ishttp(sysMenu.getPath())) {
            return Result.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (sysMenu.getId().equals(sysMenu.getParentId())) {
            return Result.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        sysMenu.setUpdateBy(getUsername());
        return Result.success(sysMenuService.updateMenu(sysMenu));
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/delete/{menuId}")
    public Result<Integer> remove(@PathVariable Long menuId) {
        if (sysMenuService.hasChildByMenuId(menuId)) {
            return Result.fail("存在子菜单,不允许删除");
        }
        if (sysMenuService.checkMenuExistRole(menuId)) {
            return Result.fail("菜单已分配,不允许删除");
        }
        return Result.success(sysMenuService.deleteMenuById(menuId));
    }

}
