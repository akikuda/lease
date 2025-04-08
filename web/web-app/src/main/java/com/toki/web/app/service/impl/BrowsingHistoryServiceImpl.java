package com.toki.web.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.BrowsingHistory;
import com.toki.web.app.mapper.BrowsingHistoryMapper;
import com.toki.web.app.service.BrowsingHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.web.app.vo.history.HistoryItemVo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class BrowsingHistoryServiceImpl extends ServiceImpl<BrowsingHistoryMapper, BrowsingHistory>
        implements BrowsingHistoryService {

    private final BrowsingHistoryMapper browsingHistoryMapper;

    @Override
    public IPage<HistoryItemVo> pageItemByUserId(Page<HistoryItemVo> historyItemVoPage, Long userId) {
        return browsingHistoryMapper.pageItemByUserId(historyItemVoPage, userId);
    }

    @Override
    @Async
    public void saveHistory(Long userId, Long id) {
        // 只有首次浏览时才保存浏览记录，以后每次浏览都只更新浏览时间
        final BrowsingHistory browsingHistory = browsingHistoryMapper.selectOne(
                new LambdaQueryWrapper<>(BrowsingHistory.class)
                        .eq(BrowsingHistory::getUserId, userId)
                        .eq(BrowsingHistory::getRoomId, id)
        );
        if (BeanUtil.isEmpty(browsingHistory)){
            // 为空,则为首次浏览，保存记录
            final BrowsingHistory newHistory = new BrowsingHistory();
            newHistory.setUserId(userId);
            newHistory.setRoomId(id);
            newHistory.setBrowseTime(new Date());
            browsingHistoryMapper.insert(newHistory);
        }else {
            browsingHistory.setBrowseTime(new Date());
            browsingHistoryMapper.updateById(browsingHistory);
        }
    }
}