package com.illusiontour.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.illusiontour.common.core.controller.BaseController;
import com.illusiontour.common.core.domain.entity.SysRole;
import com.illusiontour.common.result.Result;
import com.illusiontour.framework.web.service.SysPermissionService;
import com.illusiontour.system.service.SysRoleService;
import com.illusiontour.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "全景后台管理系统角色管理")
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Operation(summary = "获取角色列表")
    @GetMapping("/list")
    public Result<IPage<SysRole>> list(@RequestParam long current, @RequestParam long size, SysRole sysRole) {
        IPage<SysRole> page = new Page<>(current, size);
        IPage<SysRole> sysUserIPage = sysRoleService.selectRoleList(page, sysRole);
        return Result.success(sysUserIPage);
    }

    @Operation(summary = "根据角色ID获取详细信息")
    @GetMapping("/info/{roleId}")
    public Result<SysRole> info(@PathVariable Long roleId) {
        sysRoleService.checkRoleDataScope(roleId);
        return Result.success(sysRoleService.selectRoleById(roleId));
    }

    @Operation(summary = "角色创建")
    @PostMapping("/add")
    public Result<Integer> info(@RequestBody SysRole sysRole) {
        if (!sysRoleService.checkRoleNameUnique(sysRole)) {
            return Result.fail("新增角色'" + sysRole.getRoleName() + "'失败，角色名称已存在");
        } else if (!sysRoleService.checkRoleKeyUnique(sysRole)) {
            return Result.fail("新增角色'" + sysRole.getRoleName() + "'失败，角色权限已存在");
        }
        sysRole.setCreateBy(getUsername());
        return Result.success(sysRoleService.insertRole(sysRole));
    }

    @Operation(summary = "角色修改")
    @PutMapping("/edit")
    public Result<Integer> edit(@RequestBody SysRole sysRole) {
        sysRoleService.checkRoleAllowed(sysRole);
        sysRoleService.checkRoleDataScope(sysRole.getId());
        if (!sysRoleService.checkRoleNameUnique(sysRole)) {
            return Result.fail("修改角色'" + sysRole.getRoleName() + "'失败，角色名称已存在");
        } else if (!sysRoleService.checkRoleKeyUnique(sysRole)) {
            return Result.fail("修改角色'" + sysRole.getRoleName() + "'失败，角色权限已存在");
        }
        sysRole.setUpdateBy(getUsername());
        return Result.success(sysRoleService.updateRole(sysRole));
    }

    @Operation(summary = "角色删除")
    @DeleteMapping("/delete/{roleIds}")
    public Result<Integer> delete(@PathVariable Long[] roleIds) {
        return Result.success(sysRoleService.deleteRoleByIds(roleIds));
    }


}

