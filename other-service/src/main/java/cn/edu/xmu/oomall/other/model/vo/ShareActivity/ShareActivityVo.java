package cn.edu.xmu.oomall.other.model.vo.ShareActivity;

import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty( name = "beginTime")
    private LocalDateTime beginTime;

    @NotNull(message = "结束时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty( name = "endTime")
    private LocalDateTime endTime;

    @NotBlank(message = "优惠方案不能为空")
    @ApiModelProperty( name = "strategy")
    private String strategy;

    public ShareActivityBo createBo(){
        ShareActivityBo shareActivityBo=new ShareActivityBo();
        shareActivityBo.setBeginTime(this.beginTime);
        shareActivityBo.setEndTime(this.endTime);
        shareActivityBo.setStrategy(this.strategy);
        return shareActivityBo;
    }
}
