package com.toki.web.app.controller.apartment;

import com.toki.common.result.Result;
import com.toki.web.app.service.ApartmentInfoService;
import com.toki.web.app.vo.apartment.ApartmentDetailVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toki
 */
@RestController
@Tag(name = "公寓信息")
@RequestMapping("/app/apartment")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentInfoService apartmentInfoService;

    @Operation(summary = "根据id获取公寓信息")
    @GetMapping("getDetailById")
    public Result<ApartmentDetailVo> getDetailById(@RequestParam Long id) {
        return Result.ok(apartmentInfoService.getApartmentDetailById(id));
    }
}
