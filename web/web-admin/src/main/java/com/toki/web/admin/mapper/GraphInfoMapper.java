package com.toki.web.admin.mapper;

import com.toki.model.entity.GraphInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.model.enums.ItemType;
import com.toki.web.admin.vo.graph.GraphVo;

import java.util.List;

/**
* @author toki
* 针对表【graph_info(图片信息表)】的数据库操作Mapper
*/
public interface GraphInfoMapper extends BaseMapper<GraphInfo> {


    List<GraphVo> selectListByItemTypeAndId(ItemType itemType, Long id);
}




