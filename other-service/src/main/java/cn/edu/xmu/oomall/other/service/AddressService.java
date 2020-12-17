package cn.edu.xmu.oomall.other.service;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.AddressDao;
import cn.edu.xmu.oomall.other.model.bo.AddressBo;
import cn.edu.xmu.oomall.other.model.bo.RegionBo;
import cn.edu.xmu.oomall.other.model.vo.Address.AddressVo;
import cn.edu.xmu.oomall.other.model.vo.Address.RegionVo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/6 下午16:06
 */
@Service
public class AddressService {
    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    private AddressDao addressDao;

    public ReturnObject<VoObject> addAddress(Long userId, AddressVo addressVo)
    {
        AddressBo addressBo = new AddressBo();
        addressBo = addressVo.createBo();addressBo.setCustomerId(userId);
        try{ReturnObject<VoObject> returnObject = addressDao.addAddress(addressBo);return returnObject;}
        catch (Exception e){ return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);}
    }

    public ReturnObject<PageInfo<VoObject>> getAddresses(Long userId, Integer page, Integer pageSize)
    {
        return addressDao.getAddresses(userId, page, pageSize);
    }

    public ReturnObject<VoObject> updateDefaultAddress(Long userId, Long id)
    {
        return addressDao.updateDefaultAddress(userId, id);
    }

    public ReturnObject<VoObject> updateAddress(Long userId, Long id, AddressVo addressVo)
    {
        AddressBo addressBo = addressVo.createBo();
        addressBo.setCustomerId(userId);
        addressBo.setId(id);
        return addressDao.updateAddress(addressBo);
    }

    public ReturnObject<VoObject> deleteAddress(Long userId, Long id)
    {
        return addressDao.deleteAddress(userId,id);
    }


    public ReturnObject<VoObject> addSubRegion(Long id, RegionVo regionVo)
    {
        RegionBo regionBo = regionVo.createBo();
        regionBo.setPid(id);
        return addressDao.addSubRegion(regionBo);
    }

    public ReturnObject<List>  getAllParentRegions(Long id)
    {
        return addressDao.getAllParentRegions(id);
    }

    public ReturnObject<VoObject> updateRegion(Long id, RegionVo regionVo)
    {
        RegionBo regionBo = regionVo.createBo();
        regionBo.setId(id);
        return addressDao.updateRegion(regionBo);
    }

    public ReturnObject<VoObject> deleteRegion(Long id)
    {
        return addressDao.deleteRegion(id);
    }

}
