package com.illusiontour.admin.controller.system;

import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.common.core.domain.modal.CaptchaVo;
import com.illusiontour.common.core.domain.modal.LoginVo;
import com.illusiontour.common.login.LoginUserHolder;
import com.illusiontour.common.result.Result;
import com.illusiontour.framework.web.service.SysPermissionService;
import com.illusiontour.system.service.SysLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Tag(name = "全景后台管理系统登录管理")
@RestController
@RequestMapping("")
public class SysLoginController {
    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Operation(summary = "获取图形验证码")
    @GetMapping("/captcha")
    public Result<CaptchaVo> getCaptcha() {
        CaptchaVo captcha = sysLoginService.getCaptcha();
        return Result.success(captcha);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String token = sysLoginService.login(loginVo);
        return Result.success(token);
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/system/getInfo")
    public Result<Map<String, Object>> getInfo() {
        SysUser sysUser = LoginUserHolder.getLoginUser().getSysUser();
        // 权限集合
        Set<String> perms = LoginUserHolder.getLoginUser().getPerms();
        // 角色集合
        Set<String> roles = sysPermissionService.getRolePermission(sysUser);

        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("user", sysUser);
        userInfoMap.put("roles", roles);
        userInfoMap.put("permissions", perms);

        return Result.success(userInfoMap);
    }

}
