package com.toki.web.app.controller.group;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.result.Result;
import com.toki.model.entity.GroupBlogInfo;
import com.toki.web.app.service.GroupBlogInfoService;
import com.toki.web.app.vo.group.GroupBlogVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author toki
 */
@Tag(name = "圈子模块")
@RestController
@RequestMapping("/app/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupBlogInfoService groupBlogInfoService;

    @Operation(summary = "发布博文")
    @PostMapping("/saveBlog")
    public Result saveBlog(@RequestBody GroupBlogVo blog) {
        return Result.ok(groupBlogInfoService.saveBlog(blog));
    }

    @Operation(summary = "根据点赞数分页查询博文列表")
    @GetMapping("/pageItem")
    public Result<IPage<GroupBlogVo>> queryHotBlogList(@RequestParam long current, @RequestParam long size) {
        final Page<GroupBlogVo> page = new Page<>(current, size);
        return Result.ok(groupBlogInfoService.pageItem(page));
    }

}
