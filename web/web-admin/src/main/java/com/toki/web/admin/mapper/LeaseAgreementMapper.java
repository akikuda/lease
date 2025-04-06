package com.toki.web.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.LeaseAgreement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.web.admin.vo.agreement.AgreementQueryVo;
import com.toki.web.admin.vo.agreement.AgreementVo;

/**
 * @author toki
 * 针对表【lease_agreement(租约信息表)】的数据库操作Mapper
 */
public interface LeaseAgreementMapper extends BaseMapper<LeaseAgreement> {

    IPage<AgreementVo> pageAgreement(Page<AgreementVo> page, AgreementQueryVo queryVo);
}




