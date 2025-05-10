package com.toki.web.app.vo.room;

import com.toki.model.entity.*;
import com.toki.web.app.vo.attr.AttrValueVo;
import com.toki.web.app.vo.fee.FeeValueVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 这些是要查出来的结果，都是可选的,与RoomDetailVo不同的是没有图片列表
 * @author toki
 */
@Data
public class RoomQueryToolResult extends RoomItemVo {
    @Schema(description = "房间所属公寓信息")
    private ApartmentInfo apartmentInfo;

    @Schema(description = "属性信息列表")
    private List<AttrValueVo> attrValueVoList;

    @Schema(description = "配套信息列表")
    private List<FacilityInfo> facilityInfoList;

    @Schema(description = "标签信息列表")
    private List<LabelInfo> labelInfoList;

    @Schema(description = "支付方式列表")
    private List<PaymentType> paymentTypeList;

    @Schema(description = "杂费列表")
    private List<FeeValueVo> feeValueVoList;

    @Schema(description = "租期列表")
    private List<LeaseTerm> leaseTermList;
}
