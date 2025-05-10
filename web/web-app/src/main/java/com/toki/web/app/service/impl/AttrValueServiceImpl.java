package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.AttrValue;
import com.toki.web.app.service.AttrValueService;
import com.toki.web.app.mapper.AttrValueMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class AttrValueServiceImpl extends ServiceImpl<AttrValueMapper, AttrValue>
    implements AttrValueService{

}




