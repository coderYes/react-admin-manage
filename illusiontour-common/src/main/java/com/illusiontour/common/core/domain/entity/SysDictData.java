package com.illusiontour.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "字典数据表")
@TableName(value = "sys_dict_data")
@Data
public class SysDictData extends BaseEntity {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典项名称")
    private String dictLabel;

    @Schema(description = "字典项值")
    private String dictValue;

    @Schema(description = "字典项描述")
    private String dictDesc;

    @Schema(description = "状态（0正常1停用）")
    private Integer status;

    @Schema(description = "显示顺序")
    private String sort;
}
