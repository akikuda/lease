package com.toki.web.admin.vo.apartment;


import com.toki.model.entity.ApartmentInfo;
import com.toki.web.admin.vo.graph.GraphVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * @author toki
 */
@Schema(description = "公寓信息")
@EqualsAndHashCode(callSuper = true)
@Data
public class ApartmentSubmitVo extends ApartmentInfo {

    @Schema(description="公寓配套id")
    private List<Long> facilityInfoIds;

    @Schema(description="公寓标签id")
    private List<Long> labelIds;

    @Schema(description="公寓杂费值id")
    private List<Long> feeValueIds;

    @Schema(description="公寓图片id")
    private List<GraphVo> graphVoList;

}
