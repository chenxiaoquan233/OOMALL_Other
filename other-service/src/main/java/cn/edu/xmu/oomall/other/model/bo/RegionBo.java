package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.RegionPo;
import cn.edu.xmu.oomall.other.model.vo.Address.RegionRetVo;
import cn.edu.xmu.oomall.other.model.vo.Address.RegionVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/03 上午 11:42
 */
@Data
public class RegionBo implements VoObject {
    private Long id;

    private Long pid;

    private String name;

    private String postalCode;

    public enum State {
        VALID(0, "有效"),
        ABOLISH(1, "废除");

        private static final Map<Integer, RegionBo.State> stateMap;

        static {
            stateMap = new HashMap();
            Arrays.stream(State.values()).forEach(enumitem -> stateMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static RegionBo.State getTypeByCode(Integer code) { return stateMap.get(code); }

        public Integer getCode() { return code; }

        public String getDescription() { return description; }
    }

    private  State state = State.VALID;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    @Override
    public Object createVo() {
        RegionRetVo regionRetVo = new RegionRetVo();
        regionRetVo.setId(this.getId());
        regionRetVo.setPid(this.getPid());
        regionRetVo.setName(this.getName());
        //regionRetVo.setPostalCode(this.getPostalCode());
        regionRetVo.setState(this.getState().getCode());
        regionRetVo.setGmtCreate(this.getGmtCreate());
        //regionRetVo.setGmtModified("2020-12-17    11:57:02");
        return regionRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public RegionBo()
    {

    }


    public RegionBo(RegionPo po)
    {
        this.setId(po.getId());
        this.setPid(po.getPid());
        this.setName(po.getName());
        this.setState(State.getTypeByCode(po.getState().intValue()));
        //this.setPostalCode(po.getPostalCode().toString());
        this.setGmtCreate(po.getGmtCreate());
        this.setGmtModified(po.getGmtModified());
    }


    /**
     * 用bo对象创建更新po对象
     *
     * @return RegionPo
     */
    public RegionPo gotRegionPo()
    {
        RegionPo po = new RegionPo();
        po.setId(this.getId());
        po.setPid(this.getPid());
        po.setName(this.getName());
        po.setPostalCode(Long.parseLong(this.getPostalCode()));
        po.setState(this.getState().getCode().byteValue());
        po.setGmtCreate(LocalDateTime.now());
        po.setGmtModified(null);
        return po;
    }

}
