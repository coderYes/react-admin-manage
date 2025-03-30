package com.illusiontour.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * 根据角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象
     */
    SysRole selectRoleById(Long roleId);

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
     * 校验角色名称是否唯一
     *
     * @param sysRole 角色信息
     */
    boolean checkRoleNameUnique(SysRole sysRole);

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    boolean checkRoleKeyUnique(SysRole role);

    /**
     * 分页查询角色列表
     *
     * @param page    分页对象
     * @param sysRole 角色查询条件对象
     * @return 包含角色列表的分页对象
     */
    IPage<SysRole> selectRoleList(IPage<SysRole> page, SysRole sysRole);

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int insertRole(SysRole role);

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    void checkRoleAllowed(SysRole role);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int countUserRoleByRoleId(Long roleId);

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int updateRole(SysRole role);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    int deleteRoleByIds(Long[] roleIds);

}
