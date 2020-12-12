package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.AdvertisementPo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseStatesRetVo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseVo;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    private State state;
    private Integer weight;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Boolean repeats;
    private String message;
    private Boolean beDefault;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public enum State{
        BACK(0, "待审核"),
        NORM(4, "上架"),
        FORBID(6, "下架");

        private static final Map<Integer, AdvertiseBo.State> stateMap;

        static {
            stateMap = new HashMap();
            Arrays.stream(AdvertiseBo.State.values()).forEach(enumitem -> stateMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static AdvertiseBo.State getTypeByCode(Integer code) { return stateMap.get(code); }

        public Integer getCode() { return code; }

        public String getDescription() { return description; }
    }




    public AdvertiseBo(){}

    public AdvertiseBo(AdvertisementPo advertisementPo){
        this.id = advertisementPo.getId();
        this.segId= advertisementPo.getSegId();
        this.link= advertisementPo.getLink();
        this.content= advertisementPo.getContent();
        this.imageUrl= advertisementPo.getImageUrl();
        this.state= State.getTypeByCode((int)advertisementPo.getState());
        this.weight= advertisementPo.getWeight();
        this.beginDate=advertisementPo.getBeginDate();
        this.endDate=advertisementPo.getEndDate();
        this.repeats=advertisementPo.getRepeats()==(byte)1;
        this.message= advertisementPo.getMessage();
        this.beDefault= advertisementPo.getBeDefault()==(byte)1;
        this.gmtCreate=advertisementPo.getGmtCreate();
        this.gmtModified= advertisementPo.getGmtModified();
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
