package com.toki.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.result.Result;
import com.toki.model.entity.RoomInfo;
import com.toki.model.enums.ReleaseStatus;
import com.toki.web.admin.service.RoomInfoService;
import com.toki.web.admin.vo.room.RoomDetailVo;
import com.toki.web.admin.vo.room.RoomItemVo;
import com.toki.web.admin.vo.room.RoomQueryVo;
import com.toki.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author toki
 */
@Tag(name = "房间信息管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/room")
public class RoomController {

    private final RoomInfoService InfoService;

    @Operation(summary = "保存或更新房间信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody RoomSubmitVo roomSubmitVo) {
        InfoService.saveOrUpdateRoom(roomSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询房间列表")
    @GetMapping("pageItem")
    public Result<IPage<RoomItemVo>> pageItem(@RequestParam long current, @RequestParam long size, RoomQueryVo queryVo) {
        final Page<RoomItemVo> page = new Page<>(current, size);
        return Result.ok(InfoService.pageItem(page, queryVo));
    }

    @Operation(summary = "根据id获取房间详细信息")
    @GetMapping("getDetailById")
    public Result<RoomDetailVo> getDetailById(@RequestParam Long id) {
        return Result.ok(InfoService.getRoomDetailById(id));
    }

    @Operation(summary = "根据id删除房间信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        InfoService.removeRoomById(id);
        return Result.ok();
    }

    @Operation(summary = "根据id修改房间发布状态")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(Long id, ReleaseStatus status) {
        InfoService.update(
                new LambdaUpdateWrapper<>(RoomInfo.class)
                        .eq(RoomInfo::getId, id)
                        .set(RoomInfo::getIsRelease, status)
        );
        return Result.ok();
    }

    @Operation(summary = "根据公寓id查询房间列表")
    @GetMapping("listBasicByApartmentId")
    public Result<List<RoomInfo>> listBasicByApartmentId(Long id) {
        final List<RoomInfo> roomList = InfoService.list(
                new LambdaQueryWrapper<>(RoomInfo.class)
                        .eq(RoomInfo::getApartmentId, id)
                        .eq(RoomInfo::getIsRelease, ReleaseStatus.RELEASED)
        );
        return Result.ok(roomList);
    }

}


















