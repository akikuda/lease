package com.toki.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.GroupBlogInfo;
import com.toki.web.app.vo.group.GroupBlogVo;

/**
* @author toki
*/
public interface GroupBlogInfoMapper extends BaseMapper<GroupBlogInfo> {

    // 分页查询，根据公寓ID查询相应公寓的博文，若公寓ID = -1，则表示查询公共博文
    IPage<GroupBlogVo> pageItem(Page<GroupBlogVo> page, Long apartmentId);

    // 分页查询，查询博文ID列表
//    List<Long> pageItemIdList(Page<Long> idPage, Long apartmentId);
}




