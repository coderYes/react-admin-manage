package com.illusiontour.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.illusiontour.common.exception.TourException;
import com.illusiontour.common.login.LoginUserHolder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseEntity implements Serializable {
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT) //自动填充字段，配置时机，插入数据时填充。
    private Date createTime;

    @Schema(description = "创建者")
    @TableField(value = "create_by") //自动填充字段，配置时机，插入数据时填充。
    private String createBy;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.UPDATE) //自动填充字段，配置时机，更新数据时填充
    private Date updateTime;

    @Schema(description = "更新者")
    @TableField(value = "update_by") //自动填充字段，配置时机，插入数据时填充。
    private String updateBy;

    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    @TableLogic //标记逻辑删除字段，使用MyBatis Plus的删除方法时，逻辑删除会自动生效。会将该字段更新为1，而不是实际删除记录。
    @JsonIgnore //标记序列化被忽略的字段
    private Byte isDeleted;

    @Schema(description = "请求参数")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }


}
