package com.illusiontour.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.illusiontour.common.core.domain.entity.SysRole;
import com.illusiontour.common.core.domain.entity.SysUserRole;

import java.util.List;

/**
 * @author zgw
 * @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
 * @createDate 2025-02-28 17:37:21
 * @Entity com.illusiontour.admin.domain.entity.SysRole
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectRoleKeysByUserId(Long userId);

    List<SysRole> selectRoleList(SysRole role);

    int batchUserRole(List<SysUserRole> userRoleList);
}




