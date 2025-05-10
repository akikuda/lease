package com.toki.web.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.common.constant.RedisConstant;
import com.toki.common.utils.LoginUserHolder;
import com.toki.model.entity.*;
import com.toki.model.enums.ItemType;
import com.toki.web.app.mapper.*;
import com.toki.web.app.service.ApartmentInfoService;
import com.toki.web.app.service.BrowsingHistoryService;
import com.toki.web.app.service.RoomInfoService;
import com.toki.web.app.vo.apartment.ApartmentItemVo;
import com.toki.web.app.vo.attr.AttrValueVo;
import com.toki.web.app.vo.fee.FeeValueVo;
import com.toki.web.app.vo.graph.GraphVo;
import com.toki.web.app.vo.room.RoomDetailVo;
import com.toki.web.app.vo.room.RoomItemVo;
import com.toki.web.app.vo.room.RoomQueryVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private final GraphInfoMapper graphInfoMapper;
    private final AttrValueMapper attrValueMapper;
    private final FacilityInfoMapper facilityInfoMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final PaymentTypeMapper paymentTypeMapper;
    private final LeaseTermMapper leaseTermMapper;
    private final FeeValueMapper feeValueMapper;
    private final ApartmentInfoService apartmentInfoService;
    private final BrowsingHistoryService browsingHistoryService;

    private final RedisTemplate<String, Object> redisTemplate;

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

        final String key = RedisConstant.APP_ROOM_PREFIX + id;
        // 查缓存
        RoomDetailVo roomDetailVo = (RoomDetailVo) redisTemplate.opsForValue().get(key);
        // 若缓存为空，则查询数据库并缓存
        if (BeanUtil.isEmpty(roomDetailVo)) {
            //1.查询房间信息
            RoomInfo roomInfo = roomInfoMapper.selectById(id);
            if (roomInfo == null) {
                // 缓存空值，防止缓存穿透
                redisTemplate.opsForValue().set(key, new RoomDetailVo(), RedisConstant.NULL_EXPIRE_TIME_SEC, TimeUnit.SECONDS);
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

            roomDetailVo = new RoomDetailVo();
            CglibUtil.copy(roomInfo, roomDetailVo);

            roomDetailVo.setApartmentItemVo(apartmentItemVo);
            roomDetailVo.setGraphVoList(graphVoList);
            roomDetailVo.setAttrValueVoList(attrValueVoList);
            roomDetailVo.setFacilityInfoList(facilityInfoList);
            roomDetailVo.setLabelInfoList(labelInfoList);
            roomDetailVo.setPaymentTypeList(paymentTypeList);
            roomDetailVo.setFeeValueVoList(feeValueVoList);
            roomDetailVo.setLeaseTermList(leaseTermList);

            // 存入缓存
            redisTemplate.opsForValue().set(key, roomDetailVo);
        }

        // 此方法主要功能为查询房间详情，故利用@Async注解，异步保存浏览记录
        browsingHistoryService.saveHistory(LoginUserHolder.getLoginUser().getUserId(), id);

        // 缓存命中直接返回
        return roomDetailVo;
    }
}




