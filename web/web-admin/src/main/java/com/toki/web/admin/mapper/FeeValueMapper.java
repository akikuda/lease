package com.toki.web.admin.mapper;

import com.toki.model.entity.FeeValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.web.admin.vo.fee.FeeValueVo;

import java.util.List;

/**
* @author toki
* 针对表【fee_value(杂项费用值表)】的数据库操作Mapper
*/
public interface FeeValueMapper extends BaseMapper<FeeValue> {

    List<FeeValueVo> selectListByApartmentId(Long id);
}




