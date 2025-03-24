package com.illusiontour.common.exception.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 枚举类，表示通用状态。
 */
public enum BaseStatus implements BaseEnum {
    ENABLE(0, "正常"),

    DISABLE(1, "禁用");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String name;

    BaseStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
