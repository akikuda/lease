package com.toki.web.admin.mapper;

import com.toki.model.entity.FacilityInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author toki
* 针对表【facility_info(配套信息表)】的数据库操作Mapper
*/
public interface FacilityInfoMapper extends BaseMapper<FacilityInfo> {

    List<FacilityInfo> selectListByApartmentId(Long id);
}




