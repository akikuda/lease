package com.toki.web.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.RoomInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.web.admin.vo.room.RoomItemVo;
import com.toki.web.admin.vo.room.RoomQueryVo;

/**
* @author toki
* 针对表【room_info(房间信息表)】的数据库操作Mapper
*/
public interface RoomInfoMapper extends BaseMapper<RoomInfo> {

    IPage<RoomItemVo> pageItem(Page<RoomItemVo> page, RoomQueryVo queryVo);
}




