package com.toki.web.admin.service;

import com.toki.common.result.Result;
import com.toki.model.entity.AttrKey;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.admin.vo.attr.AttrKeyVo;

import java.util.List;

/**
* @author toki
* 针对表【attr_key(房间基本属性表)】的数据库操作Service
*/
public interface AttrKeyService extends IService<AttrKey> {

    Result<List<AttrKeyVo>> listAttrInfo();

}
