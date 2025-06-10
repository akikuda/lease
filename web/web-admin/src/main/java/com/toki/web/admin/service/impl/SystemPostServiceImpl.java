package com.toki.web.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.SystemPost;
import com.toki.web.admin.service.SystemPostService;
import com.toki.web.admin.mapper.SystemPostMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class SystemPostServiceImpl extends ServiceImpl<SystemPostMapper, SystemPost>
    implements SystemPostService{

}




