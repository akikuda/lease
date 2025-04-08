package com.toki.web.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.common.constant.RedisConstant;
import com.toki.model.entity.*;
import com.toki.model.enums.ItemType;
import com.toki.web.admin.mapper.*;
import com.toki.web.admin.service.*;
import com.toki.web.admin.vo.attr.AttrValueVo;
import com.toki.web.admin.vo.graph.GraphVo;
import com.toki.web.admin.vo.room.RoomDetailVo;
import com.toki.web.admin.vo.room.RoomItemVo;
import com.toki.web.admin.vo.room.RoomQueryVo;
import com.toki.web.admin.vo.room.RoomSubmitVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author toki
 * 针对表【room_info(房间信息表)】的数据库操作Service实现 <br>
 * 在对房间数据进行操作时，注意缓存和数据库的一致性
 */
@Service
@RequiredArgsConstructor
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {

    private final GraphInfoService graphInfoService;
    private final RoomAttrValueService roomAttrValueService;
    private final RoomFacilityService roomFacilityService;
    private final RoomLabelService roomLabelService;
    private final RoomPaymentTypeService roomPaymentTypeService;
    private final RoomLeaseTermService roomLeaseTermService;

    private final RoomInfoMapper roomInfoMapper;
    private final ApartmentInfoMapper apartmentInfoMapper;
    private final GraphInfoMapper graphInfoMapper;
    private final AttrValueMapper attrValueMapper;
    private final FacilityInfoMapper facilityInfoMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final PaymentTypeMapper paymentTypeMapper;
    private final LeaseTermMapper leaseTermMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo) {
        final Long roomId = roomSubmitVo.getId();
        final boolean isUpdate = roomId != null;
        super.saveOrUpdate(roomSubmitVo);

        if (isUpdate) {
            // 更新，删除原有关联数据和缓存
            graphInfoService.remove(
                    new LambdaQueryWrapper<>(GraphInfo.class)
                            .eq(GraphInfo::getItemType, ItemType.ROOM)
                            .eq(GraphInfo::getItemId, roomId)
            );

            roomAttrValueService.remove(
                    new LambdaQueryWrapper<>(RoomAttrValue.class)
                            .eq(RoomAttrValue::getRoomId, roomId)
            );

            roomFacilityService.remove(
                    new LambdaQueryWrapper<>(RoomFacility.class)
                            .eq(RoomFacility::getRoomId, roomId)
            );

            roomLabelService.remove(
                    new LambdaQueryWrapper<>(RoomLabel.class)
                            .eq(RoomLabel::getRoomId, roomId)
            );

            roomPaymentTypeService.remove(
                    new LambdaQueryWrapper<>(RoomPaymentType.class)
                            .eq(RoomPaymentType::getRoomId, roomId)
            );

            roomLeaseTermService.remove(
                    new LambdaQueryWrapper<>(RoomLeaseTerm.class)
                            .eq(RoomLeaseTerm::getRoomId, roomId)
            );

            // 删除缓存
            redisTemplate.delete(RedisConstant.APP_ROOM_PREFIX + roomId);
        }

        // 保存关联数据
        final List<GraphVo> graphVoList = roomSubmitVo.getGraphVoList();
        if (!CollUtil.isEmpty(graphVoList)) {
            final ArrayList<GraphInfo> graphInfoList = new ArrayList<>();
            for (GraphVo graphVo : graphVoList) {
                final GraphInfo graphInfo = CglibUtil.copy(graphVo, GraphInfo.class);
                graphInfo.setItemType(ItemType.ROOM);
                graphInfo.setItemId(roomId);
                graphInfoList.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfoList);
        }

        final List<Long> facilityInfoIds = roomSubmitVo.getFacilityInfoIds();
        if (!CollUtil.isEmpty(facilityInfoIds)) {
            final ArrayList<RoomFacility> roomFacilityList = new ArrayList<>();
            for (Long id : facilityInfoIds) {
                roomFacilityList.add(
                        RoomFacility.builder()
                                .roomId(roomId)
                                .facilityId(id)
                                .build()
                );
            }
            roomFacilityService.saveBatch(roomFacilityList);
        }

        final List<Long> labelInfoIds = roomSubmitVo.getLabelInfoIds();
        if (!CollUtil.isEmpty(labelInfoIds)) {
            final ArrayList<RoomLabel> roomLabels = new ArrayList<>();
            for (Long id : labelInfoIds) {
                roomLabels.add(
                        RoomLabel.builder()
                                .roomId(roomId)
                                .labelId(id)
                                .build()
                );
            }
            roomLabelService.saveBatch(roomLabels);
        }

        List<Long> paymentTypeIds = roomSubmitVo.getPaymentTypeIds();
        if (!CollUtil.isEmpty(paymentTypeIds)) {
            ArrayList<RoomPaymentType> roomPaymentTypeList = new ArrayList<>();
            for (Long id : paymentTypeIds) {
                roomPaymentTypeList.add(
                        RoomPaymentType.builder()
                                .roomId(roomId)
                                .paymentTypeId(id)
                                .build()
                );
            }
            roomPaymentTypeService.saveBatch(roomPaymentTypeList);
        }

        List<Long> attrValueIds = roomSubmitVo.getAttrValueIds();
        if (!CollUtil.isEmpty(attrValueIds)) {
            List<RoomAttrValue> roomAttrValueList = new ArrayList<>();
            for (Long id : attrValueIds) {
                roomAttrValueList.add(
                        RoomAttrValue.builder()
                                .roomId(roomId)
                                .attrValueId(id)
                                .build()
                );
            }
            roomAttrValueService.saveBatch(roomAttrValueList);
        }

        List<Long> leaseTermIds = roomSubmitVo.getLeaseTermIds();
        if (!CollUtil.isEmpty(leaseTermIds)) {
            ArrayList<RoomLeaseTerm> roomLeaseTerms = new ArrayList<>();
            for (Long id : leaseTermIds) {
                roomLeaseTerms.add(
                        RoomLeaseTerm.builder()
                                .roomId(roomId)
                                .leaseTermId(id)
                                .build()
                );
            }
            roomLeaseTermService.saveBatch(roomLeaseTerms);
        }
    }

    @Override
    public IPage<RoomItemVo> pageItem(Page<RoomItemVo> page, RoomQueryVo queryVo) {
        return roomInfoMapper.pageItem(page, queryVo);
    }

    @Override
    public RoomDetailVo getRoomDetailById(Long id) {

        //1.查询RoomInfo
        RoomInfo roomInfo = getById(id);
        if (BeanUtil.isEmpty(roomInfo)) {
            return null;
        }

        //2.查询所属公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(roomInfo.getApartmentId());

        //3.查询graphInfoList
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM, id);

        //4.查询attrValueList
        List<AttrValueVo> attrvalueVoList = attrValueMapper.selectListByRoomId(id);

        //5.查询facilityInfoList
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByRoomId(id);

        //6.查询labelInfoList
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByRoomId(id);

        //7.查询paymentTypeList
        List<PaymentType> paymentTypeList = paymentTypeMapper.selectListByRoomId(id);

        //8.查询leaseTermList
        List<LeaseTerm> leaseTermList = leaseTermMapper.selectListByRoomId(id);


        // 组装结果
        RoomDetailVo roomDetailVo = CglibUtil.copy(roomInfo, RoomDetailVo.class);

        roomDetailVo.setApartmentInfo(apartmentInfo);
        roomDetailVo.setGraphVoList(graphVoList);
        roomDetailVo.setAttrValueVoList(attrvalueVoList);
        roomDetailVo.setFacilityInfoList(facilityInfoList);
        roomDetailVo.setLabelInfoList(labelInfoList);
        roomDetailVo.setPaymentTypeList(paymentTypeList);
        roomDetailVo.setLeaseTermList(leaseTermList);

        return roomDetailVo;
    }

    @Override
    public void removeRoomById(Long id) {
        //1.删除RoomInfo
        super.removeById(id);

        //2.删除graphInfoList
        LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<>();
        graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
        graphQueryWrapper.eq(GraphInfo::getItemId, id);
        graphInfoService.remove(graphQueryWrapper);

        //3.删除attrValueList
        LambdaQueryWrapper<RoomAttrValue> attrQueryWrapper = new LambdaQueryWrapper<>();
        attrQueryWrapper.eq(RoomAttrValue::getRoomId, id);
        roomAttrValueService.remove(attrQueryWrapper);

        //4.删除facilityInfoList
        LambdaQueryWrapper<RoomFacility> facilityQueryWrapper = new LambdaQueryWrapper<>();
        facilityQueryWrapper.eq(RoomFacility::getRoomId, id);
        roomFacilityService.remove(facilityQueryWrapper);

        //5.删除labelInfoList
        LambdaQueryWrapper<RoomLabel> labelQueryWrapper = new LambdaQueryWrapper<>();
        labelQueryWrapper.eq(RoomLabel::getRoomId, id);
        roomLabelService.remove(labelQueryWrapper);

        //6.删除paymentTypeList
        LambdaQueryWrapper<RoomPaymentType> paymentQueryWrapper = new LambdaQueryWrapper<>();
        paymentQueryWrapper.eq(RoomPaymentType::getRoomId, id);
        roomPaymentTypeService.remove(paymentQueryWrapper);

        //7.删除leaseTermList
        LambdaQueryWrapper<RoomLeaseTerm> termQueryWrapper = new LambdaQueryWrapper<>();
        termQueryWrapper.eq(RoomLeaseTerm::getRoomId, id);
        roomLeaseTermService.remove(termQueryWrapper);

        //8.删除缓存
        redisTemplate.delete(RedisConstant.APP_ROOM_PREFIX + id);
    }

}




