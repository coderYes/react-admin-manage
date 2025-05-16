package com.illusiontour.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.illusiontour.common.core.domain.entity.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zgw
 * @description 针对表【sys_dict_data】的数据库操作Mapper
 * @createDate 2025-04-10 10:15:06
 * @Entity com.illusiontour.common.core/domain/entity.SysDictData
 */
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    int updateDictDataCode(@Param("oldDictCode") String oldDictCode, @Param("newDictCode") String newDictCode);

    List<SysDictData> selectDictDataByCode(String dictCode);

    int countDictDataByCode(String dictCode);

    List<SysDictData> selectDictDataList(SysDictData dictData);

    void deleteByDictCode(String dictCode);

    void insertDictData(SysDictData dictData);

}
