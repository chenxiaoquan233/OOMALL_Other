package cn.edu.xmu.oomall.other.model.bo;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.BeSharePo;
import cn.edu.xmu.oomall.other.model.vo.BeShare.BeSharedRetVo;
import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Jx
 * @version 创建时间：2020/12/5 下午3:24
 */
@Data
public class BeSharedBo  implements  VoObject{
    private Long id;
    private Long skuId;
    private GoodsSkuSimpleVo sku;
    private Long sharerId;
    private Long shareId;
    private Long customerId;
    private Long orderItemId;
    private Integer rebate;
    private Long shareActivityId;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public BeSharedBo(BeSharePo po){
        this.id=po.getId();
        this.skuId=po.getGoodsSkuId();
        this.sharerId=po.getSharerId();
        this.shareId=po.getShareId();
        this.customerId=po.getCustomerId();
        this.orderItemId=po.getOrderId();
        this.rebate=po.getRebate();
        this.shareActivityId=po.getShareActivityId();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }

    @Override
    public Object createVo() {
        BeSharedRetVo vo=new BeSharedRetVo();
        vo.setCustomerId(customerId);
        vo.setGmtCreate(gmtCreate);
        vo.setSharerId(sharerId);
        vo.setId(id);
        vo.setOrderId(orderItemId);
        vo.setRebate(rebate);
        vo.setSku(sku);
        return vo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
