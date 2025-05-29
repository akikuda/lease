package com.toki.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * group_blog_comments
 * @author toki
 */
@TableName(value ="group_blog_comments")
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupBlogComments extends BaseEntity{


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 博文id
     */
    @TableField(value = "blog_id")
    private Long blogId;

    /**
     * 父评论id，指向上一级评论，如果当前是一级评论，则值为null
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 被回复的用户id,即你回复了谁,如果没有回复谁，则值为null
     */
    @TableField(value = "reply_user_id")
    private Long replyUserId;

    /**
     * 回复的内容
     */
    @TableField(value = "content")
    private String content;
}