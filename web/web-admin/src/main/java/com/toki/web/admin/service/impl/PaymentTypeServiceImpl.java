package com.toki.web.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.PaymentType;
import com.toki.web.admin.service.PaymentTypeService;
import com.toki.web.admin.mapper.PaymentTypeMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class PaymentTypeServiceImpl extends ServiceImpl<PaymentTypeMapper, PaymentType>
    implements PaymentTypeService{

}




