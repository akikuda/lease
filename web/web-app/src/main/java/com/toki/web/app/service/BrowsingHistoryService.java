package com.toki.web.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.BrowsingHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.app.vo.history.HistoryItemVo;

/**
* @author toki
*/
public interface BrowsingHistoryService extends IService<BrowsingHistory> {
    IPage<HistoryItemVo> pageItemByUserId(Page<HistoryItemVo> historyItemVoPage, Long userId);

    void saveHistory(Long userId, Long id);
}
