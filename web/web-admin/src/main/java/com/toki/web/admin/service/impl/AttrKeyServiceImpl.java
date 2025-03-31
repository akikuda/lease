package com.toki.web.admin.service.impl;

import com.toki.common.result.Result;
import com.toki.model.entity.AttrKey;
import com.toki.web.admin.mapper.AttrKeyMapper;
import com.toki.web.admin.service.AttrKeyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.admin.vo.attr.AttrKeyVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author toki
 * 针对表【attr_key(房间基本属性表)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class AttrKeyServiceImpl extends ServiceImpl<AttrKeyMapper, AttrKey>
        implements AttrKeyService {

    private final AttrKeyMapper attrKeyMapper;

    @Override
    public Result<List<AttrKeyVo>> listAttrInfo() {
        List<AttrKeyVo> attrKeyVoList = attrKeyMapper.listAttrInfo();
        if ( attrKeyVoList == null ){
            return Result.fail();
        }
        return Result.ok(attrKeyVoList);
    }
}




