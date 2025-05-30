package com.toki.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "房间基本属性表")
@TableName(value = "attr_key")
@EqualsAndHashCode(callSuper = true)
@Data
public class AttrKey extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "属性key")
    @TableField(value = "name")
    private String name;

}