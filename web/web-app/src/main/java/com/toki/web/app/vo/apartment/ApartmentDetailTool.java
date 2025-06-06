package com.toki.web.app.vo.apartment;

import com.toki.model.entity.ApartmentInfo;
import com.toki.model.entity.FacilityInfo;
import com.toki.model.entity.LabelInfo;
import com.toki.web.app.vo.graph.GraphVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author toki
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "APP端公寓信息详情")
public class ApartmentDetailTool extends ApartmentInfo {

    @Schema(description = "标签列表")
    private List<LabelInfo> labelInfoList;

    @Schema(description = "配套列表")
    private List<FacilityInfo> facilityInfoList;

    @Schema(description = "租金最小值")
    private BigDecimal minRent;
}
