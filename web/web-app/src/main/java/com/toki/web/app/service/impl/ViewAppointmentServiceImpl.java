package com.toki.web.app.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.toki.model.entity.ViewAppointment;
import com.toki.web.app.mapper.ViewAppointmentMapper;
import com.toki.web.app.service.ApartmentInfoService;
import com.toki.web.app.service.ViewAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.app.vo.apartment.ApartmentItemVo;
import com.toki.web.app.vo.appointment.AppointmentDetailVo;
import com.toki.web.app.vo.appointment.AppointmentItemVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class ViewAppointmentServiceImpl extends ServiceImpl<ViewAppointmentMapper, ViewAppointment>
        implements ViewAppointmentService {

    private final ViewAppointmentMapper viewAppointmentMapper;
    private final ApartmentInfoService apartmentInfoService;

    @Override
    public List<AppointmentItemVo> listItemByUserId(Long userId) {
        return viewAppointmentMapper.listItemByUserId(userId);
    }

    @Override
    public AppointmentDetailVo getDetailByAppointmentId(Long id) {
        final ViewAppointment viewAppointment = viewAppointmentMapper.selectById(id);
        final ApartmentItemVo apartmentItemVo =
                apartmentInfoService.selectApartmentItemVoById(viewAppointment.getApartmentId());

        final AppointmentDetailVo appointmentDetailVo = CglibUtil.copy(viewAppointment, AppointmentDetailVo.class);
        appointmentDetailVo.setApartmentItemVo(apartmentItemVo);

        return appointmentDetailVo;
    }
}




