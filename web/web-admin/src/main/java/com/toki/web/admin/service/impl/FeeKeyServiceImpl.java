package com.toki.web.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.toki.common.result.Result;
import com.toki.model.entity.FeeKey;
import com.toki.web.admin.mapper.FeeKeyMapper;
import com.toki.web.admin.service.FeeKeyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.admin.vo.fee.FeeKeyVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* @author toki
* 针对表【fee_key(杂项费用名称表)】的数据库操作Service实现
*/
@Service
@RequiredArgsConstructor
public class FeeKeyServiceImpl extends ServiceImpl<FeeKeyMapper, FeeKey>
    implements FeeKeyService{

    private final FeeKeyMapper feeKeyMapper;

    @Override
    public Result<List<FeeKeyVo>> feeInfoList() {
        List<FeeKeyVo> feeKeyVoList = feeKeyMapper.feeInfoList();
        if (CollUtil.isEmpty(feeKeyVoList)) {
            return Result.ok(Collections.emptyList());
        }
        return Result.ok(feeKeyVoList);
    }
}




