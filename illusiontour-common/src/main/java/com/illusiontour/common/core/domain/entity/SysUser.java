package com.illusiontour.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息表
 *
 * @TableName sys_user
 */
@Schema(description = "用户信息表")
@TableName(value = "sys_user")
@Data
public class SysUser extends BaseEntity {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "微信小程序ID")
    private String openId;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户类型（00系统用户）")
    private String userType;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phonenumber;

    @Schema(description = "用户性别（0男 1女 2未知）")
    private String sex;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "帐号状态（0停用 1正常）")
    private Integer status;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    private Date loginDate;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "角色组")
    private Long[] roleIds;


    @Schema(description = "逻辑删除")
    @TableField("is_deleted")
    @TableLogic //标记逻辑删除字段，使用MyBatis Plus的删除方法时，逻辑删除会自动生效。会将该字段更新为1，而不是实际删除记录。
    @JsonIgnore //标记序列化被忽略的字段
    private Byte isDeleted;

    @JsonIgnore
    public boolean isAdmin() {
        return isAdmin(this.getId());
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

}