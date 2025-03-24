package com.illusiontour.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色信息表
 * @TableName sys_role
 */
@Schema(description = "角色信息表")
@TableName(value ="sys_role")
@Data
public class SysRole extends BaseEntity {

    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色权限字符串
     */
    @Schema(description = "角色权限字符串")
    private String roleKey;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    private Integer roleSort;

    /**
     * 角色状态（0正常 1停用）
     */
    @Schema(description = "角色状态（0正常 1停用）")
    private String status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}