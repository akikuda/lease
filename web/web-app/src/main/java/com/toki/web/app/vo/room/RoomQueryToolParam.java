package com.toki.web.app.vo.room;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 这些是查询参数，可选
 * @author toki
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoomQueryToolParam extends RoomQueryVo {

    @ToolParam(required = false, description = "房间所属公寓id")
    @Schema(description = "房间所属公寓id")
    private Long apartmentId;

    @ToolParam(required = false, description = "房间id")
    private Long roomId;
}
