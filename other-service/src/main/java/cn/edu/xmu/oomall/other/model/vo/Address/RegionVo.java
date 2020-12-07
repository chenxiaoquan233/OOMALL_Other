package cn.edu.xmu.oomall.other.model.vo.Address;

import cn.edu.xmu.oomall.other.model.bo.RegionBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午12:21
 */
@Data
public class RegionVo {
    @NotBlank
    @ApiModelProperty(name = "地区名", value = "思明区")
    private String name;

    @NotBlank
    @ApiModelProperty(name = "地区邮政编码", value = "679100")
    private String postalCode;

    public RegionBo createBo()
    {
        RegionBo bo = new RegionBo();
        bo.setName(this.getName());
        bo.setPostalCode(this.getPostalCode());
        return bo;
    }
}
