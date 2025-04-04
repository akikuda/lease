package com.toki.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.toki.model.entity.ViewAppointment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.admin.vo.appointment.AppointmentQueryVo;
import com.toki.web.admin.vo.appointment.AppointmentVo;

/**
* @author toki
* 针对表【view_appointment(预约看房信息表)】的数据库操作Service
*/
public interface ViewAppointmentService extends IService<ViewAppointment> {

    IPage<AppointmentVo> pageAppointmentByQuery(IPage<AppointmentVo> page, AppointmentQueryVo queryVo);
}
