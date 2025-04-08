package com.toki.web.app.controller.history;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toki.common.result.Result;
import com.toki.common.utils.LoginUserHolder;
import com.toki.web.app.service.BrowsingHistoryService;
import com.toki.web.app.vo.history.HistoryItemVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toki
 */
@RestController
@Tag(name = "浏览历史管理")
@RequestMapping("/app/history")
@RequiredArgsConstructor
public class BrowsingHistoryController {

    private final BrowsingHistoryService browsingHistoryService;

    @Operation(summary = "获取当前用户的浏览历史")
    @GetMapping("pageItem")
    private Result<IPage<HistoryItemVo>> page(@RequestParam long current, @RequestParam long size) {
        return Result.ok(
                browsingHistoryService.pageItemByUserId(
                        new Page<HistoryItemVo>(current, size),
                        LoginUserHolder.getLoginUser().getUserId()
                )
        );
    }
}
