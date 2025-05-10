package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.FeeValue;
import com.toki.web.app.service.FeeValueService;
import com.toki.web.app.mapper.FeeValueMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class FeeValueServiceImpl extends ServiceImpl<FeeValueMapper, FeeValue>
    implements FeeValueService{

}




