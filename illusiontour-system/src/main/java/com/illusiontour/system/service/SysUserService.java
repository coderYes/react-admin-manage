package com.illusiontour.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.illusiontour.common.core.domain.entity.SysUser;

/**
 * @author zgw
 * @description 针对表【sys_user(用户信息表)】的数据库操作Service
 * @createDate 2025-01-24 10:35:23
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 分页查询用户列表
     *
     * @param page    分页对象
     * @param sysUser 用户查询条件对象
     * @return 包含用户列表的分页对象
     */
    IPage<SysUser> selectUserList(IPage<SysUser> page, SysUser sysUser);

    /**
     * 根据用户ID查询用户对象
     *
     * @param userId 用户ID
     * @return 用户对象
     */
    SysUser selectUserById(Long userId);

    /**
     * 检查用户名是否唯一
     *
     * @param sysUser 用户对象，包含要检查的用户名
     * @return 如果用户名唯一，返回 true；否则返回 false
     */
    boolean checkUserNameUnique(SysUser sysUser);

    /**
     * 检查手机号是否唯一
     *
     * @param sysUser 用户对象，包含要检查的手机号
     * @return 如果手机号唯一，返回 true；否则返回 false
     */
    boolean checkPhoneUnique(SysUser sysUser);

    /**
     * 检查邮箱是否唯一
     *
     * @param sysUser 用户对象，包含要检查的手机号
     * @return 如果邮箱唯一，返回 true；否则返回 false
     */
    boolean checkEmailUnique(SysUser sysUser);


    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    void checkUserAllowed(SysUser user);

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    void checkUserDataScope(Long userId);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(SysUser user);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    int deleteUserByIds(Long[] userIds);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(SysUser user);
}
