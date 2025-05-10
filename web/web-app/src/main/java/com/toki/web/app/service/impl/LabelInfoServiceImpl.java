package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.LabelInfo;
import com.toki.web.app.service.LabelInfoService;
import com.toki.web.app.mapper.LabelInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class LabelInfoServiceImpl extends ServiceImpl<LabelInfoMapper, LabelInfo>
    implements LabelInfoService{

}




