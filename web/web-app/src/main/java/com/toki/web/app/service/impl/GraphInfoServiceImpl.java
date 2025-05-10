package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.GraphInfo;
import com.toki.web.app.service.GraphInfoService;
import com.toki.web.app.mapper.GraphInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class GraphInfoServiceImpl extends ServiceImpl<GraphInfoMapper, GraphInfo>
    implements GraphInfoService{

}




