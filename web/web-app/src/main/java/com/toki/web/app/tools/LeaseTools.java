package com.toki.web.app.tools;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.ApartmentInfo;
import com.toki.model.entity.RoomInfo;
import com.toki.model.entity.ViewAppointment;
import com.toki.model.enums.AppointmentStatus;
import com.toki.web.app.service.ApartmentInfoService;
import com.toki.web.app.service.RoomInfoService;
import com.toki.web.app.service.ViewAppointmentService;
import com.toki.web.app.vo.apartment.ApartmentDetailTool;
import com.toki.web.app.vo.apartment.ApartmentDetailVo;
import com.toki.web.app.vo.room.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author toki
 */
@RequiredArgsConstructor
@Component
public class LeaseTools {

    private final ViewAppointmentService viewAppointmentService;
    private final RoomInfoService roomInfoService;
    private final ApartmentInfoService apartmentInfoService;

    // 查询基本的房间信息列表
    @Tool(description = "根据除了公寓名称、房间ID之外的条件查询房间列表")
    public IPage<RoomItemVo> queryRoom(@ToolParam(required = false, description = "查询条件") RoomQueryVo queryRoom) {
        return roomInfoService.pageItem(new Page<>(0, Integer.MAX_VALUE), queryRoom);
    }

    @Tool(description = "查询公寓列表")
    public List<ApartmentInfo> queryApartment() {
        return apartmentInfoService.list(
                new LambdaQueryWrapper<>(ApartmentInfo.class)
                        .eq(ApartmentInfo::getIsRelease, 1)
                        .eq(ApartmentInfo::getIsDeleted, 0)
        );
    }

    @Tool(description = "根据公寓名称查询公寓基本信息")
    public ApartmentInfo queryApartmentByName(@ToolParam(description = "公寓名称") String name) {
        return apartmentInfoService.getOne(
                new LambdaQueryWrapper<>(ApartmentInfo.class)
                        .eq(ApartmentInfo::getName, name)
                        .eq(ApartmentInfo::getIsRelease, 1)
                        .eq(ApartmentInfo::getIsDeleted, 0)
        );
    }

    @Tool(description = "根据公寓ID查询公寓详细信息")
    public ApartmentDetailTool queryApartmentById(@ToolParam(description = "公寓ID") Long apartmentId) {
        final ApartmentDetailVo apartmentDetailVo = apartmentInfoService.getApartmentDetailById(apartmentId);
        return CglibUtil.copy(apartmentDetailVo, ApartmentDetailTool.class);
    }

    @Tool(description = "根据公寓名称查询房间,用于查询特定公寓下的房间信息列表")
    public IPage<RoomItemToolResult> queryRoomByApartmentName(@ToolParam(description = "公寓名称") String name) {
        final ApartmentInfo apartmentInfo = this.queryApartmentByName(name);
        if (apartmentInfo == null) {
            return new Page<>();
        }
        final Long apartmentId = apartmentInfo.getId();
        final IPage<RoomItemVo> roomItemVoList = roomInfoService
                .pageItemByApartmentId(new Page<>(0, Integer.MAX_VALUE), apartmentId);
        final List<RoomItemToolResult> resultList = CglibUtil.copyList(roomItemVoList.getRecords(), RoomItemToolResult::new);
        Page<RoomItemToolResult> resultPage = new Page<>();
        resultPage.setTotal(roomItemVoList.getTotal());
        resultPage.setRecords(resultList);
        resultPage.setCurrent(roomItemVoList.getCurrent());
        resultPage.setSize(roomItemVoList.getSize());
        return resultPage;
    }

    @Tool(description = "根据公寓名称和房间号查询房间详细信息")
    public RoomQueryToolResult queryRoomById(@ToolParam(description = "公寓名称") String apartmentName, @ToolParam(description = "房间号") String roomNumber ) {
        // 根据公寓名称查询公寓ID
        final ApartmentInfo apartmentInfo = this.queryApartmentByName(apartmentName);
        if (apartmentInfo == null) {
            return null;
        }
        final Long apartmentId = apartmentInfo.getId();
        // 根据房间号和公寓ID查询房间ID
        final RoomInfo roomInfo = roomInfoService.getOne(
                new LambdaQueryWrapper<>(RoomInfo.class)
                        .eq(RoomInfo::getRoomNumber, roomNumber)
                        .eq(RoomInfo::getApartmentId, apartmentId)
                        .eq(RoomInfo::getIsRelease, 1)
                        .eq(RoomInfo::getIsDeleted, 0)
        );
        // 根据房间ID查询房间详细信息
        final RoomDetailVo roomDetail = roomInfoService.getRoomDetailById(roomInfo.getId());

        return CglibUtil.copy(roomDetail, RoomQueryToolResult.class);
    }

    @Tool(description = "生成看房预约单,并返回预约单ID")
    public String generateViewAppointment(
            @ToolParam(description = "公寓名称") String apartmentName,
            @ToolParam(description = "预约时间，注意是北京时间") Date appointmentTime, // todo AI时间有误差
            @ToolParam(description = "用户姓名") String name,
            @ToolParam(description = "用户手机号") String phone,
            @ToolParam(description = "备注信息", required = false) String additionalInfo
    ) {
//        final Long userId = LoginUserHolder.getLoginUser().getUserId();
//        if (userId == null) {
//            throw new LeaseException(ResultCodeEnum.APP_LOGIN_AUTH);
//        }
        // 根据用户指定的公寓名称查询公寓ID
        final Long apartmentId = this.queryApartmentByName(apartmentName).getId();
        // 设置看房预约单属性
        final ViewAppointment viewAppointment = new ViewAppointment();
        viewAppointment.setApartmentId(apartmentId);
        viewAppointment.setAppointmentTime(appointmentTime);
//        viewAppointment.setUserId(userId);
        viewAppointment.setUserId(9L);
        viewAppointment.setName(name);
        viewAppointment.setPhone(phone);
        viewAppointment.setAdditionalInfo(additionalInfo);
        viewAppointment.setAppointmentStatus(AppointmentStatus.WAITING);
        // 保存
        final boolean isSuccess = viewAppointmentService.save(viewAppointment);
        if (!isSuccess) {
            return "预约失败!";
        }
        return String.valueOf(viewAppointment.getId());
    }

}
