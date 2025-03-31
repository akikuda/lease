package com.toki.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.toki.common.result.Result;
import com.toki.model.entity.FacilityInfo;
import com.toki.model.enums.ItemType;
import com.toki.web.admin.service.FacilityInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author toki
 */
@Tag(name = "配套管理")
@RestController
@RequestMapping("/admin/facility")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityInfoService facilityInfoService;

    @Operation(summary = "[根据类型]查询配套信息列表")
    @GetMapping("list")
    public Result<List<FacilityInfo>> listFacility(@RequestParam(required = false) ItemType type) {
        final LambdaQueryWrapper<FacilityInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type != null, FacilityInfo::getType, type);
        final List<FacilityInfo> facilityInfoList = facilityInfoService.list(queryWrapper);
        if (facilityInfoList == null) {
            return Result.fail();
        }
        return Result.ok(facilityInfoList);
    }

    @Operation(summary = "新增或修改配套信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody FacilityInfo facilityInfo) {
        return Result.ok(facilityInfoService.saveOrUpdate(facilityInfo));
    }

    @Operation(summary = "根据id删除配套信息")
    @DeleteMapping("deleteById")
    public Result removeFacilityById(@RequestParam Long id) {
        if (id == null) {
            return Result.fail();
        }
        return Result.ok(facilityInfoService.removeById(id));
    }

}
