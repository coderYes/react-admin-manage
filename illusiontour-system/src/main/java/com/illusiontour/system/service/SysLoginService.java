package com.illusiontour.system.service;

import com.illusiontour.common.core.domain.modal.CaptchaVo;
import com.illusiontour.common.core.domain.modal.LoginVo;

public interface SysLoginService {
    CaptchaVo getCaptcha();

    String login(LoginVo loginVo);
}
