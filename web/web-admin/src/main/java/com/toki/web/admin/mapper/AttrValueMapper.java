package com.toki.web.admin.mapper;

import com.toki.model.entity.AttrValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.web.admin.vo.attr.AttrValueVo;

import java.util.List;

/**
* @author toki
* 针对表【attr_value(房间基本属性值表)】的数据库操作Mapper
*/
public interface AttrValueMapper extends BaseMapper<AttrValue> {

    List<AttrValueVo> selectListByRoomId(Long id);
}




