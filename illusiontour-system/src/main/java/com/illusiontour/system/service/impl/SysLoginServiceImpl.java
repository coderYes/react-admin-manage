package com.illusiontour.system.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.common.core.domain.modal.CaptchaVo;
import com.illusiontour.common.core.domain.modal.LoginVo;
import com.illusiontour.common.utils.SecurityUtils;
import com.illusiontour.system.mapper.SysUserMapper;
import com.illusiontour.system.service.SysLoginService;
import com.illusiontour.common.constant.RedisConstant;
import com.illusiontour.common.exception.TourException;
import com.illusiontour.common.result.ResultCodeEnum;
import com.illusiontour.common.utils.JwtUtil;
import com.illusiontour.common.exception.enums.BaseStatus;
import com.illusiontour.system.service.SysMenuService;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class SysLoginServiceImpl implements SysLoginService {
//    @Autowired
//    AuthenticationManager authenticationManager;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysMenuService sysMenuService;


    @Override
    public CaptchaVo getCaptcha() {
        //使用easyCaptcha生成验证码图片
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);//新建实例，配置相关信息
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);//设置字符类型

        //保存验证码uuid+code到redis中
        String code = specCaptcha.text().toLowerCase();//忽略大小写
        String key = RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID();//根据规则拼接key
        redisTemplate.opsForValue().set(key, code, RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC, TimeUnit.SECONDS);//存入redis中，带有过期时间。

        //构造vo并返回。
        String image = specCaptcha.toBase64();//对图片进行base64编码
        return new CaptchaVo(image, key);//返回image+key
    }


    @Override
    public String login(LoginVo loginVo) {
        try {
            validateCaptcha(loginVo); // 验证码校验

            SysUser sysUser = loginPreCheck(loginVo); //登录用户校验

            Set<String> perms = new HashSet<>();
            if (sysUser.isAdmin()) {
                perms.add("*:*:*");
            } else {
                perms.addAll(sysMenuService.selectRolePermissionByUserId(sysUser.getId()));
            }


//        Authentication authentication = null;
//
//        try {
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
//            authentication = authenticationManager.authenticate(authenticationToken);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        } catch (Exception e) {
//            throw new TourException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
//        } finally {
//            SecurityContextHolder.clearContext();
//        }

            return JwtUtil.createToken(sysUser, perms);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create JWT token", e);
        }
    }


    public void validateCaptcha(LoginVo loginVo) {
        if (!StringUtils.hasText(loginVo.getCode())) {
            throw new TourException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }

        String code = redisTemplate.opsForValue().get(loginVo.getUuid()); // 从redis中取key对应的验证码code
        if (code == null) {
            throw new TourException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED); // 验证码过期（redis中没有key-code，说明已经过期。）
        }
        if (!code.equals(loginVo.getCode().toLowerCase())) {
            throw new TourException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }
    }

    private SysUser loginPreCheck(LoginVo loginVo) {
        // 校验用户是否存在
        SysUser sysUser = sysUserMapper.selectOneByUsername(loginVo.getUsername());
        if (sysUser == null) {
            throw new TourException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }

        // 校验用户是否被禁
        if (sysUser.getStatus().equals(BaseStatus.DISABLE.getCode())) {
            throw new TourException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR); // 账户被封禁异常
        }

        // 校验用户密码
        if (!SecurityUtils.matchesPassword(loginVo.getPassword(), sysUser.getPassword())) {
            throw new TourException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);//密码错误异常。
        }

        // 将密码字段设置为 null
        sysUser.setPassword(null);

        return sysUser;
    }


}
