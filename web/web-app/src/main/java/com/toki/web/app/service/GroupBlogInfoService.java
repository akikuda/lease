package com.toki.web.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.model.entity.GroupBlogInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.toki.web.app.vo.group.CommentItemVo;
import com.toki.web.app.vo.group.CommentSubmitVo;
import com.toki.web.app.vo.group.GroupBlogVo;
import com.toki.web.app.vo.user.UserInfoVo;

import java.util.List;

/**
* @author toki
*/
public interface GroupBlogInfoService extends IService<GroupBlogInfo> {

    /**
     * 发布博客,通过feed流的推模式,推送到粉丝收件箱(redis)
     */
    boolean saveBlog(GroupBlogVo blog);

    IPage<GroupBlogVo> pageItem(Page<GroupBlogVo> page, Long apartmentId);

    boolean removeBlogById(Long id);

    boolean likeBlog(Long id);

    List<UserInfoVo> queryBlogLikes(Long id);

    boolean saveComment(CommentSubmitVo comment);

    /**
     * 根据博文ID查询博客评论列表
     * */
    List<CommentItemVo> queryBlogCommentListById(Long id);
}
