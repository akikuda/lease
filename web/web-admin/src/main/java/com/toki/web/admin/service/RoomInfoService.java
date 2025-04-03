package com.toki.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.RoomInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.admin.vo.room.RoomDetailVo;
import com.toki.web.admin.vo.room.RoomItemVo;
import com.toki.web.admin.vo.room.RoomQueryVo;
import com.toki.web.admin.vo.room.RoomSubmitVo;

/**
* @author toki
* 针对表【room_info(房间信息表)】的数据库操作Service
*/
public interface RoomInfoService extends IService<RoomInfo> {

    void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo);

    IPage<RoomItemVo> pageItem(Page<RoomItemVo> page, RoomQueryVo queryVo);

    RoomDetailVo getRoomDetailById(Long id);

    void removeRoomById(Long id);
}
