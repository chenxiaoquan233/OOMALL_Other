package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.AddressPo;
import cn.edu.xmu.oomall.other.model.vo.Address.AddressVo;
import lombok.Data;
import org.apache.tomcat.jni.Address;

import java.time.LocalDateTime;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/03 上午 11:42
 */
@Data
public class AddressBo implements VoObject {
    private Long id;

    private Long regionId;

    private String detail;

    private String consignee;

    private String mobile;

    private Boolean beDefault;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;



    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    /**
     * 用vo对象创建更新po对象
     * @param vo vo对象
     * @return po对象
     */
    public AddressPo createUpdatePo(AddressVo vo)
    {
        AddressPo po = new AddressPo();
        po.setId(this.getId());
        po.setRegionId(vo.getRegionId());
        po.setDetail(vo.getDetail());
        po.setConsignee(vo.getConsignee());
        po.setMobile(vo.getMobile());
        po.setBeDefault((byte)(this.getBeDefault()?1:0));
        po.setGmtCreate(null);
        po.setGmtModified(LocalDateTime.now());
        return po;
    }
    /**
     * 用bo对象创建更新po对象
     * @return AddressPo
     */
    public AddressPo gotAddressPo()
    {
        AddressPo po = new AddressPo();
        po.setId(this.getId());
        po.setRegionId(this.getRegionId());
        po.setDetail(this.getDetail());
        po.setConsignee(this.getConsignee());
        po.setMobile(this.getMobile());
        po.setBeDefault((byte)(this.getBeDefault()?1:0));
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());
        return po;
    }


}
