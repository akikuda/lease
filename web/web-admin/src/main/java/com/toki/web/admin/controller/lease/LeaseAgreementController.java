package com.toki.web.admin.controller.lease;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.result.Result;
import com.toki.model.entity.LeaseAgreement;
import com.toki.model.enums.LeaseStatus;
import com.toki.web.admin.service.LeaseAgreementService;
import com.toki.web.admin.vo.agreement.AgreementQueryVo;
import com.toki.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author toki
 */
@Tag(name = "租约管理")
@RestController
@RequestMapping("/admin/agreement")
@RequiredArgsConstructor
public class LeaseAgreementController {

    private final LeaseAgreementService leaseAgreementService;

    @Operation(summary = "保存或修改租约信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody LeaseAgreement leaseAgreement) {
        return Result.ok(leaseAgreementService.saveOrUpdate(leaseAgreement));
    }

    @Operation(summary = "根据条件分页查询租约列表")
    @GetMapping("page")
    public Result<IPage<AgreementVo>> page(@RequestParam long current, @RequestParam long size, AgreementQueryVo queryVo) {
        return Result.ok(leaseAgreementService.pageAgreement(new Page<>(current, size), queryVo));
    }

    @Operation(summary = "根据id查询租约信息")
    @GetMapping(name = "getById")
    public Result<AgreementVo> getById(@RequestParam Long id) {
        return Result.ok(leaseAgreementService.getAgreementById(id));
    }

    @Operation(summary = "根据id删除租约信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        return Result.ok(leaseAgreementService.removeById(id));
    }

    @Operation(summary = "根据id更新租约状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam LeaseStatus status) {
        leaseAgreementService.update(
                new LambdaUpdateWrapper<>(LeaseAgreement.class)
                        .eq(LeaseAgreement::getId, id)
                        .set(LeaseAgreement::getStatus, status)
        );
        return Result.ok();
    }

}

