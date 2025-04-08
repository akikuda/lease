package com.toki.web.app.service;

import com.toki.model.entity.LeaseAgreement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.model.enums.LeaseStatus;
import com.toki.web.app.vo.agreement.AgreementDetailVo;
import com.toki.web.app.vo.agreement.AgreementItemVo;

import java.util.List;

/**
* @author toki
*/
public interface LeaseAgreementService extends IService<LeaseAgreement> {

    List<AgreementItemVo> listItemByPhone(String phone);

    AgreementDetailVo getLeaseAgreementDetailById(Long id);
}
