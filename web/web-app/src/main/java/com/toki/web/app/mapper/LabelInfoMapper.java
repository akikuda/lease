package com.toki.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.model.entity.LabelInfo;

import java.util.List;

/**
* @author toki
* 针对表【label_info(标签信息表)】的数据库操作Mapper
*/
public interface LabelInfoMapper extends BaseMapper<LabelInfo> {

    List<LabelInfo> selectListByRoomId(Long id);

    List<LabelInfo> selectListByApartmentId(Long apartmentId);
}




