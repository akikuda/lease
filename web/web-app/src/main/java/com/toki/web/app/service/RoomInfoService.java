package com.toki.web.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.RoomInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.app.vo.room.RoomDetailVo;
import com.toki.web.app.vo.room.RoomItemVo;
import com.toki.web.app.vo.room.RoomQueryVo;

/**
* @author toki
* 针对表【room_info(房间信息表)】的数据库操作Service
*/
public interface RoomInfoService extends IService<RoomInfo> {
    IPage<RoomItemVo> pageItem(Page<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo);

    IPage<RoomItemVo> pageItemByApartmentId(Page<RoomItemVo> roomItemVoPage, Long id);

    RoomDetailVo getRoomDetailById(Long id);
}
