package cn.edu.xmu.oomall.other.model.vo.Aftersale;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午15:13
 */
@Data
public class AftersaleSendbackVo {
    @NotBlank
    @ApiModelProperty(name = "运单号", value = "002")
    private String logSn;
}
