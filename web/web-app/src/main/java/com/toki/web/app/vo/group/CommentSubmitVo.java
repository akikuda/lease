package com.toki.web.app.vo.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


/**
 * @author toki
 */
@Data
public class CommentSubmitVo {

    /**
     * 博文ID
     */
    private Long blogId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 1024, message = "评论内容不能超过1024个字符")
    private String content;

    /**
     * 父评论ID，如果是回复某条评论，则需要传入
     */
    private Long parentId;

    /**
     * 被回复的用户ID，如果是回复某人的评论，则需要传入
     */
    private Long replyUserId;
}
