package com.toki.web.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.toki.model.entity.ViewAppointment;
import com.toki.web.admin.mapper.ViewAppointmentMapper;
import com.toki.web.admin.service.ViewAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.admin.vo.appointment.AppointmentQueryVo;
import com.toki.web.admin.vo.appointment.AppointmentVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author toki
 * 针对表【view_appointment(预约看房信息表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class ViewAppointmentServiceImpl extends ServiceImpl<ViewAppointmentMapper, ViewAppointment>
        implements ViewAppointmentService {

    private final ViewAppointmentMapper viewAppointmentMapper;

    @Override
    public IPage<AppointmentVo> pageAppointmentByQuery(IPage<AppointmentVo> page, AppointmentQueryVo queryVo) {
        return viewAppointmentMapper.pageAppointmentByQuery(page, queryVo);
    }
}




