package com.toki.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @author toki
 * group_follow
 */
@TableName(value ="group_follow")
@Data
public class GroupFollow extends BaseEntity{

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 关联的用户id
     */
    @TableField(value = "follow_user_id")
    private Long followUserId;

}