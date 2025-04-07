package com.toki.web.admin.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.toki.common.exception.LeaseException;
import com.toki.common.result.ResultCodeEnum;
import com.toki.common.utils.JwtUtil;
import com.toki.model.entity.SystemUser;
import com.toki.model.enums.BaseStatus;
import com.toki.web.admin.mapper.SystemUserMapper;
import com.toki.web.admin.service.LoginService;
import com.toki.web.admin.vo.login.CaptchaVo;
import com.toki.web.admin.vo.login.LoginVo;
import com.toki.web.admin.vo.system.user.SystemUserInfoVo;
import com.wf.captcha.SpecCaptcha;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.toki.common.constant.RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC;
import static com.toki.common.constant.RedisConstant.ADMIN_LOGIN_PREFIX;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final StringRedisTemplate stringRedisTemplate;
    private final SystemUserMapper systemUserMapper;

    @Override
    public CaptchaVo getCaptcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        // text() 获取验证码，又因为用户不用区分大小写，在这里统一转为小写
        final String code = specCaptcha.text().toLowerCase();
        final String key = ADMIN_LOGIN_PREFIX + UUID.fastUUID();
        // 将验证码存入redis，并设置过期时间
        stringRedisTemplate.opsForValue().set(key, code, ADMIN_LOGIN_CAPTCHA_TTL_SEC, TimeUnit.SECONDS);
        return new CaptchaVo(specCaptcha.toBase64(), key);

    }

    @Override
    public String login(LoginVo loginVo) {

        // 校验验证码
        // 1. 判断是否输入了验证码
        if (StrUtil.isBlank(loginVo.getCaptchaCode())) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }
        // 2. 校验验证码是否过期
        String code = stringRedisTemplate.opsForValue().get(loginVo.getCaptchaKey());
        if (StrUtil.isBlank(code)) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED);
        }
        // 3. 校验验证码是否正确
        final String userCaptchaCode = loginVo.getCaptchaCode().toLowerCase();
        if (!code.equals(userCaptchaCode)) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }

        // 校验用户
        // 1. 用户是否存在
        SystemUser systemUser = systemUserMapper.selectOneByUsername(loginVo.getUsername());
        if (systemUser == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }
        // 2. 用户状态
        if (systemUser.getStatus() == BaseStatus.DISABLE) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
        }
        // 3. 校验密码
        if (!systemUser.getPassword().equals(DigestUtil.md5Hex(loginVo.getPassword()))) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }

        // 登录成功，生成token并返回
        return JwtUtil.createToken(systemUser.getId(), systemUser.getUsername());
    }

    @Override
    public SystemUserInfoVo getLoginUserInfoById(Long userId) {
        final SystemUser systemUser = systemUserMapper.selectById(userId);
        return SystemUserInfoVo.builder()
                .name(systemUser.getName())
                .avatarUrl(systemUser.getAvatarUrl())
                .build();
    }
}
