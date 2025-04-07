package com.toki.web.app.service;

import com.toki.model.entity.PaymentType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author toki
*/
public interface PaymentTypeService extends IService<PaymentType> {

    List<PaymentType> selectListByRoomId(Long id);
}
