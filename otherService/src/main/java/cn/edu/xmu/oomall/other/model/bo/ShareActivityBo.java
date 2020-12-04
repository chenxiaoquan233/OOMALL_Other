package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.ShareActivityPo;
import cn.edu.xmu.oomall.other.model.vo.ShareActivity.ShareActivityVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午6:53
 */
@Data
public class ShareActivityBo implements VoObject {
    private Long id;
    private Long shopId;
    private Long goodSpuId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String strategy;
    private Byte beDeleted;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public ShareActivityBo(){}
    public ShareActivityBo(ShareActivityPo shareActivityPo){
        this.id= shareActivityPo.getId();
        this.shopId= shareActivityPo.getShopId();
        this.goodSpuId= shareActivityPo.getGoodsSpuId();
        this.beginTime= shareActivityPo.getBeginTime();
        this.endTime=shareActivityPo.getEndTime();
        this.strategy= shareActivityPo.getStrategy();
        this.beDeleted= shareActivityPo.getBeDeleted();
        this.gmtCreate= shareActivityPo.getGmtCreate();
        this.gmtModified= shareActivityPo.getGmtModified();
    }

    @Override
    public Object createVo() {
        ShareActivityVo shareActivityVo=new ShareActivityVo();
        shareActivityVo.setBeginTime(this.beginTime);
        shareActivityVo.setEndTime(this.endTime);
        shareActivityVo.setStrategy(this.strategy);
        return shareActivityVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
