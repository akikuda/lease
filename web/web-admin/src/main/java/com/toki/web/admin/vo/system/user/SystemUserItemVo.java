package com.toki.web.admin.vo.system.user;

import com.toki.model.entity.SystemUser;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


/**
 * @author toki
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "后台管理系统用户基本信息实体")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemUserItemVo extends SystemUser {

    @Schema(description = "岗位名称")
    @TableField(value = "post_name")
    private String postName;

}
