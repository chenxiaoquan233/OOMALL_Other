package cn.edu.xmu.oomall.other.model.vo.Share;

import cn.edu.xmu.oomall.other.model.bo.ShareBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午6:01
 */
@Data
@ApiModel
public class SharesVo {
    @NotNull(message = "分享者Id不能为空")
    @ApiModelProperty(name = "shareId", value = "0")
    private Long sharerId;

    @NotNull(message = "商品SpuId不能为空")
    @ApiModelProperty(name = "goodsSpuId", value = "0")
    private Long goodSpuId;

    public ShareBo createBo(){
        ShareBo sharesBo = new ShareBo();
        sharesBo.setSharerId(this.sharerId);
        sharesBo.setGoodsSpuId(this.goodSpuId);
        return sharesBo;
    }
}
