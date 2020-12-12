package cn.edu.xmu.oomall.other.model.vo.Aftersale;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午15:13
 */
@Data
public class AftersaleConfirmVo {
    @NotBlank
    @ApiModelProperty(name = "处理结果", value = "true")
    private Boolean confirm;

    @Min(value = 0)
    @NotNull
    private Long price;

    @NotBlank
    @ApiModelProperty(name = "处理意见", value = "no")
    private String conclusion;
}
