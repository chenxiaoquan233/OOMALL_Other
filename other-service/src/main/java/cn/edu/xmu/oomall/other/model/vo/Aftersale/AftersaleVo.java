package cn.edu.xmu.oomall.other.model.vo.Aftersale;

import cn.edu.xmu.oomall.other.model.bo.AftersaleOrderBo;
import cn.edu.xmu.oomall.other.model.po.AftersalePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午12:52
 */
@ApiModel(description = "售后传值对象")
@Data
public class AftersaleVo {
    @NotBlank
    @ApiModelProperty(name = "订单明细ID", value = "坏了")
    private Long id;

    @Pattern(regexp = "", message = "换货/退货")
    @ApiModelProperty(name = "售后类别", value = "0")
    private Integer type;

    @Min(1)
    @NotBlank
    @ApiModelProperty(name = "数量", value = "1")
    private Integer quantity;

    @NotBlank
    @ApiModelProperty(name = "原因", value = "坏了")
    private String reason;

    @NotBlank
    @ApiModelProperty(name = "区域ID", value = "123")
    private Long regionId;

    @NotBlank
    @ApiModelProperty(name = "细节", value = "无")
    private String detail;

    @NotBlank
    @ApiModelProperty(name = "联系人", value = "曹某")
    private String consignee;

    @NotBlank
    @Pattern(regexp = "[+]?[0-9*#]*")
    @ApiModelProperty(name = "手机号", value = "12300000000")
    private String mobile;
    public AftersaleOrderBo createBo()
    {
        AftersaleOrderBo bo = new AftersaleOrderBo();
        bo.setOrderItemId(this.getId());
        bo.setQuantity(this.getQuantity());
        bo.setReason(this.getReason());
        bo.setRegionId(this.getRegionId());
        bo.setDetail(this.getDetail());
        bo.setConsignee(this.getConsignee());
        bo.setMobile(this.getMobile());
        return bo;
    }
}
