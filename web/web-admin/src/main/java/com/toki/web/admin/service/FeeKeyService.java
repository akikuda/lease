package com.toki.web.admin.service;

import com.toki.common.result.Result;
import com.toki.model.entity.FeeKey;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.admin.vo.fee.FeeKeyVo;

import java.util.List;

/**
* @author toki
* 针对表【fee_key(杂项费用名称表)】的数据库操作Service
*/
public interface FeeKeyService extends IService<FeeKey> {

    Result<List<FeeKeyVo>> feeInfoList();
}
