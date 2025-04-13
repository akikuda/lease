package com.toki.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author toki
 * group_blog_info
 */
@TableName(value ="group_blog_info")
@Data
public class GroupBlogInfo extends BaseEntity{

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 公寓id
     */
    @TableField(value = "apartment_id")
    private Long apartmentId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 博文的文字描述
     */
    @TableField(value = "content")
    private String content;

    /**
     * 点赞数量
     */
    @TableField(value = "liked")
    private Integer liked;

    /**
     * 评论数量
     */
    @TableField(value = "comments")
    private Integer comments;

    /**
     * 用户图标
     */
    @Schema(description = "头像url")
    @TableField(value = "avatar_url",exist = false)
    private String avatarUrl;

    /**
     * 用户姓名
     */
    @TableField(exist = false)
    private String name;

    /**
     * 是否点赞过了
     */
    @TableField(exist = false)
    private Boolean isLike;
}