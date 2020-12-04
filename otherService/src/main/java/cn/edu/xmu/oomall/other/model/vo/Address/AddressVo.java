package cn.edu.xmu.oomall.other.model.vo.Address;


import cn.edu.xmu.oomall.other.model.bo.AddressBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 上午11:02
 */
@ApiModel(description = "地址传值对象")
@Data
public class AddressVo {
    @NotBlank
    @ApiModelProperty(name = "区域ID", value = "t001")
    private Long regionId;

    @NotBlank
    @ApiModelProperty(name = "详细地址", value = "思明区厦大学生公寓")
    private String detail;

    @NotBlank
    @ApiModelProperty(name = "联系人", value = "曹某")
    private String consignee;

    @NotBlank
    @Pattern(regexp = "[+]?[0-9*#]*")
    @ApiModelProperty(name = "手机号", value = "12300000000")
    private String mobile;

    public AddressBo createBo()
    {
        AddressBo bo = new AddressBo();
        bo.setRegionId(this.getRegionId());
        bo.setDetail(this.getDetail());
        bo.setConsignee(this.getConsignee());
        bo.setMobile(this.getMobile());
        return bo;
    }
}
