package cn.edu.xmu.oomall.other.model.bo;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.BeSharePo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Jx
 * @version 创建时间：2020/12/5 下午3:24
 */
@Data
public class BeSharedBo  {
    private Long id;
    private Long goodsSkuId;
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
        //this.goodsSkuId=po.getGoodsSpuId();
        this.sharerId=po.getSharerId();
        this.shareId=po.getShareId();
        this.customerId=po.getCustomerId();
        //this.orderItemId=po.getOrderItemId();
        this.rebate=po.getRebate();
        this.shareActivityId=po.getShareActivityId();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();
    }

}
