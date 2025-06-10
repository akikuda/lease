package com.toki.web.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.UserInfo;
import com.toki.web.admin.service.UserInfoService;
import com.toki.web.admin.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




