package com.illusiontour.common.core.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "字典类型表")
@TableName(value = "sys_dict_type")
@Data
public class SysDictType extends BaseEntity {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典描述")
    private String remark;

    @Schema(description = "状态（0正常1停用）")
    private Integer status;

}