package com.toki.web.admin.controller.system;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.exception.LeaseException;
import com.toki.common.result.Result;
import com.toki.common.result.ResultCodeEnum;
import com.toki.model.entity.SystemUser;
import com.toki.model.enums.BaseStatus;
import com.toki.web.admin.service.SystemUserService;
import com.toki.web.admin.vo.system.user.SystemUserItemVo;
import com.toki.web.admin.vo.system.user.SystemUserQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author toki
 */
@Tag(name = "后台用户信息管理")
@RestController
@RequestMapping("/admin/system/user")
@RequiredArgsConstructor
public class SystemUserController {

    private final SystemUserService systemUserService;

    @Operation(summary = "根据条件分页查询后台用户列表")
    @GetMapping("page")
    public Result<IPage<SystemUserItemVo>> page(@RequestParam long current, @RequestParam long size, SystemUserQueryVo queryVo) {
        final Page<SystemUser> page = new Page<>(current, size);
        return Result.ok(systemUserService.pageSystemUser(page, queryVo));
    }

    @Operation(summary = "根据ID查询后台用户信息")
    @GetMapping("getById")
    public Result<SystemUserItemVo> getById(@RequestParam Long id) {
        return Result.ok(systemUserService.getSystemUserItemById(id));
    }

    @Operation(summary = "保存或更新后台用户信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemUser systemUser) {
        // 对密码单独md5处理
        final String password = systemUser.getPassword();
        if (StrUtil.isNotBlank(password)) {
            systemUser.setPassword(DigestUtil.md5Hex(password));
        }

        return Result.ok(systemUserService.saveOrUpdate(systemUser));
    }

    @Operation(summary = "判断后台用户名是否可用")
    @GetMapping("isUserNameAvailable")
    public Result<Boolean> isUsernameExists(@RequestParam String username) {
        final long count = systemUserService.count(
                new LambdaQueryWrapper<>(SystemUser.class)
                        .eq(SystemUser::getUsername, username)
        );

        if (count > 0){
            throw new LeaseException(ResultCodeEnum.ADMIN_USER_NAME_EXIST_ERROR);
        }

        return Result.ok(true);
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "根据ID删除后台用户信息")
    public Result removeById(@RequestParam Long id) {
        systemUserService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据ID修改后台用户状态")
    @PostMapping("updateStatusByUserId")
    public Result updateStatusByUserId(@RequestParam Long id, @RequestParam BaseStatus status) {
        systemUserService.update(
                new LambdaUpdateWrapper<>(SystemUser.class)
                        .eq(SystemUser::getId, id)
                        .set(SystemUser::getStatus, status)
        );
        return Result.ok();
    }
}
