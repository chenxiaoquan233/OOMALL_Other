package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.ShareActivityPo;
import cn.edu.xmu.oomall.other.model.vo.ShareActivity.ShareActivityRetVo;
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
    private Byte state;
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
        this.state=shareActivityPo.getState();
        this.gmtCreate= shareActivityPo.getGmtCreate();
        this.gmtModified= shareActivityPo.getGmtModified();
    }

    @Override
    public Object createVo() {
        ShareActivityRetVo shareActivityRetVo=new ShareActivityRetVo();
        shareActivityRetVo.setBeginTime(this.beginTime);
        shareActivityRetVo.setEndTime(this.endTime);
        shareActivityRetVo.setId(this.id);
        shareActivityRetVo.setShopId(this.shopId);
        shareActivityRetVo.setState(this.state.intValue());
        return shareActivityRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public ShareActivityPo createPo(){
        ShareActivityPo po=new ShareActivityPo();
        po.setId(this.id);
        po.setShopId(this.shopId);
        po.setGoodsSpuId(this.goodSpuId);
        po.setBeginTime(this.beginTime);
        po.setEndTime(this.endTime);
        po.setStrategy(this.strategy);
        po.setState(this.state);
        po.setBeDeleted(this.beDeleted);
        po.setGmtCreate(this.gmtCreate);
        po.setGmtModified(this.gmtModified);
        return po;
    }
}
