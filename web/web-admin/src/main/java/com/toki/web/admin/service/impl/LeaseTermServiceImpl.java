package com.toki.web.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.model.entity.LeaseTerm;
import com.toki.web.admin.service.LeaseTermService;
import com.toki.web.admin.mapper.LeaseTermMapper;
import org.springframework.stereotype.Service;

/**
* @author toki
* 针对表【lease_term(租期)】的数据库操作Service实现
*/
@Service
public class LeaseTermServiceImpl extends ServiceImpl<LeaseTermMapper, LeaseTerm>
    implements LeaseTermService{

}




