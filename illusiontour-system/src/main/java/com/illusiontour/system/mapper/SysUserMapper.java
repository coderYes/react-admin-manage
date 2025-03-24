package com.illusiontour.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.illusiontour.common.core.domain.entity.SysUser;

/**
 * @author zgw
 * @description 针对表【sys_user(用户信息表)】的数据库操作Mapper
 * @createDate 2025-01-24 10:35:23
 * @Entity com.illusiontour.common.domain.entity.SysUser
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser selectOneByUsername(String username);

    IPage<SysUser> selectUserList(IPage<SysUser> page, SysUser sysUser);

    SysUser selectUserById(Long userId);

    SysUser checkUserNameUnique(String userName);

    SysUser checkPhoneUnique(String phonenumber);

    SysUser checkEmailUnique(String email);

    int insertUser(SysUser sysUser);

    int deleteUserByIds(Long[] userIds);

    int updateUser(SysUser user);
}




