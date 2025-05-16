package com.illusiontour.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.illusiontour.common.core.domain.entity.SysDictType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author zgw
 * @description 针对表【sys_dict_type】的数据库操作Service
 * @createDate 2025-04-02 15:20:06
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * 分页查询字典
     *
     * @param page        分页对象
     * @param SysDictType 查询条件对象
     * @return 字典分页对象
     */
    IPage<SysDictType> selectDictTypeList(IPage<SysDictType> page, SysDictType SysDictType);

    /**
     * 根据字典类型ID查询信息
     *
     * @param id 字典类型ID
     * @return 字典类型
     */
    SysDictType selectDictTypeById(Long id);

    /**
     * 根据字典编码ID查询信息
     *
     * @param dictCode 字典编码
     * @return 字典类型信息
     */
    SysDictType selectDictTypeByDictCode(String dictCode);

    /**
     * 校验字典编码是否唯一
     *
     * @param dictCode 字典编码
     * @return 结果
     */
    boolean checkDictCodeUnique(String dictCode);

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    int insertDictType(SysDictType dictType);

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    int updateDictType(SysDictType dictType);

    /**
     * 批量删除字典信息
     *
     * @param ids 需要删除的字典ID
     */
    void deleteDictTypeByIds(Long[] ids);

    /**
     * 删除字典缓存
     *
     * @param dictCode 字典编码
     */
    void deleteCacheByCode(String dictCode);

    /**
     * 加载字典缓存数据
     */
    void loadingDictCache();

    /**
     * 清空字典缓存数据
     */
    void clearDictCache();

    /**
     * 重置字典缓存数据
     */
    void resetDictCache();
}
