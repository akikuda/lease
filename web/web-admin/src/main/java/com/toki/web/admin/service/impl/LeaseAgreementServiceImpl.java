package com.toki.web.admin.service.impl;

import com.toki.common.exception.LeaseException;
import com.toki.common.result.ResultCodeEnum;
import com.toki.model.entity.*;
import com.toki.web.admin.mapper.LeaseAgreementMapper;
import com.toki.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.admin.vo.agreement.AgreementVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author toki
 * 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    private final ApartmentInfoService apartmentInfoService;
    private final RoomInfoService roomInfoService;
    private final PaymentTypeService paymentTypeService;
    private final LeaseTermService leaseTermService;

    @Override
    public AgreementVo getAgreementById(Long id) {

        final LeaseAgreement leaseAgreement = getById(id);
        if (leaseAgreement == null) {
            throw new LeaseException(ResultCodeEnum.LEASE_AGREEMENT_NOT_EXIST_ERROR);
        }

        final ApartmentInfo apartmentInfo = apartmentInfoService.getById(leaseAgreement.getApartmentId());
        final RoomInfo roomInfo = roomInfoService.getById(leaseAgreement.getRoomId());
        final PaymentType paymentType = paymentTypeService.getById(leaseAgreement.getPaymentTypeId());
        final LeaseTerm leaseTerm = leaseTermService.getById(leaseAgreement.getLeaseTermId());

        return AgreementVo.builder()
                .apartmentInfo(apartmentInfo)
                .roomInfo(roomInfo)
                .paymentType(paymentType)
                .leaseTerm(leaseTerm)
                .build();
    }
}




