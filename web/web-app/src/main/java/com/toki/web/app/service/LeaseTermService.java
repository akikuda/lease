package com.toki.web.app.service;

import com.toki.model.entity.LeaseTerm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author toki
*/
public interface LeaseTermService extends IService<LeaseTerm> {
    List<LeaseTerm> listByRoomId(Long id);
}
