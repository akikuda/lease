package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.ApartmentFacility;
import com.toki.web.app.service.ApartmentFacilityService;
import com.toki.web.app.mapper.ApartmentFacilityMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class ApartmentFacilityServiceImpl extends ServiceImpl<ApartmentFacilityMapper, ApartmentFacility>
    implements ApartmentFacilityService{
}




