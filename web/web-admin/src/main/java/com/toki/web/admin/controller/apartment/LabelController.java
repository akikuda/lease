package com.toki.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.toki.common.result.Result;
import com.toki.model.entity.LabelInfo;
import com.toki.model.enums.ItemType;
import com.toki.web.admin.service.LabelInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author toki
 */
@Tag(name = "标签管理")
@RestController
@RequestMapping("/admin/label")
@RequiredArgsConstructor
public class LabelController {

    private final LabelInfoService labelInfoService;

    @Operation(summary = "（根据类型）查询标签列表")
    @GetMapping("list")
    public Result<List<LabelInfo>> labelList(@RequestParam(required = false) ItemType type) {
        final LambdaQueryWrapper<LabelInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type!=null, LabelInfo::getType, type);
        final List<LabelInfo> labelInfoList = labelInfoService.list(queryWrapper);
        if (labelInfoList == null){
            return Result.fail();
        }
        return Result.ok(labelInfoList);
    }

    @Operation(summary = "新增或修改标签信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdateLabel(@RequestBody LabelInfo labelInfo) {
        return Result.ok(labelInfoService.saveOrUpdate(labelInfo));
    }

    @Operation(summary = "根据id删除标签信息")
    @DeleteMapping("deleteById")
    public Result deleteLabelById(@RequestParam Long id) {
        if (id == null) {
            return Result.fail();
        }
        return Result.ok(labelInfoService.removeById(id));
    }
}
