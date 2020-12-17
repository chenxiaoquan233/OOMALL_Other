package cn.edu.xmu.oomall.other.model.vo.Advertisement;

import cn.edu.xmu.oomall.other.model.bo.AdvertiseBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.text.DateFormat;
import java.time.LocalDate;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午5:19
 */
@Data
@ApiModel
public class AdvertiseVo {
    @NotNull(message = "广告内容不能为空")
    @ApiModelProperty(name = "content", value = "message")
    private String content;


    @NotNull(message = "权重不能为空")
    @ApiModelProperty(name = "weight", value = "0")
    private Integer weight;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "开始日期不能为空")
    @ApiModelProperty(name = "beginDate", value = "0")
    private LocalDate beginDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "结束日期不能为空")
    @ApiModelProperty(name = "endDate", value = "0")
    private LocalDate endDate;

    @NotNull(message = "重复不能为空")
    @ApiModelProperty(name = "repeat", value = "0")
    private Boolean repeat;

    @NotNull(message = "链接不能为空")
    @ApiModelProperty(name = "link", value = "https://www.baidu.com")
    private String link;

    public AdvertiseBo createBo(){
        AdvertiseBo advertiseBo = new AdvertiseBo();
        advertiseBo.setContent(this.content);
        advertiseBo.setRepeats(this.repeat);
        advertiseBo.setBeginDate(this.beginDate);
        advertiseBo.setEndDate(this.endDate);
        advertiseBo.setWeight(this.weight);
        advertiseBo.setLink(this.link);
        return advertiseBo;
    }
}
