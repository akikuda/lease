package com.toki.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.ApartmentInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.admin.vo.apartment.ApartmentItemVo;
import com.toki.web.admin.vo.apartment.ApartmentQueryVo;
import com.toki.web.admin.vo.apartment.ApartmentSubmitVo;

/**
 * @author toki
 * 针对表【apartment_info(公寓信息表)】的数据库操作Service
 */
public interface ApartmentInfoService extends IService<ApartmentInfo> {

    void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo);

    IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo);
}
