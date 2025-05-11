package com.toki.web.app.service;

import com.toki.model.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.app.vo.user.UserInfoVo;
import org.apache.ibatis.annotations.Select;

/**
 * @author toki
 */
public interface UserInfoService extends IService<UserInfo> {

    Long getIdByPhone(String phone);
}