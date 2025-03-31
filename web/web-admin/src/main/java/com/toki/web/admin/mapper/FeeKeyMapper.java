package com.toki.web.admin.mapper;

import com.toki.model.entity.FeeKey;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.web.admin.vo.fee.FeeKeyVo;

import java.util.List;

/**
* @author toki
* 针对表【fee_key(杂项费用名称表)】的数据库操作Mapper
*/
public interface FeeKeyMapper extends BaseMapper<FeeKey> {

    List<FeeKeyVo> feeInfoList();
}




