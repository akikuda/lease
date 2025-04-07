package com.toki.web.app.service.impl;

import com.toki.model.entity.PaymentType;
import com.toki.web.app.mapper.PaymentTypeMapper;
import com.toki.web.app.service.PaymentTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.app.service.RoomInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author toki
*/
@Service
@RequiredArgsConstructor
public class PaymentTypeServiceImpl extends ServiceImpl<PaymentTypeMapper, PaymentType>
    implements PaymentTypeService{

    private final PaymentTypeMapper paymentTypeMapper;

    @Override
    public List<PaymentType> selectListByRoomId(Long id) {
        return paymentTypeMapper.selectListByRoomId(id);
    }
}




