package com.toki.web.app.vo.room;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.math.BigDecimal;

/**
 * @author toki
 */
@Data
@Schema(description = "房间查询实体")
public class RoomQueryVo {


    @ToolParam(required = false, description = "省份Id")
    @Schema(description = "省份Id")
    private Long provinceId;

    @ToolParam(required = false, description = "城市Id")
    @Schema(description = "城市Id")
    private Long cityId;

    @ToolParam(required = false, description = "区域Id")
    @Schema(description = "区域Id")
    private Long districtId;

    @ToolParam(required = false, description = "最小租金")
    @Schema(description = "最小租金")
    private BigDecimal minRent;

    @ToolParam(required = false, description = "最大租金")
    @Schema(description = "最大租金")
    private BigDecimal maxRent;

    @ToolParam(required = false, description = "支付方式Id")
    @Schema(description = "支付方式Id")
    private Long paymentTypeId;

    @ToolParam(required = false, description = "价格排序方式")
    @Schema(description = "价格排序方式", allowableValues = {"desc", "asc"})
    private String orderType;

}
