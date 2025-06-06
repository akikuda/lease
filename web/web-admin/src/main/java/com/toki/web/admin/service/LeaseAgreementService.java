package com.toki.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.LeaseAgreement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.admin.vo.agreement.AgreementQueryVo;
import com.toki.web.admin.vo.agreement.AgreementVo;

/**
* @author toki
* 针对表【lease_agreement(租约信息表)】的数据库操作Service
*/
public interface LeaseAgreementService extends IService<LeaseAgreement> {

    AgreementVo getAgreementById(Long id);

    IPage<AgreementVo> pageAgreement(Page<AgreementVo> page, AgreementQueryVo queryVo);
}
