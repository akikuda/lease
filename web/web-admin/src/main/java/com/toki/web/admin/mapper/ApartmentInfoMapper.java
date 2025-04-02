package com.toki.web.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.ApartmentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.web.admin.vo.apartment.ApartmentItemVo;
import com.toki.web.admin.vo.apartment.ApartmentQueryVo;

/**
 * @author toki
 * 针对表【apartment_info(公寓信息表)】的数据库操作Mapper
 */
public interface ApartmentInfoMapper extends BaseMapper<ApartmentInfo> {

    /**
     * 分页查询，虽涉及多张表，但结果为集合，更适合自定义sql
     * */
    IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo);
}




