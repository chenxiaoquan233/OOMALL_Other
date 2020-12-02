package cn.edu.xmu.other.model.vo.FootPrint;

import cn.edu.xmu.other.model.bo.FootPrintBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午1:07
 */
@Data
@ApiModel
public class FootPrintVo {
    @NotNull(message = "SkuId不能为空")
    @ApiModelProperty(name = "goodSkuID", value = "0")
    private Long goodSkuID;

    public FootPrintBo createBo(){
        FootPrintBo footPrintBo=new FootPrintBo();
        footPrintBo.setGoodSkuId(this.goodSkuID);
        return footPrintBo;
    }
}
