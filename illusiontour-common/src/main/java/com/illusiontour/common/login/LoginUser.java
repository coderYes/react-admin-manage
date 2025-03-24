package com.illusiontour.common.login;

import com.illusiontour.common.core.domain.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * 封装登录用户的信息。
 */
@Data
@AllArgsConstructor
public class LoginUser {
    private SysUser sysUser;
    private Set<String> perms;

}