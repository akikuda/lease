package com.toki.web.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.GroupBlogInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.app.vo.group.GroupBlogVo;

/**
* @author toki
*/
public interface GroupBlogInfoService extends IService<GroupBlogInfo> {

    /**
     * 发布博客,通过feed流的推模式,推送到粉丝收件箱(redis)
     */
    boolean saveBlog(GroupBlogVo blog);

    IPage<GroupBlogVo> pageItem(Page<GroupBlogVo> page);
}
