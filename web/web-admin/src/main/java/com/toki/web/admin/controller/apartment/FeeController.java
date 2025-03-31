package com.toki.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.toki.common.result.Result;
import com.toki.model.entity.FeeKey;
import com.toki.model.entity.FeeValue;
import com.toki.web.admin.service.FeeKeyService;
import com.toki.web.admin.service.FeeValueService;
import com.toki.web.admin.vo.fee.FeeKeyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "房间杂费管理")
@RestController
@RequestMapping("/admin/fee")
@RequiredArgsConstructor
public class FeeController {

    private final FeeKeyService keyService;
    private final FeeValueService valueService;

    @Operation(summary = "保存或更新杂费名称")
    @PostMapping("key/saveOrUpdate")
    public Result saveOrUpdateFeeKey(@RequestBody FeeKey feeKey) {
        return Result.ok(keyService.saveOrUpdate(feeKey));
    }

    @Operation(summary = "保存或更新杂费值")
    @PostMapping("value/saveOrUpdate")
    public Result saveOrUpdateFeeValue(@RequestBody FeeValue feeValue) {
        return Result.ok(valueService.saveOrUpdate(feeValue));
    }


    /**
     * 多表联查key表和value表
     * */
    @Operation(summary = "查询全部杂费名称和杂费值列表")
    @GetMapping("list")
    public Result<List<FeeKeyVo>> feeInfoList() {
        return keyService.feeInfoList();
    }

    @Operation(summary = "根据id删除杂费名称")
    @DeleteMapping("key/deleteById")
    public Result deleteFeeKeyById(@RequestParam Long feeKeyId) {
        if (feeKeyId == null){
            return Result.fail();
        }
        // 删除杂费名
        keyService.removeById(feeKeyId);
        // 同时删除其对应的全部杂费值
        valueService.remove(new LambdaQueryWrapper<>(FeeValue.class).eq(FeeValue::getFeeKeyId, feeKeyId));
        return Result.ok();
    }

    @Operation(summary = "根据id删除杂费值")
    @DeleteMapping("value/deleteById")
    public Result deleteFeeValueById(@RequestParam Long id) {
        if (id == null){
            return Result.fail();
        }
        return Result.ok(valueService.removeById(id));
    }
}
