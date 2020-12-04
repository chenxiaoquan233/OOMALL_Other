package cn.edu.xmu.oomall.other.model.vo.Aftersale;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午15:13
 */
@Data
public class AftersaleDeliverVo {
    @NotBlank
    @ApiModelProperty(name = "店铺ID", value = "111")
    private Long shopId;

    @NotBlank
    @ApiModelProperty(name = "售后单ID", value = "222")
    private Long id;

    @NotBlank
    @ApiModelProperty(name = "运单号", value = "333")
    private String logSn;
}
