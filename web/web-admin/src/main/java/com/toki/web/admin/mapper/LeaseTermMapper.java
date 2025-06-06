package com.toki.web.admin.mapper;

import com.toki.model.entity.LeaseTerm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author toki
 * 针对表【lease_term(租期)】的数据库操作Mapper
 */
public interface LeaseTermMapper extends BaseMapper<LeaseTerm> {

    List<LeaseTerm> selectListByRoomId(Long id);
}




