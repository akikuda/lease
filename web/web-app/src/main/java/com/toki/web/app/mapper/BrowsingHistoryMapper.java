package com.toki.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.BrowsingHistory;
import com.toki.web.app.vo.history.HistoryItemVo;

/**
* @author toki
*/
public interface BrowsingHistoryMapper extends BaseMapper<BrowsingHistory> {

    IPage<HistoryItemVo> pageItemByUserId(Page<HistoryItemVo> historyItemVoPage, Long userId);
}




