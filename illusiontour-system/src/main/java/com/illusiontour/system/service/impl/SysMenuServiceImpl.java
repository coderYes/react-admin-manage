package com.illusiontour.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.illusiontour.common.core.domain.entity.SysMenu;
import com.illusiontour.common.core.domain.entity.SysRole;
import com.illusiontour.system.service.SysMenuService;
import com.illusiontour.system.mapper.SysMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zgw
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 * @createDate 2025-03-07 11:12:20
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysMenu> perms = sysMenuMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysMenu perm : perms){
            permsSet.addAll(Arrays.asList(perm.getPerms().trim()));
        }
        return permsSet;
    }
}




