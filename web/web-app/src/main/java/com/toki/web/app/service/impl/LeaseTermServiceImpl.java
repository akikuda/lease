package com.toki.web.app.service.impl;

import com.toki.model.entity.LeaseTerm;
import com.toki.web.app.mapper.LeaseTermMapper;
import com.toki.web.app.service.LeaseTermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class LeaseTermServiceImpl extends ServiceImpl<LeaseTermMapper, LeaseTerm>
        implements LeaseTermService {

    private final LeaseTermMapper leaseTermMapper;

    @Override
    public List<LeaseTerm> listByRoomId(Long id) {
        return leaseTermMapper.selectListByRoomId(id);
    }
}




