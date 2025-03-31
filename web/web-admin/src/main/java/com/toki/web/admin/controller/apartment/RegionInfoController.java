package com.toki.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.toki.common.result.Result;
import com.toki.model.entity.CityInfo;
import com.toki.model.entity.DistrictInfo;
import com.toki.model.entity.ProvinceInfo;
import com.toki.web.admin.service.CityInfoService;
import com.toki.web.admin.service.DistrictInfoService;
import com.toki.web.admin.service.ProvinceInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "地区信息管理")
@RestController
@RequestMapping("/admin/region")
@RequiredArgsConstructor
public class RegionInfoController {

    private final ProvinceInfoService provinceInfoService;
    private final CityInfoService cityInfoService;
    private final DistrictInfoService districtInfoService;

    @Operation(summary = "查询省份信息列表")
    @GetMapping("province/list")
    public Result<List<ProvinceInfo>> listProvince() {
        return Result.ok(provinceInfoService.list());
    }

    @Operation(summary = "根据省份id查询城市信息列表")
    @GetMapping("city/listByProvinceId")
    public Result<List<CityInfo>> listCityInfoByProvinceId(@RequestParam Long id) {
        if (id == null) {
            return Result.fail();
        }
        return Result.ok(cityInfoService
                .list(new LambdaQueryWrapper<>(CityInfo.class)
                        .eq(CityInfo::getProvinceId, id)));
    }

    @GetMapping("district/listByCityId")
    @Operation(summary = "根据城市id查询区县信息")
    public Result<List<DistrictInfo>> listDistrictInfoByCityId(@RequestParam Long id) {
        if (id == null) {
            return Result.fail();
        }
        return Result.ok(districtInfoService
                .list(new LambdaQueryWrapper<>(DistrictInfo.class)
                        .eq(DistrictInfo::getCityId, id)));
    }

}
