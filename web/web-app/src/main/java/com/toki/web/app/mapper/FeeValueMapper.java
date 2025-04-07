package com.toki.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.model.entity.FeeValue;
import com.toki.web.app.vo.fee.FeeValueVo;

import java.util.List;

/**
* @author toki
* 针对表【fee_value(杂项费用值表)】的数据库操作Mapper
*/
public interface FeeValueMapper extends BaseMapper<FeeValue> {

    List<FeeValueVo> selectListByApartmentId(Long apartmentId);
}




