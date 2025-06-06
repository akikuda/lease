package com.toki.web.app.vo.room;


import com.toki.model.entity.ApartmentInfo;
import com.toki.model.entity.LabelInfo;
import com.toki.web.app.vo.graph.GraphVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "APP房间列表实体")
@Data
public class RoomItemVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "房间id")
    private Long id;

    @Schema(description = "房间号")
    private String roomNumber;

    @Schema(description = "租金（元/月）")
    private BigDecimal rent;

    @Schema(description = "房间图片列表")
    private List<GraphVo> graphVoList;

    @Schema(description = "房间标签列表")
    private List<LabelInfo> labelInfoList;

    @Schema(description = "房间所属公寓信息")
    private ApartmentInfo apartmentInfo;

}
