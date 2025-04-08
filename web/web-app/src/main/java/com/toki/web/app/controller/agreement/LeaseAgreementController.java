package com.toki.web.app.controller.agreement;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.toki.common.result.Result;
import com.toki.common.utils.LoginUserHolder;
import com.toki.model.entity.LeaseAgreement;
import com.toki.model.enums.LeaseStatus;
import com.toki.web.app.service.LeaseAgreementService;
import com.toki.web.app.vo.agreement.AgreementDetailVo;
import com.toki.web.app.vo.agreement.AgreementItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author toki
 */
@RestController
@RequestMapping("/app/agreement")
@Tag(name = "租约信息")
@RequiredArgsConstructor
public class LeaseAgreementController {

    private final LeaseAgreementService leaseAgreementService;

    @Operation(summary = "获取个人租约基本信息列表")
    @GetMapping("listItem")
    public Result<List<AgreementItemVo>> listItem() {
        // 后台创建租约时只保存了用户的姓名、手机号、身份证号，没有用户ID，所以这里只能通过手机号来获取当前用户
        final String phone = LoginUserHolder.getLoginUser().getUsername();

        return Result.ok(leaseAgreementService.listItemByPhone(phone));
    }

    @Operation(summary = "根据id获取租约详细信息")
    @GetMapping("getDetailById")
    public Result<AgreementDetailVo> getDetailById(@RequestParam Long id) {
        return Result.ok(leaseAgreementService.getLeaseAgreementDetailById(id));
    }

    @Operation(summary = "根据id更新租约状态", description = "用于确认租约和提前退租")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam LeaseStatus leaseStatus) {
        return Result.ok(
                leaseAgreementService.update(
                        new LambdaUpdateWrapper<>(LeaseAgreement.class)
                                .eq(LeaseAgreement::getId, id)
                                .set(LeaseAgreement::getStatus, leaseStatus.getCode())
                )
        );
    }

    @Operation(summary = "保存或更新租约", description = "用于续约")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody LeaseAgreement leaseAgreement) {
        return Result.ok(leaseAgreementService.saveOrUpdate(leaseAgreement));
    }

}
