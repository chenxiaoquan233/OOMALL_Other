package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.TimeSegmentPo;
import cn.edu.xmu.oomall.other.model.vo.TimeSegment.TimeSegmentRetVo;
import cn.edu.xmu.oomall.other.model.vo.TimeSegment.TimeSegmentVo;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/11 下午1:10
 */
@Data
public class TimeSegmentBo implements VoObject {
    public TimeSegmentBo(){}
    public TimeSegmentBo(TimeSegmentPo timeSegmentPo){
        id=timeSegmentPo.getId();
        beginTime=timeSegmentPo.getBeginTime();
        endTime=timeSegmentPo.getEndTime();
        type=Type.getTypeByCode((int)timeSegmentPo.getType());
        gmtCreate=timeSegmentPo.getGmtCreate();
        gmtModified=timeSegmentPo.getGmtModified();
    }
    @Override
    public Object createVo() {
        TimeSegmentRetVo vo=new TimeSegmentRetVo();
        vo.setBeginTime(this.beginTime);
        vo.setEndTime(this.endTime);
        vo.setId(this.id);
        vo.setGmtCreate(this.gmtCreate);
        vo.setGmtModified(this.gmtModified);
        return vo;

    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    /*
        states
            */
    public enum Type {
        ADS(0, "广告"),
        FLASHSALE(1, "秒杀");

        private static final Map<Integer, TimeSegmentBo.Type> typeMap;
        static {
            typeMap = new HashMap();
            Arrays.stream(TimeSegmentBo.Type.values()).forEach(enumitem -> typeMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        Type(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static TimeSegmentBo.Type getTypeByCode(Integer code) { return typeMap.get(code); }

        public Integer getCode() { return code; }

        public String getDescription() { return description; }
    }

    private Long id;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Type type=Type.ADS;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
