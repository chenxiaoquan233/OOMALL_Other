package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.AdvertisementPo;
import cn.edu.xmu.other.model.vo.Advertisement.AdvertiseVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午5:24
 */
@Data
public class AdvertiseBo implements VoObject {
    private Long id;
    private Long segId;
    private String link;
    private String content;
    private String imageUrl;
    private Byte state;
    private Integer weight;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private Byte repeats;
    private String message;
    private Byte beDefault;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public AdvertiseBo(){}

    public AdvertiseBo(AdvertisementPo advertisementPo){
        this.link= advertisementPo.getLink();
        this.weight= advertisementPo.getWeight();
        this.content= advertisementPo.getContent();
        this.segId= advertisementPo.getSegId();
    }
    @Override
    public Object createVo() {
        AdvertiseVo advertiseVo = new AdvertiseVo();
        advertiseVo.setContent(this.content);
        advertiseVo.setLink(this.link);
        advertiseVo.setSeg_id(this.segId);
        advertiseVo.setWeight(this.weight);
        return advertiseVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
