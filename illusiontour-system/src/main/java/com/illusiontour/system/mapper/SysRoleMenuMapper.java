package com.illusiontour.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.illusiontour.common.core.domain.entity.SysRole;
import com.illusiontour.common.core.domain.entity.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    int batchRoleMenu(List<SysRoleMenu> roleMenuList);

    int deleteRoleMenuByRoleId(Long roleId);

    int deleteRoleMenu(Long[] ids);
}
