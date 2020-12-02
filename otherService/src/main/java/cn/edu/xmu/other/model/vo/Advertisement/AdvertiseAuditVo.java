package cn.edu.xmu.other.model.vo.Advertisement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午5:50
 */
@Data
@ApiModel
public class AdvertiseAuditVo {
    @NotNull(message = "结果不能为空")
    @ApiModelProperty(name = "conclusion", value = "true")
    private Boolean conclusion;

    @NotNull(message = "信息不能为空")
    @ApiModelProperty(name = "message", value = "message")
    private String message;
}
