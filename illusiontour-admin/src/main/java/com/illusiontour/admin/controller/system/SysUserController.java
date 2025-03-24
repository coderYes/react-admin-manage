package com.illusiontour.admin.controller.system;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.illusiontour.common.core.controller.BaseController;
import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.common.exception.TourException;
import com.illusiontour.common.result.HttpStatus;
import com.illusiontour.common.result.Result;
import com.illusiontour.common.utils.SecurityUtils;
import com.illusiontour.common.utils.StringUtils;
import com.illusiontour.system.service.SysRoleService;
import com.illusiontour.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "全景后台管理系统用户管理")
@RestController
@RequestMapping("/system")
public class SysUserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Operation(summary = "获取用户列表")
    @GetMapping("/user/list")
    public Result<IPage<SysUser>> list(@RequestParam long current, @RequestParam long size, SysUser sysUser) {
        IPage<SysUser> page = new Page<>(current, size);
        IPage<SysUser> sysUserIPage = sysUserService.selectUserList(page, sysUser);
        return Result.success(sysUserIPage);
    }

    @Operation(summary = "用户创建")
    @PostMapping("/user/add")
    public Result<Integer> add(@RequestBody SysUser sysUser) {
        sysRoleService.checkRoleDataScope(sysUser.getRoleIds());
        if (StringUtils.isNull(sysUser.getNickName())) {
            throw new TourException("新增用户'" + sysUser.getUserName() + "'失败，用户昵称不能为空", HttpStatus.ERROR);
        } else if (StringUtils.isNull(sysUser.getStatus())) {
            throw new TourException("新增用户'" + sysUser.getUserName() + "'失败，状态不能为空", HttpStatus.ERROR);
        } else if (!sysUserService.checkUserNameUnique(sysUser)) {
            throw new TourException("新增用户'" + sysUser.getUserName() + "'失败，登录账号已存在", HttpStatus.ERROR);
        } else if (StringUtils.isNotEmpty(sysUser.getPhonenumber()) && !sysUserService.checkPhoneUnique(sysUser)) {
            throw new TourException("新增用户'" + sysUser.getUserName() + "'失败，手机号码已存在", HttpStatus.ERROR);
        } else if (StringUtils.isNotEmpty(sysUser.getEmail()) && !sysUserService.checkEmailUnique(sysUser)) {
            throw new TourException("新增用户'" + sysUser.getUserName() + "'失败，邮箱账号已存在", HttpStatus.ERROR);
        }
        sysUser.setCreateBy(getUsername());
        sysUser.setPassword(SecurityUtils.encryptPassword(sysUser.getPassword()));
        return Result.success(sysUserService.insertUser(sysUser));
    }

    @Operation(summary = "用户删除")
    @DeleteMapping("/user/delete/{userIds}")
    public Result<Integer> delete(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, getUserId())) {
            return Result.fail("当前用户不能删除");
        }
        return Result.success(sysUserService.deleteUserByIds(userIds));
    }

    @Operation(summary = "用户编辑")
    @PutMapping("/user/edit")
    public Result<Integer> edit(@RequestBody SysUser sysUser) {
        sysUserService.checkUserAllowed(sysUser);
        sysUserService.checkUserDataScope(sysUser.getId());
        sysRoleService.checkRoleDataScope(sysUser.getRoleIds());
        if (!sysUserService.checkUserNameUnique(sysUser)) {
            return Result.fail("修改用户'" + sysUser.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(sysUser.getPhonenumber()) && !sysUserService.checkPhoneUnique(sysUser)) {
            return Result.fail("修改用户'" + sysUser.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(sysUser.getEmail()) && !sysUserService.checkEmailUnique(sysUser)) {
            return Result.fail("修改用户'" + sysUser.getUserName() + "'失败，邮箱账号已存在");
        }
        sysUser.setUpdateBy(getUsername());
        return Result.success(sysUserService.updateUser(sysUser));
    }
}
