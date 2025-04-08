package com.toki.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.model.entity.GraphInfo;
import com.toki.model.enums.ItemType;
import com.toki.web.app.vo.graph.GraphVo;

import java.util.List;

/**
* @author toki
*/
public interface GraphInfoMapper extends BaseMapper<GraphInfo> {

    List<GraphVo> selectListByItemTypeAndId(ItemType itemType, Long id);
}




