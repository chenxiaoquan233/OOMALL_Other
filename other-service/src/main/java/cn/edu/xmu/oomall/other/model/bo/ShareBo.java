package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.SharePo;
import cn.edu.xmu.oomall.other.model.vo.Share.SharesVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午6:12
 */
@Data
public class ShareBo implements VoObject {
    private Long id;
    private Long sharerId;
    private Long goodsSpuId;
    private Integer quantity;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Long shareActivityId;

    public ShareBo(){}
    public ShareBo(SharePo sharePo){
        this.id= sharePo.getId();
        this.sharerId=sharePo.getSharerId();
        this.goodsSpuId=sharePo.getGoodsSpuId();
        this.quantity= sharePo.getQuantity();
        this.gmtCreate= sharePo.getGmtCreate();
        this.gmtModified=sharePo.getGmtModified();
        this.shareActivityId= sharePo.getShareActivityId();
    }
    @Override
    public Object createVo() {
        SharesVo sharesVo=new SharesVo();
        sharesVo.setGoodSpuId(this.goodsSpuId);
        sharesVo.setSharerId(this.sharerId);
        return  sharesVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
