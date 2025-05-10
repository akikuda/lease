package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.ApartmentFeeValue;
import com.toki.web.app.service.ApartmentFeeValueService;
import com.toki.web.app.mapper.ApartmentFeeValueMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class ApartmentFeeValueServiceImpl extends ServiceImpl<ApartmentFeeValueMapper, ApartmentFeeValue>
    implements ApartmentFeeValueService{
}




