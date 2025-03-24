package com.illusiontour.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.illusiontour.common.core.domain.entity.SysUser;
import com.illusiontour.common.core.domain.entity.SysUserRole;

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    int deleteUserRole(Long[] ids);

    int deleteUserRoleByUserId(Long userId);
}
