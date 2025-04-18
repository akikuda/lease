package com.toki.web.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.toki.common.constant.RedisConstant;
import com.toki.common.exception.LeaseException;
import com.toki.common.result.ResultCodeEnum;
import com.toki.common.utils.JwtUtil;
import com.toki.model.entity.UserInfo;
import com.toki.model.enums.BaseStatus;
import com.toki.web.app.mapper.UserInfoMapper;
import com.toki.web.app.service.LoginService;
import com.toki.web.app.service.SmsService;
import com.toki.web.app.vo.user.LoginVo;
import com.toki.web.app.vo.user.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final SmsService smsService;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserInfoMapper userInfoMapper;

    @Override
    public void getSMSCode(String phone) {
        // 判断手机号是否合法
        if (StrUtil.isBlank(phone)) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_PHONE_EMPTY);
        }
        // 判断是否频繁发送验证码
        final String key = RedisConstant.APP_LOGIN_PREFIX + phone;
        if (stringRedisTemplate.hasKey(key)) {
            // 检查验证码已存在时间
            final long ttl = RedisConstant.APP_LOGIN_CODE_TTL_SEC - stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
            // 若存在时间小于1分钟
            if (ttl < RedisConstant.APP_LOGIN_CODE_RESEND_TIME_SEC) {
                throw new LeaseException(ResultCodeEnum.APP_SEND_SMS_TOO_OFTEN);
            }
        }
        // 生成随机验证码
        final String code = RandomUtil.randomNumbers(6);
        // 发送验证码
        smsService.sendCode(phone, code);
        // 存入redis, 有效期10分钟
        stringRedisTemplate.opsForValue().set(key, code, RedisConstant.APP_LOGIN_CODE_TTL_SEC, TimeUnit.SECONDS);
    }

    @Override
    public String login(LoginVo loginVo) {
        // 判断手机号是否合法
        final String phone = loginVo.getPhone();
        if (StrUtil.isBlank(phone)) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_PHONE_EMPTY);
        }
        // 判断验证码是否合法
        if (StrUtil.isBlank(loginVo.getCode())) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_EMPTY);
        }
        final String key = RedisConstant.APP_LOGIN_PREFIX + phone;
        String code = stringRedisTemplate.opsForValue().get(key);
        // 判断验证码是否过期
        if (StrUtil.isBlank(code)) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_EXPIRED);
        }
        // 判断验证码是否正确
        if (!code.equals(loginVo.getCode())) {
            throw new LeaseException(ResultCodeEnum.APP_LOGIN_CODE_ERROR);
        }

        UserInfo userInfo = userInfoMapper.selectOne(
                new LambdaQueryWrapper<>(UserInfo.class)
                        .eq(UserInfo::getPhone, phone)
        );
        // 判断用户是否存在
        if (BeanUtil.isEmpty(userInfo)) {
            // 不存在，自动注册
            userInfo = new UserInfo();
            userInfo.setPhone(phone);
            userInfo.setNickname("用户-" + phone.substring(7) + "-" + UUID.fastUUID().toString().substring(0, 4));
            userInfoMapper.insert(userInfo);
        } else {
            // 存在，判断是否被禁用
            if (userInfo.getStatus() == BaseStatus.DISABLE) {
                throw new LeaseException(ResultCodeEnum.APP_ACCOUNT_DISABLED_ERROR);
            }
        }

        return JwtUtil.createToken(userInfo.getId(), userInfo.getPhone());
    }

    @Override
    public UserInfoVo getLoginUserInfoById(Long userId) {
        final UserInfo userInfo = userInfoMapper.selectById(userId);
        // 在JDK9及以上，需要添加启动命令--add-opens java.base/java.lang=ALL-UNNAMED，以允许CGLIB反射
        return CglibUtil.copy(userInfo, UserInfoVo.class);
    }
}
