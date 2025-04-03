package com.toki.web.admin.mapper;

import com.toki.model.entity.PaymentType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author toki
 * 针对表【payment_type(支付方式表)】的数据库操作Mapper
 */
public interface PaymentTypeMapper extends BaseMapper<PaymentType> {

    List<PaymentType> selectListByRoomId(Long id);
}




