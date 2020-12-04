package cn.edu.xmu.oomall.other.model.vo.Aftersale;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午15:13
 */
@Data
public class AftersaleConfirmVo {
    @NotBlank
    @ApiModelProperty(name = "店铺ID", value = "111")
    private Long shopId;

    @NotBlank
    @ApiModelProperty(name = "售后单ID", value = "222")
    private Long id;

    @NotBlank
    @ApiModelProperty(name = "处理结果", value = "true")
    private Boolean confirm;

    @NotBlank
    @ApiModelProperty(name = "处理意见", value = "no")
    private String conclusion;
}
