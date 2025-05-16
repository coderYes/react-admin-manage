package com.illusiontour.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色信息表
 *
 * @TableName sys_role
 */
@Schema(description = "角色信息表")
@TableName(value = "sys_role")
@Data
public class SysRole extends BaseEntity {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色权限字符串")
    private String roleKey;

    @Schema(description = "显示顺序")
    private Integer roleSort;

    @Schema(description = "角色状态（0正常 1停用）")
    private String status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "菜单组")
    private Long[] menuIds;

    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    @TableLogic //标记逻辑删除字段，使用MyBatis Plus的删除方法时，逻辑删除会自动生效。会将该字段更新为1，而不是实际删除记录。
    @JsonIgnore //标记序列化被忽略的字段
    private Byte isDeleted;

    public boolean isAdmin() {
        return isAdmin(this.getId());
    }

    public static boolean isAdmin(Long roleId) {
        return roleId != null && 1L == roleId;
    }

    public SysRole() {
    }

    public SysRole(Long id) {
        this.id = id;
    }

}