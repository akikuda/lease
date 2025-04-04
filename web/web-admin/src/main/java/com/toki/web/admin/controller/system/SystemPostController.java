package com.toki.web.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.exception.LeaseException;
import com.toki.common.result.Result;
import com.toki.common.result.ResultCodeEnum;
import com.toki.model.entity.SystemPost;
import com.toki.model.entity.SystemUser;
import com.toki.model.enums.BaseStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.toki.web.admin.service.SystemPostService;
import com.toki.web.admin.service.SystemUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author toki
 */
@RestController
@Tag(name = "后台用户岗位管理")
@RequestMapping("/admin/system/post")
@RequiredArgsConstructor
public class SystemPostController {

    private final SystemPostService postService;
    private final SystemUserService userService;

    /**
     * 无条件分页查询，直接返回就行
     */
    @Operation(summary = "分页获取岗位信息")
    @GetMapping("page")
    private Result<IPage<SystemPost>> page(@RequestParam long current, @RequestParam long size) {
        return Result.ok(postService.page(new Page<>(current, size)));
    }

    @Operation(summary = "保存或更新岗位信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemPost systemPost) {
        return Result.ok(postService.saveOrUpdate(systemPost));
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "根据id删除岗位")
    public Result removeById(@RequestParam Long id) {
        // 若岗位下有用户，则不能删除岗位
        final long count = userService.count(
                new LambdaQueryWrapper<>(SystemUser.class)
                        .eq(SystemUser::getPostId, id)
        );
        if (count > 0) {
            throw new LeaseException(ResultCodeEnum.SYSTEM_POST_DELETE_ERROR);
        }

        postService.removeById(id);
        return Result.ok();
    }

    @GetMapping("getById")
    @Operation(summary = "根据id获取岗位信息")
    public Result<SystemPost> getById(@RequestParam Long id) {
        return Result.ok(postService.getById(id));
    }

    @Operation(summary = "获取全部岗位列表")
    @GetMapping("list")
    public Result<List<SystemPost>> list() {
        return Result.ok(postService.list());
    }

    @Operation(summary = "根据岗位id修改状态")
    @PostMapping("updateStatusByPostId")
    public Result updateStatusByPostId(@RequestParam Long id, @RequestParam BaseStatus status) {
        LambdaUpdateWrapper<SystemPost> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SystemPost::getId, id);
        updateWrapper.set(SystemPost::getStatus, status);
        postService.update(updateWrapper);
        return Result.ok();
    }
}
