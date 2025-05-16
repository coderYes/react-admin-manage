package com.illusiontour.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.illusiontour.common.core.domain.entity.SysDictType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author zgw
 * @description 针对表【sys_dict_type】的数据库操作Mapper
 * @createDate 2025-04-02 15:20:06
 * @Entity com.illusiontour.common.core/domain/entity.SysDictType
 */
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {
    IPage<SysDictType> selectDictTypeList(IPage<SysDictType> page, SysDictType sysDictType);

    SysDictType selectDictTypeByDictCode(String dictCode);

    int insertDictType(SysDictType dictType);

    int updateDictCode(SysDictType dictType);

    SysDictType selectDictTypeById(Long dictId);

    int deleteDictTypeById(Long id);
}




