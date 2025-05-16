package com.illusiontour.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.illusiontour.common.core.controller.BaseController;
import com.illusiontour.common.core.domain.entity.SysDictType;
import com.illusiontour.common.result.Result;
import com.illusiontour.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "全景后台管理系统字典管理")
@RestController
@RequestMapping("/system/dict")
public class SysDictTypeController extends BaseController {
    @Autowired
    private SysDictTypeService sysDictTypeService;

    @Operation(summary = "获取字典列表")
    @GetMapping("/list")
    public Result<IPage<SysDictType>> list(@RequestParam long current, @RequestParam long size, SysDictType SysDictType) {
        IPage<SysDictType> page = new Page<>(current, size);
        IPage<SysDictType> listIPage = sysDictTypeService.selectDictTypeList(page, SysDictType);
        return Result.success(listIPage);
    }

    @Operation(summary = "查询字典类型详细")
    @GetMapping("/{dictCode}")
    public Result<SysDictType> getInfo(@PathVariable String dictCode) {
        return Result.success(sysDictTypeService.selectDictTypeByDictCode(dictCode));
    }

    @Operation(summary = "新增字典类型")
    @PostMapping("/add")
    public Result<Integer> add(@RequestBody SysDictType sysDictType) {
        if (!sysDictTypeService.checkDictCodeUnique(sysDictType.getDictCode())) {
            return Result.fail("新增字典'" + sysDictType.getDictName() + "'失败，字典编码已存在");
        }
        sysDictType.setCreateBy(getUsername());
        return Result.success(sysDictTypeService.insertDictType(sysDictType));
    }

    @Operation(summary = "修改字典类型")
    @PutMapping("/edit")
    public Result<Integer> edit(@RequestBody SysDictType sysDictType) {
        if (!sysDictTypeService.checkDictCodeUnique(sysDictType.getDictCode())) {
            return Result.fail("修改字典'" + sysDictType.getDictName() + "'失败，字典编码已存在");
        }
        sysDictType.setCreateBy(getUsername());
        return Result.success(sysDictTypeService.updateDictType(sysDictType));
    }

    @Operation(summary = "删除字典类型")
    @DeleteMapping("/delete/{ids}")
    public Result delete(@PathVariable Long[] ids) {
        sysDictTypeService.deleteDictTypeByIds(ids);
        return Result.success();
    }

    @Operation(summary = "刷新字典缓存")
    @DeleteMapping("/refreshCache")
    public Result refreshCache() {
        sysDictTypeService.resetDictCache();
        return Result.success();
    }

    @Operation(summary = "删除字典缓存")
    @DeleteMapping("/deleteCacheByCode/{dictCode}")
    public Result deleteCacheByCode(@PathVariable String dictCode) {
        sysDictTypeService.deleteCacheByCode(dictCode);
        return Result.success();
    }
}
