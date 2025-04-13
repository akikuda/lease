package com.toki.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * group_blog_comments
 * @author toki
 */
@TableName(value ="group_blog_comments")
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
     * 关联的1级评论id，如果是一级评论，则值为0
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 回复的评论id
     */
    @TableField(value = "answer_id")
    private Long answerId;

    /**
     * 回复的内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 点赞数量
     */
    @TableField(value = "liked")
    private Integer liked;

    /**
     * 状态，0：正常，1：被举报，2：禁止查看
     */
    @TableField(value = "status")
    private Integer status;
}