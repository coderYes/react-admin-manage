package com.illusiontour.common.core.controller;

import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.common.login.LoginUserHolder;

public class BaseController {
    /**
     * 获取登录用户id
     */
    public Long getUserId() {
        return LoginUserHolder.getLoginUser().getSysUser().getId();
    }

    /**
     * 获取登录用户id
     */
    public String getUsername() {
        return LoginUserHolder.getLoginUser().getSysUser().getUserName();
    }

    /**
     * 获取用户缓存信息
     */
    public SysUser getLoginUser()
    {
        return LoginUserHolder.getLoginUser().getSysUser();
    }
}
