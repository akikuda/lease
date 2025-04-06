package com.toki.web.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.SystemUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.web.admin.vo.system.user.SystemUserItemVo;
import com.toki.web.admin.vo.system.user.SystemUserQueryVo;

/**
* @author toki
* 针对表【system_user(员工信息表)】的数据库操作Mapper
*/
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    IPage<SystemUserItemVo> pageSystemUser(Page<SystemUser> page, SystemUserQueryVo queryVo);

    SystemUser selectOneByUsername(String username);
}




