package com.toki.web.app.mapper;

import com.toki.model.entity.GroupBlogComments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toki.web.app.vo.group.CommentItemVo;

import java.util.List;

/**
* @author toki
* @description 针对表【group_blog_comments】的数据库操作Mapper
* @createDate 2025-04-12 15:51:13
* @Entity com.toki.model.entity.GroupBlogComments
*/
public interface GroupBlogCommentsMapper extends BaseMapper<GroupBlogComments> {

    List<CommentItemVo> queryBlogCommentListById(Long id);
}




