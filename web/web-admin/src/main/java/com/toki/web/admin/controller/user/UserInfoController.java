package com.toki.web.admin.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.result.Result;
import com.toki.model.entity.UserInfo;
import com.toki.model.enums.BaseStatus;
import com.toki.web.admin.service.UserInfoService;
import com.toki.web.admin.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author toki
 */
@Tag(name = "用户信息管理")
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    /**
     * 单表分页查询，直接构造条件，不用写mapper接口
     */
    @Operation(summary = "分页查询用户信息")
    @GetMapping("page")
    public Result<IPage<UserInfo>> pageUserInfo(@RequestParam long current, @RequestParam long size, UserInfoQueryVo queryVo) {
        final Page<UserInfo> page = new Page<>(current, size);
        final IPage<UserInfo> result = userInfoService.page(
                page,
                new LambdaQueryWrapper<>(UserInfo.class)
                        .like(queryVo.getPhone() != null, UserInfo::getPhone, queryVo.getPhone())
                        .eq(queryVo.getStatus() != null, UserInfo::getStatus, queryVo.getStatus())
        );
        return Result.ok(result);
    }

    @Operation(summary = "根据用户id更新账号状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam BaseStatus status) {
        userInfoService.update(
                new LambdaUpdateWrapper<>(UserInfo.class)
                        .eq(UserInfo::getId, id)
                        .set(UserInfo::getStatus, status)
        );
        return Result.ok();
    }
}
