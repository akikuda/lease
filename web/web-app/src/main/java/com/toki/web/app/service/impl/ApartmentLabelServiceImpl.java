package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.ApartmentLabel;
import com.toki.web.app.service.ApartmentLabelService;
import com.toki.web.app.mapper.ApartmentLabelMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class ApartmentLabelServiceImpl extends ServiceImpl<ApartmentLabelMapper, ApartmentLabel>
    implements ApartmentLabelService{
}
