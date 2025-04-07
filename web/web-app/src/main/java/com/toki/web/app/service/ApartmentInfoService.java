package com.toki.web.app.service;

import com.toki.model.entity.ApartmentInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.app.vo.apartment.ApartmentDetailVo;
import com.toki.web.app.vo.apartment.ApartmentItemVo;

/**
 * @author toki
 */
public interface ApartmentInfoService extends IService<ApartmentInfo> {
    ApartmentItemVo selectApartmentItemVoById(Long apartmentId);

    ApartmentDetailVo getApartmentDetailById(Long id);
}
