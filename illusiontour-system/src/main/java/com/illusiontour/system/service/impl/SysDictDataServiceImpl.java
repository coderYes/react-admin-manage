package com.illusiontour.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.illusiontour.common.core.domain.entity.SysDictData;
import com.illusiontour.common.core.domain.entity.SysMenu;
import com.illusiontour.common.utils.DictUtils;
import com.illusiontour.common.utils.StringUtils;
import com.illusiontour.system.mapper.SysDictDataMapper;
import com.illusiontour.system.service.SysDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements SysDictDataService {

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    /**
     * 查询字典数据
     *
     * @param dictCode 字典编码
     */
    @Override
    public List<SysDictData> selectDictDataByDictCode(String dictCode) {
        return sysDictDataMapper.selectDictDataByCode(dictCode);
    }

    /**
     * 根据字典编码查询字典数据
     *
     * @param dictCode 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataByCode(String dictCode) {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictCode);
        if (StringUtils.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        dictDatas = sysDictDataMapper.selectDictDataByCode(dictCode);
        if (StringUtils.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache(dictCode, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 批量删除字典数据
     *
     * @param dictCode 字典编码
     */
    @Override
    public void deleteByDictCode(String dictCode) {
        sysDictDataMapper.deleteByDictCode(dictCode);
    }

    /**
     * 添加字典数据
     *
     * @param dictData 字典数据
     */
    @Override
    public void insertDictData(SysDictData dictData) {
        sysDictDataMapper.insertDictData(dictData);
    }

    /**
     * 刷新字典数据缓存
     *
     * @param dictCode 字典数据
     */
    @Override
    public void refreshDictDataCache(String dictCode) {
        List<SysDictData> dictDatas = sysDictDataMapper.selectDictDataByCode(dictCode);
        DictUtils.setDictCache(dictCode, dictDatas);
    }

}
