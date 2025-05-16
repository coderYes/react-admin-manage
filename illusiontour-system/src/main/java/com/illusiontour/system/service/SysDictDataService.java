package com.illusiontour.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.illusiontour.common.core.domain.entity.SysDictData;

import java.util.List;

/**
 * @author zgw
 * @description 针对表【sys_dict_data】的数据库操作Service
 * @createDate 2025-04-10 10:15:06
 */
public interface SysDictDataService extends IService<SysDictData> {

    /**
     * 查询字典数据
     *
     * @param dictCode 字典编码
     */
    List<SysDictData> selectDictDataByDictCode(String dictCode);

    /**
     * 根据字典编码查询字典数据
     *
     * @param dictCode 字典类型
     * @return 字典数据集合信息
     */
    List<SysDictData> selectDictDataByCode(String dictCode);

    /**
     * 批量删除字典数据
     *
     * @param dictCode 字典编码
     */
    void deleteByDictCode(String dictCode);

    /**
     * 添加字典数据
     *
     * @param dictData 字典数据
     */
    void insertDictData(SysDictData dictData);

    /**
     * 刷新字典数据缓存
     *
     * @param dictCode 字典数据
     */
    void refreshDictDataCache(String dictCode);

}
