package com.toki.web.admin.controller.apartment;


import com.toki.common.result.Result;
import com.toki.model.entity.PaymentType;
import com.toki.web.admin.service.PaymentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author toki
 */
@Tag(name = "支付方式管理")
@RequestMapping("/admin/payment")
@RestController
@RequiredArgsConstructor
public class PaymentTypeController {

    private final PaymentTypeService paymentTypeService;

    @Operation(summary = "查询全部支付方式列表")
    @GetMapping("list")
    public Result<List<PaymentType>> listPaymentType() {
        final List<PaymentType> paymentTypeList = paymentTypeService.list();
        if (paymentTypeList == null) {
            return Result.fail();
        }
        return Result.ok(paymentTypeList);
    }

    @Operation(summary = "保存或更新支付方式")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdatePaymentType(@RequestBody PaymentType paymentType) {
        return Result.ok(paymentTypeService.saveOrUpdate(paymentType));
    }

    @Operation(summary = "根据ID删除支付方式")
    @DeleteMapping("deleteById")
    public Result deletePaymentById(@RequestParam Long id) {
        if (id == null) {
            return Result.fail();
        }
        return Result.ok(paymentTypeService.removeById(id));
    }

}















