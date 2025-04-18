package com.toki.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.toki.common.utils.LoginUserHolder;
import com.toki.model.entity.GroupBlogComments;
import com.toki.model.entity.GroupBlogInfo;
import com.toki.web.app.mapper.GroupBlogCommentsMapper;
import com.toki.web.app.service.GroupBlogCommentsService;
import com.toki.web.app.service.GroupBlogInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class GroupBlogCommentsServiceImpl extends ServiceImpl<GroupBlogCommentsMapper, GroupBlogComments>
        implements GroupBlogCommentsService {

    private final GroupBlogInfoService groupBlogInfoService;

    @Override
    public boolean removeCommentById(Long id) {
        // 查询当前用户ID
        final Long userId = LoginUserHolder.getLoginUser().getUserId();

        // 查询评论，找出对应的博文ID
        final GroupBlogComments comment = query().eq("id", id).one();
        if (comment == null) {
            return false;
        }
        final Long blogId = comment.getBlogId();
        // 根据博文ID查询博文作者ID
        final GroupBlogInfo blog = groupBlogInfoService.query().eq("id", blogId).one();
        final Long blogUserId = blog.getUserId();

        if (!userId.equals(blogUserId) && !userId.equals(comment.getUserId())) {
            return false;
        }
        // 只有博文作者或评论作者可以删除评论
        final boolean isSuccess = this.removeById(id);
        if (isSuccess) {
            // 更新评论数
            groupBlogInfoService.update()
                    .setSql("comments = comments - 1").eq("id", blogId)
                    .update();
        }

        return isSuccess;
    }
}




