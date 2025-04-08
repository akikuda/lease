package com.toki.web.app.controller.appointment;



import com.toki.common.result.Result;
import com.toki.common.utils.LoginUserHolder;
import com.toki.model.entity.ViewAppointment;
import com.toki.web.app.service.ViewAppointmentService;
import com.toki.web.app.vo.appointment.AppointmentDetailVo;
import com.toki.web.app.vo.appointment.AppointmentItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author toki
 */
@Tag(name = "看房预约信息")
@RestController
@RequestMapping("/app/appointment")
@RequiredArgsConstructor
public class ViewAppointmentController {

    private final ViewAppointmentService viewAppointmentService;

    @Operation(summary = "保存或更新看房预约")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ViewAppointment viewAppointment) {
        // 设置当前用户ID再保存或更新
        viewAppointment.setUserId(LoginUserHolder.getLoginUser().getUserId());
        viewAppointmentService.saveOrUpdate(viewAppointment);
        return Result.ok();
    }

    @Operation(summary = "查询个人预约看房列表")
    @GetMapping("listItem")
    public Result<List<AppointmentItemVo>> listItem() {
        return Result.ok(viewAppointmentService.listItemByUserId(LoginUserHolder.getLoginUser().getUserId()));
    }

    @GetMapping("getDetailById")
    @Operation(summary = "根据ID查询预约详情信息")
    public Result<AppointmentDetailVo> getDetailById(Long id) {
        return Result.ok(viewAppointmentService.getDetailByAppointmentId(id));
    }

}

