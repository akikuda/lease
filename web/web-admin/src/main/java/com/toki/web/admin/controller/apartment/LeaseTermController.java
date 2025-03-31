package com.toki.web.admin.controller.apartment;


import com.toki.common.result.Result;
import com.toki.model.entity.LeaseTerm;
import com.toki.web.admin.service.LeaseTermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author toki
 */
@Tag(name = "租期管理")
@RequestMapping("/admin/term")
@RestController
@RequiredArgsConstructor
public class LeaseTermController {

    private final LeaseTermService leaseTermService;

    @GetMapping("list")
    @Operation(summary = "查询全部租期列表")
    public Result<List<LeaseTerm>> listLeaseTerm() {
        final List<LeaseTerm> leaseTermList = leaseTermService.list();
        if (leaseTermList == null) {
            return Result.fail();
        }
        return Result.ok(leaseTermList);
    }

    @PostMapping("saveOrUpdate")
    @Operation(summary = "保存或更新租期信息")
    public Result saveOrUpdate(@RequestBody LeaseTerm leaseTerm) {
        return Result.ok(leaseTermService.saveOrUpdate(leaseTerm));
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "根据ID删除租期")
    public Result deleteLeaseTermById(@RequestParam Long id) {
        if (id == null) {
            return Result.fail();
        }
        return Result.ok(leaseTermService.removeById(id));
    }
}
