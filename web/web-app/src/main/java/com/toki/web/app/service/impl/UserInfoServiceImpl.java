package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.common.exception.LeaseException;
import com.toki.common.utils.LoginUserHolder;
import com.toki.model.entity.UserInfo;
import com.toki.web.app.mapper.UserInfoMapper;
import com.toki.web.app.service.UserInfoService;
import com.toki.web.app.vo.user.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.toki.common.result.ResultCodeEnum.ADMIN_USER_NAME_EXIST_ERROR;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    @Override
    public Long getIdByPhone(String phone) {
        return userInfoMapper.getIdByPhone(phone);
    }
}




