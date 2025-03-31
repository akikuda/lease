package com.toki.web.admin.mapper;

import com.toki.model.entity.AttrKey;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.web.admin.vo.attr.AttrKeyVo;

import java.util.List;

/**
* @author toki
* 针对表【attr_key(房间基本属性表)】的数据库操作Mapper
*/
public interface AttrKeyMapper extends BaseMapper<AttrKey> {

    List<AttrKeyVo> listAttrInfo();
}




