package com.toki.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.toki.common.result.Result;
import com.toki.model.entity.AttrKey;
import com.toki.model.entity.AttrValue;
import com.toki.web.admin.service.AttrKeyService;
import com.toki.web.admin.service.AttrValueService;
import com.toki.web.admin.vo.attr.AttrKeyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author toki
 */
@Tag(name = "房间属性管理")
@RestController
@RequestMapping("/admin/attr")
@RequiredArgsConstructor
public class AttrController {

    private final AttrKeyService keyService;
    private final AttrValueService valueService;

    @Operation(summary = "新增或更新属性名称")
    @PostMapping("key/saveOrUpdate")
    public Result saveOrUpdateAttrKey(@RequestBody AttrKey attrKey) {
                return Result.ok(keyService.saveOrUpdate(attrKey));
    }

    @Operation(summary = "新增或更新属性值")
    @PostMapping("value/saveOrUpdate")
    public Result saveOrUpdateAttrValue(@RequestBody AttrValue attrValue) {
        return Result.ok(valueService.saveOrUpdate(attrValue));
    }


    /**
     * 多表联查key表和value表
     * */
    @Operation(summary = "查询全部属性名称和属性值列表")
    @GetMapping("list")
    public Result<List<AttrKeyVo>> listAttrInfo() {
        return keyService.listAttrInfo();
    }

    @Operation(summary = "根据id删除属性名称")
    @DeleteMapping("key/deleteById")
    public Result removeAttrKeyById(@RequestParam Long attrKeyId) {
        if (attrKeyId == null){
            return Result.fail();
        }
        // 删除属性名
        keyService.removeById(attrKeyId);
        // 同时要删除属性值
        valueService.remove(new LambdaQueryWrapper<>(AttrValue.class).eq(AttrValue::getAttrKeyId, attrKeyId));
        return Result.ok();
    }

    @Operation(summary = "根据id删除属性值")
    @DeleteMapping("value/deleteById")
    public Result removeAttrValueById(@RequestParam Long id) {
        if (id == null){
            return Result.fail();
        }
        return Result.ok(valueService.removeById(id));
    }

}
