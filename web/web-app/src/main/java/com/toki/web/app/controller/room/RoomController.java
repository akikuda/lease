package com.toki.web.app.controller.room;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.result.Result;
import com.toki.web.app.service.RoomInfoService;
import com.toki.web.app.vo.room.RoomDetailVo;
import com.toki.web.app.vo.room.RoomItemVo;
import com.toki.web.app.vo.room.RoomQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toki
 */
@Tag(name = "房间信息")
@RestController
@RequestMapping("/app/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomInfoService roomInfoService;

    @Operation(summary = "分页查询房间列表")
    @GetMapping("pageItem")
    public Result<IPage<RoomItemVo>> pageItem(@RequestParam long current, @RequestParam long size, RoomQueryVo queryVo) {
        return Result.ok(roomInfoService.pageItem(new Page<RoomItemVo>(current, size), queryVo));
    }

    @Operation(summary = "根据id获取房间的详细信息")
    @GetMapping("getDetailById")
    public Result<RoomDetailVo> getDetailById(@RequestParam Long id) {
        return Result.ok(roomInfoService.getRoomDetailById(id));
    }

    @Operation(summary = "根据公寓id分页查询房间列表")
    @GetMapping("pageItemByApartmentId")
    public Result<IPage<RoomItemVo>> pageItemByApartmentId(@RequestParam long current, @RequestParam long size, @RequestParam Long id) {
        return Result.ok(roomInfoService.pageItemByApartmentId(new Page<RoomItemVo>(current, size), id));
    }
}
