package cn.edu.xmu.oomall.other.model.vo.FootPrint;

import cn.edu.xmu.oomall.other.model.bo.FootPrintBo;
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
    private Long customerId;
    private Long goodsSkuId;
}
