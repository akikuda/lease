package com.toki.web.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.SystemPost;
import com.toki.model.entity.SystemUser;
import com.toki.web.admin.mapper.SystemUserMapper;
import com.toki.web.admin.service.SystemPostService;
import com.toki.web.admin.service.SystemUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.admin.vo.system.user.SystemUserItemVo;
import com.toki.web.admin.vo.system.user.SystemUserQueryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author toki
 * 针对表【system_user(员工信息表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser>
        implements SystemUserService {

    private final SystemUserMapper systemUserMapper;
    private final SystemPostService systemPostService;

    @Override
    public IPage<SystemUserItemVo> pageSystemUser(Page<SystemUser> page, SystemUserQueryVo queryVo) {
        return systemUserMapper.pageSystemUser(page, queryVo);
    }

    @Override
    public SystemUserItemVo getSystemUserItemById(Long id) {
        final SystemUser systemUser = getById(id);
        final SystemPost post = systemPostService.getById(systemUser.getPostId());
        final SystemUserItemVo systemUserItemVo =
                CglibUtil.copy(systemUser, SystemUserItemVo.class);
        systemUserItemVo.setPostName(post.getName());
        return systemUserItemVo;
    }
}




