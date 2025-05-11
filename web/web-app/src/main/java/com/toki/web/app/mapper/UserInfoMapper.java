package com.toki.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.model.entity.UserInfo;
import org.apache.ibatis.annotations.Select;

/**
 * @author toki
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    @Select("SELECT id FROM user_info WHERE phone = #{phone} and is_deleted = 0")
    Long getIdByPhone(String phone);
}




