package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.ShareActivityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午6:03
 */
@Data
@ApiModel
public class ShareActivityVo {
    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty( name = "beginTime")
    private LocalDateTime beginTime;

    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty( name = "endTime")
    private LocalDateTime endTime;

    @NotNull(message = "")
    @ApiModelProperty( name = "优惠方案不能为空")
    private String strategy;

    public ShareActivityBo createBo(){
        ShareActivityBo shareActivityBo=new ShareActivityBo();
        shareActivityBo.setBeginTime(this.beginTime);
        shareActivityBo.setEndTime(this.endTime);
        shareActivityBo.setStrategy(this.strategy);
        return shareActivityBo;
    }
}
