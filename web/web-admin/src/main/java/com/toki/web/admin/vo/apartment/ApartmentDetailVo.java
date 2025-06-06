package com.toki.web.admin.vo.apartment;


import com.toki.model.entity.ApartmentInfo;
import com.toki.model.entity.FacilityInfo;
import com.toki.model.entity.LabelInfo;
import com.toki.web.admin.vo.graph.GraphVo;
import com.toki.web.admin.vo.fee.FeeValueVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * @author toki
 */
@Schema(description = "公寓信息")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentDetailVo extends ApartmentInfo {

    @Schema(description = "图片列表")
    private List<GraphVo> graphVoList;

    @Schema(description = "标签列表")
    private List<LabelInfo> labelInfoList;

    @Schema(description = "配套列表")
    private List<FacilityInfo> facilityInfoList;

    @Schema(description = "杂费列表")
    private List<FeeValueVo> feeValueVoList;

}
