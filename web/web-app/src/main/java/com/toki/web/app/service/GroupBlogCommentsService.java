package com.toki.web.app.service;

import com.toki.model.entity.GroupBlogComments;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author toki
*/
public interface GroupBlogCommentsService extends IService<GroupBlogComments> {

    /**
     * 只有博文作者和评论作者可以删除评论
     * */
    boolean removeCommentById(Long id);
}
