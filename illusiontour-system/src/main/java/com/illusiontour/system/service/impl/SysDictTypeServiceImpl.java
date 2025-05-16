package com.illusiontour.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.illusiontour.common.core.domain.entity.SysDictData;
import com.illusiontour.common.core.domain.entity.SysDictType;
import com.illusiontour.common.exception.TourException;
import com.illusiontour.common.result.HttpStatus;
import com.illusiontour.common.utils.DictUtils;
import com.illusiontour.common.utils.StringUtils;
import com.illusiontour.system.mapper.SysDictDataMapper;
import com.illusiontour.system.service.SysDictTypeService;
import com.illusiontour.system.mapper.SysDictTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zgw
 * @description 针对表【sys_dict_type】的数据库操作Service实现
 * @createDate 2025-04-02 15:20:06
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType>
        implements SysDictTypeService {

    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    /**
     * 分页查询字典
     *
     * @param page        分页对象
     * @param SysDictType 查询条件对象
     * @return 字典分页对象
     */
    @Override
    public IPage<SysDictType> selectDictTypeList(IPage<SysDictType> page, SysDictType SysDictType) {
        return sysDictTypeMapper.selectDictTypeList(page, SysDictType);
    }

    /**
     * 根据字典编码ID查询信息
     *
     * @param dictCode 字典编码
     * @return 字典类型信息
     */
    @Override
    public SysDictType selectDictTypeByDictCode(String dictCode) {
        return sysDictTypeMapper.selectDictTypeByDictCode(dictCode);
    }

    /**
     * 校验字典编码称是否唯一
     *
     * @param dictCode 字典编码
     * @return 结果
     */
    @Override
    public boolean checkDictCodeUnique(String dictCode) {
        SysDictType sysDictType = sysDictTypeMapper.selectDictTypeByDictCode(dictCode);
        if (StringUtils.isNotNull(sysDictType)) {
            return false;
        }
        return true;
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(SysDictType dictType) {
        int row = sysDictTypeMapper.insertDictType(dictType);
        if (row > 0) {
            DictUtils.setDictCache(dictType.getDictCode(), null);
        }
        return row;
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDictType(SysDictType dictType) {
        SysDictType oldDict = sysDictTypeMapper.selectDictTypeById(dictType.getId());
        sysDictDataMapper.updateDictDataCode(oldDict.getDictCode(), dictType.getDictCode());
        int row = sysDictTypeMapper.updateDictCode(dictType);
        if (row > 0) {
            List<SysDictData> dictDatas = sysDictDataMapper.selectDictDataByCode(dictType.getDictCode());
            DictUtils.setDictCache(dictType.getDictCode(), dictDatas);
        }
        return row;
    }


    /**
     * 批量删除字典信息
     *
     * @param ids 需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(Long[] ids) {
        for (Long id : ids) {
            SysDictType dictType = selectDictTypeById(id);
            if (sysDictDataMapper.countDictDataByCode(dictType.getDictCode()) > 0) {
                throw new TourException(String.format("%1$s已分配,不能删除", dictType.getDictName()), HttpStatus.ERROR);
            }
            sysDictTypeMapper.deleteDictTypeById(id);
            DictUtils.removeDictCache(dictType.getDictCode());
        }
    }

    /**
     * 删除字典缓存
     *
     * @param dictCode 字典编码
     */
    @Override
    public void deleteCacheByCode(String dictCode) {
        DictUtils.removeDictCache(dictCode);
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        SysDictData dictData = new SysDictData();
        dictData.setStatus(0);
        Map<String, List<SysDictData>> dictDataMap = sysDictDataMapper.selectDictDataList(dictData).stream().collect(Collectors.groupingBy(SysDictData::getDictCode));
        for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getSort)).collect(Collectors.toList()));
        }
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return sysDictTypeMapper.selectDictTypeById(dictId);
    }
}




