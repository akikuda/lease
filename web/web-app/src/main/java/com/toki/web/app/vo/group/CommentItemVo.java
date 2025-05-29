package com.toki.web.app.vo.group;

import com.toki.model.entity.GroupBlogComments;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author toki
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentItemVo extends GroupBlogComments {
    /**
     * 评论用户昵称
     */
    @Schema(description = "评论用户昵称")
    private String nickname;

    /**
     * 评论用户头像
     */
    @Schema(description = "评论用户头像")
    private String avatarUrl;

    /**
     * 被回复用户昵称
     */
    @Schema(description = "被回复用户昵称")
    private String replyUserNickname;

    /**
     * 子评论列表
     */
    @Schema(description = "子评论列表")
    private List<CommentItemVo> children;
}