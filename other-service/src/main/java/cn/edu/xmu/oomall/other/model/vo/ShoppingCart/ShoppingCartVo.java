package cn.edu.xmu.oomall.other.model.vo.ShoppingCart;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Jx
 * @version 创建时间：2020/12/3 上午8:11
 */
@Data
@ApiModel
public class ShoppingCartVo {

    @NotBlank
    @ApiModelProperty(name = "商品skuId", value = "0")
    private Long goodsSkuId;

    @NotBlank
    @ApiModelProperty(name = "数量", value = "1")
    private Integer quantity;
}
