package com.illusiontour.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色和菜单关联表
 * @TableName sys_role_menu
 */
@Schema(description = "角色和菜单关联表")
@TableName(value ="sys_role_menu")
@Data
public class SysRoleMenu {
    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    @TableId
    private Long roleId;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    @TableId
    private Long menuId;
}