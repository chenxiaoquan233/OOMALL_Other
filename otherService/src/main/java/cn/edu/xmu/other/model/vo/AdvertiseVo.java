package cn.edu.xmu.other.model.vo;

import cn.edu.xmu.other.model.bo.AdvertiseBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

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

    @NotNull(message = "时间段id不能为空")
    @ApiModelProperty(name = "seg_id", value = "0")
    private Long seg_id;

    @NotNull(message = "权重不能为空")
    @ApiModelProperty(name = "weight", value = "0")
    private Integer weight;

    @NotNull(message = "链接不能为空")
    @ApiModelProperty(name = "link", value = "https://www.baidu.com")
    private String link;

    public AdvertiseBo createBo(){
        AdvertiseBo advertiseBo = new AdvertiseBo();
        advertiseBo.setContent(this.content);
        advertiseBo.setSegId(this.seg_id);
        advertiseBo.setWeight(this.weight);
        advertiseBo.setLink(this.link);
        return advertiseBo;
    }
}
