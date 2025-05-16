package com.illusiontour.admin.controller.system;

import com.illusiontour.common.core.controller.BaseController;
import com.illusiontour.common.core.domain.entity.SysDictData;
import com.illusiontour.common.core.domain.entity.SysDictType;
import com.illusiontour.common.exception.TourException;
import com.illusiontour.common.result.HttpStatus;
import com.illusiontour.common.result.Result;
import com.illusiontour.common.utils.StringUtils;
import com.illusiontour.system.service.SysDictDataService;
import com.illusiontour.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Tag(name = "全景后台管理系统字典数据管理")
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
    @Autowired
    private SysDictTypeService sysDictTypeService;

    @Autowired
    private SysDictDataService sysDictDataService;

    @Operation(summary = "查询字典数据")
    @GetMapping("/{dictCode}")
    public Result<List<SysDictData>> list(@PathVariable String dictCode) {
        List<SysDictData> list = sysDictDataService.selectDictDataByDictCode(dictCode);
        return Result.success(list);
    }

    @Operation(summary = "根据字典编码查询字典数据信息")
    @GetMapping("/code/{dictCode}")
    public Result<List<SysDictData>> listByCode(@PathVariable String dictCode) {
        List<SysDictData> list = sysDictDataService.selectDictDataByCode(dictCode);
        if (StringUtils.isNull(list)) {
            list = new ArrayList<SysDictData>();
        }
        return Result.success(list);
    }

    @Operation(summary = "批量操作字典数据")
    @PostMapping("/batchDictData/{dictCode}")
    @Transactional
    public Result batchDictData(
            @PathVariable String dictCode,
            @RequestBody List<SysDictData> dictDataList) {

        // 1. 校验字典类型是否存在
        boolean dictExists = sysDictTypeService.lambdaQuery()
                .eq(SysDictType::getDictCode, dictCode)
                .exists();
        if (!dictExists) {
            throw new TourException("字典编码不存在", HttpStatus.ERROR);
        }

        // 2. 查询现有字典数据，转为 Map<dictValue, SysDictData>
        Map<String, SysDictData> existingMap = sysDictDataService.lambdaQuery()
                .eq(SysDictData::getDictCode, dictCode)
                .list()
                .stream()
                .collect(Collectors.toMap(SysDictData::getDictValue, Function.identity()));

        // 3. 收集传入数据的 dictValue 集合（用于删除判断）
        Set<String> incomingValues = dictDataList.stream()
                .map(SysDictData::getDictValue)
                .collect(Collectors.toSet());

        // 4. 分离新增、更新、删除
        List<SysDictData> toInsert = new ArrayList<>();
        List<SysDictData> toUpdate = new ArrayList<>();
        List<Long> toDeleteIds = new ArrayList<>();

        // 处理删除：存在旧数据但新数据中没有的条目
        existingMap.values().stream()
                .filter(data -> !incomingValues.contains(data.getDictValue()))
                .forEach(data -> toDeleteIds.add(data.getId()));

        // 处理新增/更新
        for (SysDictData data : dictDataList) {
            if (!dictCode.equals(data.getDictCode())) {
                throw new TourException(
                        "参数中的dictCode：" + data.getDictCode() + "与路径" + dictCode + "不一致",
                        HttpStatus.ERROR);
            }

            data.setUpdateBy(getUsername());
            SysDictData existingData = existingMap.get(data.getDictValue());

            if (existingData == null) {
                toInsert.add(data);
            } else {
                data.setId(existingData.getId());  // 确保更新能匹配到ID
                toUpdate.add(data);
            }
        }

        // 5. 批量执行操作（注意删除操作建议放在最前）
        if (!toDeleteIds.isEmpty()) {
            sysDictDataService.removeBatchByIds(toDeleteIds);  // MyBatis-Plus 批量删除
        }
        if (!toInsert.isEmpty()) {
            sysDictDataService.saveBatch(toInsert);
        }
        if (!toUpdate.isEmpty()) {
            sysDictDataService.updateBatchById(toUpdate);
        }

        // 6. 刷新缓存
        sysDictDataService.refreshDictDataCache(dictCode);

        return Result.success();
    }
}
