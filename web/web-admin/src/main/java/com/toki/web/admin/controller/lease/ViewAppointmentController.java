package com.toki.web.admin.controller.lease;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.result.Result;
import com.toki.model.entity.ViewAppointment;
import com.toki.model.enums.AppointmentStatus;
import com.toki.web.admin.service.ViewAppointmentService;
import com.toki.web.admin.vo.appointment.AppointmentQueryVo;
import com.toki.web.admin.vo.appointment.AppointmentVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "预约看房管理")
@RequestMapping("/admin/appointment")
@RestController
@RequiredArgsConstructor
public class ViewAppointmentController {

    private final ViewAppointmentService viewAppointmentService;

    @Operation(summary = "分页查询预约信息")
    @GetMapping("page")
    public Result<IPage<AppointmentVo>> page(@RequestParam long current, @RequestParam long size, AppointmentQueryVo queryVo) {
        IPage<AppointmentVo> page = new Page<>(current, size);
        return Result.ok(viewAppointmentService.pageAppointmentByQuery(page, queryVo));
    }

    @Operation(summary = "根据id更新预约状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam AppointmentStatus status) {
        viewAppointmentService.update(
                new LambdaUpdateWrapper<>(ViewAppointment.class)
                        .eq(ViewAppointment::getId, id)
                        .set(ViewAppointment::getAppointmentStatus, status)
        );
        return Result.ok();
    }

}
