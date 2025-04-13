package com.toki.web.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.GroupBlogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.web.app.vo.group.GroupBlogVo;

/**
* @author toki
* @description 针对表【group_blog_info】的数据库操作Mapper
* @createDate 2025-04-12 15:51:13
* @Entity com.toki.model.entity.GroupBlogInfo
*/
public interface GroupBlogInfoMapper extends BaseMapper<GroupBlogInfo> {

    IPage<GroupBlogVo> pageItem(Page<GroupBlogVo> page);
}




