package com.toki.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Schema(description = "房间&基本属性值关联表")
@TableName(value = "room_attr_value")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomAttrValue extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "房间id")
    @TableField(value = "room_id")
    private Long roomId;

    @Schema(description = "属性值id")
    @TableField(value = "attr_value_id")
    private Long attrValueId;
}