package com.toki.web.app.service.impl;

import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.toki.model.entity.*;
import com.toki.model.enums.ItemType;
import com.toki.model.enums.LeaseStatus;
import com.toki.web.app.mapper.*;
import com.toki.web.app.service.ApartmentInfoService;
import com.toki.web.app.service.GraphInfoService;
import com.toki.web.app.service.LeaseAgreementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.app.service.RoomInfoService;
import com.toki.web.app.vo.agreement.AgreementDetailVo;
import com.toki.web.app.vo.agreement.AgreementItemVo;
import com.toki.web.app.vo.graph.GraphVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    private final LeaseAgreementMapper leaseAgreementMapper;
    private final ApartmentInfoMapper apartmentInfoMapper;
    private final RoomInfoMapper roomInfoMapper;
    private final GraphInfoMapper graphInfoMapper;
    private final PaymentTypeMapper paymentTypeMapper;
    private final LeaseTermMapper leaseTermMapper;

    @Override
    public List<AgreementItemVo> listItemByPhone(String phone) {
        return leaseAgreementMapper.listItemByPhone(phone);
    }

    @Override
    public AgreementDetailVo getLeaseAgreementDetailById(Long id) {
        //1.查询租约信息
        LeaseAgreement leaseAgreement = leaseAgreementMapper.selectById(id);
        if (leaseAgreement == null) {
            return null;
        }
        //2.查询公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(leaseAgreement.getApartmentId());

        //3.查询房间信息
        RoomInfo roomInfo = roomInfoMapper.selectById(leaseAgreement.getRoomId());

        //4.查询图片信息
        List<GraphVo> roomGraphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM, leaseAgreement.getRoomId());
        List<GraphVo> apartmentGraphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.APARTMENT, leaseAgreement.getApartmentId());

        //5.查询支付方式
        PaymentType paymentType = paymentTypeMapper.selectById(leaseAgreement.getPaymentTypeId());

        //6.查询租期
        LeaseTerm leaseTerm = leaseTermMapper.selectById(leaseAgreement.getLeaseTermId());

        //封装数据
        AgreementDetailVo agreementDetailVo = CglibUtil.copy(leaseAgreement, AgreementDetailVo.class);

        agreementDetailVo.setApartmentName(apartmentInfo.getName());
        agreementDetailVo.setRoomNumber(roomInfo.getRoomNumber());
        agreementDetailVo.setApartmentGraphVoList(apartmentGraphVoList);
        agreementDetailVo.setRoomGraphVoList(roomGraphVoList);
        agreementDetailVo.setPaymentTypeName(paymentType.getName());
        agreementDetailVo.setLeaseTermMonthCount(leaseTerm.getMonthCount());
        agreementDetailVo.setLeaseTermUnit(leaseTerm.getUnit());

        return agreementDetailVo;
    }
}




