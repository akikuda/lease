package com.toki.web.app.controller.leasaterm;


import com.toki.common.result.Result;
import com.toki.model.entity.LeaseTerm;
import com.toki.web.app.service.LeaseTermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author toki
 */
@RestController
@RequestMapping("/app/term/")
@Tag(name = "租期信息")
@RequiredArgsConstructor
public class LeaseTermController {

    private final LeaseTermService leaseTermService;

    @GetMapping("listByRoomId")
    @Operation(summary = "根据房间id获取可选获取租期列表")
    public Result<List<LeaseTerm>> list(@RequestParam Long id) {
        return Result.ok(leaseTermService.listByRoomId(id));
    }
}
