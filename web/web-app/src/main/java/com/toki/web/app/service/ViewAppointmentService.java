package com.toki.web.app.service;

import com.toki.model.entity.ViewAppointment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.app.vo.appointment.AppointmentDetailVo;
import com.toki.web.app.vo.appointment.AppointmentItemVo;

import java.util.List;

/**
* @author toki
*/
public interface ViewAppointmentService extends IService<ViewAppointment> {
    List<AppointmentItemVo> listItemByUserId(Long userId);

    AppointmentDetailVo getDetailByAppointmentId(Long id);
}
