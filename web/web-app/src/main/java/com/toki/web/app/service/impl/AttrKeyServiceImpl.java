package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.AttrKey;
import com.toki.web.app.service.AttrKeyService;
import com.toki.web.app.mapper.AttrKeyMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class AttrKeyServiceImpl extends ServiceImpl<AttrKeyMapper, AttrKey>
    implements AttrKeyService{

}




