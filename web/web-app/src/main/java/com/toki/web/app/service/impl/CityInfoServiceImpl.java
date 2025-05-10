package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.CityInfo;
import com.toki.web.app.service.CityInfoService;
import com.toki.web.app.mapper.CityInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class CityInfoServiceImpl extends ServiceImpl<CityInfoMapper, CityInfo>
    implements CityInfoService{

}




