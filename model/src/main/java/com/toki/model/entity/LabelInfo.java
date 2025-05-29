package com.toki.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.toki.model.enums.ItemType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author toki
 */
@Schema(description = "标签信息表")
@TableName(value = "label_info")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "类型")
    @TableField(value = "type")
    private ItemType type;

    @Schema(description = "标签名称")
    @TableField(value = "name")
    private String name;


}