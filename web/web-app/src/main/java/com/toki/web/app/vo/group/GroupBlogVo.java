package com.toki.web.app.vo.group;

import com.toki.model.entity.GroupBlogInfo;
import com.toki.web.app.vo.graph.GraphVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author toki
 */
@Data
@Schema(description = "博文信息")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupBlogVo extends GroupBlogInfo {

    @Schema(description = "博文图片列表")
    private List<GraphVo> graphVoList;

    @Schema(description = "博文发布时间")
    private String publishTime;
}
