package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.FacilityInfo;
import com.toki.web.app.service.FacilityInfoService;
import com.toki.web.app.mapper.FacilityInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class FacilityInfoServiceImpl extends ServiceImpl<FacilityInfoMapper, FacilityInfo>
    implements FacilityInfoService{

}




