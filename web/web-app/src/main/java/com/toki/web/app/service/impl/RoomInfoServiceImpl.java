package com.toki.web.app.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.*;
import com.toki.model.enums.ItemType;
import com.toki.web.app.mapper.*;
import com.toki.web.app.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.app.vo.apartment.ApartmentItemVo;
import com.toki.web.app.vo.attr.AttrValueVo;
import com.toki.web.app.vo.fee.FeeValueVo;
import com.toki.web.app.vo.graph.GraphVo;
import com.toki.web.app.vo.room.RoomDetailVo;
import com.toki.web.app.vo.room.RoomItemVo;
import com.toki.web.app.vo.room.RoomQueryVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author toki
 * 针对表【room_info(房间信息表)】的数据库操作Service实现
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {

    private final RoomInfoMapper roomInfoMapper;
    private final ApartmentInfoMapper apartmentInfoMapper;
    private final GraphInfoMapper graphInfoMapper;
    private final AttrValueMapper attrValueMapper;
    private final FacilityInfoMapper facilityInfoMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final PaymentTypeMapper paymentTypeMapper;
    private final LeaseTermMapper leaseTermMapper;
    private final FeeValueMapper feeValueMapper;
    private final ApartmentInfoService apartmentInfoService;

    @Override
    public IPage<RoomItemVo> pageItem(Page<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo) {
        return roomInfoMapper.pageItem(roomItemVoPage, queryVo);
    }

    @Override
    public IPage<RoomItemVo> pageItemByApartmentId(Page<RoomItemVo> roomItemVoPage, Long id) {
        return roomInfoMapper.pageItemByApartmentId(roomItemVoPage, id);
    }

    @Override
    public RoomDetailVo getRoomDetailById(Long id) {
        //1.查询房间信息
        RoomInfo roomInfo = roomInfoMapper.selectById(id);
        if (roomInfo == null) {
            return null;
        }
        //2.查询图片
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM, id);
        //3.查询租期
        List<LeaseTerm> leaseTermList = leaseTermMapper.selectListByRoomId(id);
        //4.查询配套
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByRoomId(id);
        //5.查询标签
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByRoomId(id);
        //6.查询支付方式
        List<PaymentType> paymentTypeList = paymentTypeMapper.selectListByRoomId(id);
        //7.查询基本属性
        List<AttrValueVo> attrValueVoList = attrValueMapper.selectListByRoomId(id);
        //8.查询杂费信息
        List<FeeValueVo> feeValueVoList = feeValueMapper.selectListByApartmentId(roomInfo.getApartmentId());
        //9.查询公寓信息
        ApartmentItemVo apartmentItemVo = apartmentInfoService.selectApartmentItemVoById(roomInfo.getApartmentId());

        RoomDetailVo roomDetailVo = new RoomDetailVo();
        CglibUtil.copy(roomInfo, roomDetailVo);

        roomDetailVo.setApartmentItemVo(apartmentItemVo);
        roomDetailVo.setGraphVoList(graphVoList);
        roomDetailVo.setAttrValueVoList(attrValueVoList);
        roomDetailVo.setFacilityInfoList(facilityInfoList);
        roomDetailVo.setLabelInfoList(labelInfoList);
        roomDetailVo.setPaymentTypeList(paymentTypeList);
        roomDetailVo.setFeeValueVoList(feeValueVoList);
        roomDetailVo.setLeaseTermList(leaseTermList);

        return roomDetailVo;
    }
}




