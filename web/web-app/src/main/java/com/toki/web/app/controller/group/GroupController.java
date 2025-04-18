package com.toki.web.app.controller.group;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.result.Result;
import com.toki.model.entity.GroupBlogComments;
import com.toki.web.app.service.GroupBlogCommentsService;
import com.toki.web.app.service.GroupBlogInfoService;
import com.toki.web.app.vo.group.CommentItemVo;
import com.toki.web.app.vo.group.CommentSubmitVo;
import com.toki.web.app.vo.group.GroupBlogVo;
import com.toki.web.app.vo.user.UserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author toki
 */
@Tag(name = "圈子模块")
@RestController
@RequestMapping("/app/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupBlogInfoService blogService;
    private final GroupBlogCommentsService commentsService;

    @Operation(summary = "发布博文")
    @PostMapping("/saveBlog")
    public Result saveBlog(@RequestBody GroupBlogVo blog) {
        return Result.ok(blogService.saveBlog(blog));
    }

    @Operation(summary = "根据点赞数分页查询博文列表")
    @GetMapping("/pageItem")
    public Result<IPage<GroupBlogVo>> queryHotBlogList(
            @RequestParam long current,
            @RequestParam long size,
            @RequestParam(required = false) Long apartmentId
    ) {
        final Page<GroupBlogVo> page = new Page<>(current, size);
        return Result.ok(blogService.pageItem(page, apartmentId));
    }

    @Operation(summary = "根据id删除博文")
    @DeleteMapping("removeBlogById")
    public Result removeById(@RequestParam Long id) {
        // 需要删除博文、评论、点赞等相关数据
        return Result.ok(blogService.removeBlogById(id));
    }

    @Operation(summary = "根据id改变博文点赞状态")
    @PutMapping("/like/{id}")
    public Result likeBlog(@PathVariable("id") Long id) {
        return Result.ok(blogService.likeBlog(id));
    }

    @Operation(summary = "根据id查询博文点赞前5名用户")
    @GetMapping("/likes/top5/{id}")
    public Result<List<UserInfoVo>> queryBlogLikes(@PathVariable("id") Long id) {
        return Result.ok(blogService.queryBlogLikes(id));
    }

    @Operation(summary = "提交评论")
    @PostMapping("/comment")
    public Result saveComment(@RequestBody CommentSubmitVo comment){
        return Result.ok(blogService.saveComment(comment));
    }

    @Operation(summary = "根据id查询博文评论列表")
    @GetMapping("/comments/{id}")
    public Result<List<CommentItemVo>> queryBlogCommentListById(@PathVariable("id") Long id) {
        return Result.ok(blogService.queryBlogCommentListById(id));
    }

    @Operation(summary = "根据评论id删除评论")
    @DeleteMapping("/removeComment/{id}")
    public Result removeCommentById(@PathVariable("id") Long id){
        return Result.ok(commentsService.removeCommentById(id));
    }


}
