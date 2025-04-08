package com.toki.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.model.entity.LeaseAgreement;
import com.toki.web.app.vo.agreement.AgreementItemVo;

import java.util.List;

/**
* @author toki
*/
public interface LeaseAgreementMapper extends BaseMapper<LeaseAgreement> {

    List<AgreementItemVo> listItemByPhone(String phone);
}




