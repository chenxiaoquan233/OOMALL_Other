package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.SharePo;
import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
import cn.edu.xmu.oomall.other.model.vo.Share.ShareRetVo;
import cn.edu.xmu.oomall.other.model.vo.Share.ShareVo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午6:12
 */
@Data
public class ShareBo implements VoObject, Serializable {
    private Long id;
    private Long sharerId;
    private Long skuId;
    private GoodsSkuSimpleVo sku;
    private Integer quantity;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Long shareActivityId;

    public ShareBo(){}
    public ShareBo(SharePo sharePo){
        this.id= sharePo.getId();
        this.sharerId=sharePo.getSharerId();
        this.skuId=sharePo.getGoodsSkuId();
//remember to get sku object
        this.quantity= sharePo.getQuantity();
        this.gmtCreate= sharePo.getGmtCreate();
        this.gmtModified=sharePo.getGmtModified();
        this.shareActivityId= sharePo.getShareActivityId();
    }
    @Override
    public Object createVo() {
        ShareRetVo retVo=new ShareRetVo();
        retVo.setGmtCreate(gmtCreate);
        retVo.setId(id);
        retVo.setQuantity(quantity);
        retVo.setSharerId(sharerId);
        retVo.setSku(sku);
        return retVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
