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

    private Long customerId;

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
     * 用bo对象创建po对象
     * @return po对象
     */
    public AddressPo getAddressPo()
    {
        AddressPo po = new AddressPo();
        po.setId(this.getId());
        po.setRegionId(this.getRegionId());
        po.setDetail(this.getDetail());
        po.setConsignee(this.getConsignee());
        po.setMobile(this.getMobile());
        po.setBeDefault((byte)(this.getBeDefault()?1:0));
        po.setGmtCreate(null);
        po.setGmtModified(LocalDateTime.now());
        return po;
    }
    public AddressBo(AddressPo po)
    {
        this.setId(po.getId());
        this.setRegionId(po.getRegionId());
        this.setDetail(po.getDetail());
        this.setConsignee(po.getConsignee());
        this.setMobile(po.getMobile());
        this.setGmtCreate(po.getGmtCreate());
        this.setGmtModified(po.getGmtModified());
    }

    public AddressBo()
    {

    }



}
