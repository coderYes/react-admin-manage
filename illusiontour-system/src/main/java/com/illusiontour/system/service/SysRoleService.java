package com.illusiontour.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.illusiontour.common.core.domain.entity.SysRole;

import java.util.List;
import java.util.Set;

/**
 * @author zgw
 * @description 针对表【sys_role(角色信息表)】的数据库操作Service
 * @createDate 2025-02-28 17:37:21
 */
public interface SysRoleService extends IService<SysRole> {
    /**
     * 根据用户ID查询角色权限字符串
     *
     * @param userId 用户ID
     * @return 角色权限字符串列表
     */
    Set<String> selectRoleKeysByUserId(Long userId);

    /**
     * 校验角色是否有数据权限
     *
     * @param roleIds 角色id
     */
    void checkRoleDataScope(Long... roleIds);

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    List<SysRole> selectRoleList(SysRole role);

}
