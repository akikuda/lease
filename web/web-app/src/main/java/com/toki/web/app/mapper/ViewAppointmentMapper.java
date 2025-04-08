package com.toki.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.model.entity.ViewAppointment;
import com.toki.web.app.vo.appointment.AppointmentItemVo;

import java.util.List;

/**
* @author toki
*/
public interface ViewAppointmentMapper extends BaseMapper<ViewAppointment> {

    List<AppointmentItemVo> listItemByUserId(Long userId);
}




