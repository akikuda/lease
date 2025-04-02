package com.toki.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.result.Result;
import com.toki.model.entity.ApartmentInfo;
import com.toki.model.enums.ReleaseStatus;
import com.toki.web.admin.service.ApartmentInfoService;
import com.toki.web.admin.vo.apartment.ApartmentDetailVo;
import com.toki.web.admin.vo.apartment.ApartmentItemVo;
import com.toki.web.admin.vo.apartment.ApartmentQueryVo;
import com.toki.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author toki
 */
@Tag(name = "公寓信息管理")
@RestController
@RequestMapping("/admin/apartment")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentInfoService InfoService;

    @Operation(summary = "保存或更新公寓信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ApartmentSubmitVo apartmentSubmitVo) {
        InfoService.saveOrUpdateApartment(apartmentSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询公寓列表")
    @GetMapping("pageItem")
    public Result<IPage<ApartmentItemVo>> pageItem(@RequestParam long current, @RequestParam long size, ApartmentQueryVo queryVo) {
        final Page<ApartmentItemVo> page = new Page<>(current, size);
        IPage<ApartmentItemVo> result = InfoService.pageItem(page, queryVo);
        return Result.ok(result);
    }

    @Operation(summary = "根据ID获取公寓详细信息")
    @GetMapping("getDetailById")
    public Result<ApartmentDetailVo> getDetailById(@RequestParam Long id) {
        ApartmentDetailVo apartmentDetailVo = InfoService.getApartmentDetailById(id);
        return Result.ok(apartmentDetailVo);
    }

    @Operation(summary = "根据id删除公寓信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        InfoService.removeApartmentById(id);
        return Result.ok();
    }

    @Operation(summary = "根据id修改公寓发布状态")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status) {
        return Result.ok(
                InfoService.update(
                        new LambdaUpdateWrapper<>(ApartmentInfo.class)
                                .eq(ApartmentInfo::getId, id)
                                .set(ApartmentInfo::getIsRelease, status)
                )
        );
    }

    @Operation(summary = "根据区县id查询公寓信息列表")
    @GetMapping("listInfoByDistrictId")
    public Result<List<ApartmentInfo>> listInfoByDistrictId(@RequestParam Long id) {
        return Result.ok(
                InfoService.list(
                        new LambdaQueryWrapper<>(ApartmentInfo.class)
                                .eq(ApartmentInfo::getDistrictId, id)
                )
        );
    }
}














