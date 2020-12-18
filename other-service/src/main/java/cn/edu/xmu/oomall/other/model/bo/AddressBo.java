package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.AddressPo;
import cn.edu.xmu.oomall.other.model.vo.Address.AddressInfoVo;
import cn.edu.xmu.oomall.other.model.vo.Address.AddressRetVo;
import cn.edu.xmu.oomall.other.model.vo.Address.AddressVo;
import lombok.Data;
import org.apache.tomcat.jni.Address;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private Byte state;



    @Override
    public Object createVo() {
        AddressInfoVo addressInfoVo = new AddressInfoVo();
        addressInfoVo.setId(this.getId());
        addressInfoVo.setRegionId(this.getRegionId());
        addressInfoVo.setDetail(this.getDetail());
        addressInfoVo.setConsignee(this.getConsignee());
        addressInfoVo.setMobile(this.getMobile());
        addressInfoVo.setBeDefault(this.getBeDefault());
        addressInfoVo.setGmtCreate(this.getGmtCreate());
        addressInfoVo.setState(this.getState().intValue());
        return addressInfoVo;
    }

    @Override
    public Object createSimpleVo() {
        AddressRetVo addressRetVo = new AddressRetVo();
        addressRetVo.setId(this.getId());
        addressRetVo.setRegionId(this.getRegionId());
        addressRetVo.setDetail(this.getDetail());
        addressRetVo.setConsignee(this.getConsignee());
        addressRetVo.setMobile(this.getMobile());
        addressRetVo.setBeDefault(this.getBeDefault());
//        addressRetVo.setGmtCreate(this.getGmtCreate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        addressRetVo.setGmtModified(this.getGmtModified().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return addressRetVo;
    }

    /**
     * 用bo对象创建po对象
     * @return po对象
     */
    public AddressPo getAddressPo()
    {
        AddressPo po = new AddressPo();
        //po.setId(this.getId());
        po.setCustomerId(this.getCustomerId());
        po.setRegionId(this.getRegionId());
        po.setDetail(this.getDetail());
        po.setConsignee(this.getConsignee());
        po.setMobile(this.getMobile());
        //po.setBeDefault((byte)(this.getBeDefault()?1:0));
        //po.setGmtCreate(null);
       // po.setGmtModified(null);
        return po;
    }
    public AddressBo(AddressPo po)
    {
        this.setId(po.getId());
        this.setCustomerId(po.getCustomerId());
        this.setRegionId(po.getRegionId());
        this.setDetail(po.getDetail());
        this.setConsignee(po.getConsignee());
        this.setMobile(po.getMobile());
        this.setBeDefault(po.getBeDefault().intValue() == 1);
        this.setGmtCreate(po.getGmtCreate());
        this.setGmtModified(po.getGmtModified());
    }

    public AddressBo()
    {

    }



}
