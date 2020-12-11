package cn.edu.xmu.oomall.other.model.vo.ShareActivity;

import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午6:04
 */
@Data
@NoArgsConstructor
public class ShareActivityRetVo {
    private Long id;
    private Long shopId;
    private Long goodSkuId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer state;
    public ShareActivityRetVo(ShareActivityBo bo){
        id=bo.getId();
        shopId=bo.getShopId();
        goodSkuId=bo.getGoodSkuId();
        beginTime=bo.getBeginTime();
        endTime=bo.getEndTime();
        state=Integer.valueOf(bo.getState());
    }

}
