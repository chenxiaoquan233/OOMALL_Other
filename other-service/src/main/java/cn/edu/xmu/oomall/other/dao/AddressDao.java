package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.AddressPoMapper;
import cn.edu.xmu.oomall.other.mapper.RegionPoMapper;
import cn.edu.xmu.oomall.other.model.bo.AddressBo;
import cn.edu.xmu.oomall.other.model.bo.RegionBo;
import cn.edu.xmu.oomall.other.model.po.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.tomcat.jni.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.script.ScriptEngine;
import javax.swing.plaf.synth.Region;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/6 15:19
 */

@Repository
public class AddressDao {
    private static final Logger logger = LoggerFactory.getLogger(AddressDao.class);

    @Autowired
    private AddressPoMapper addressPoMapper;

    @Autowired
    private RegionPoMapper regionPoMapper;

    /**
     * 新增地址
     * @param addressBo
     * @return
     */
    public ReturnObject<VoObject>addAddress(AddressBo addressBo){
        ReturnObject<VoObject> retObj = null;
        AddressPo addressPo = addressBo.getAddressPo();
        addressPo.setBeDefault((byte)0);
        addressPo.setGmtCreate(LocalDateTime.now());
        addressPo.setGmtModified(null);
        AddressPoExample addressPoExample = new AddressPoExample();
        AddressPoExample.Criteria criteria = addressPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(addressBo.getCustomerId());
        try {
            List<AddressPo> addressPos = addressPoMapper.selectByExample(addressPoExample);
            if (addressPos.size() >= 20) {
                return new ReturnObject<>(ResponseCode.ADDRESS_OUTLIMIT);
            }
            if(regionPoMapper.selectByPrimaryKey(addressBo.getRegionId()).getState().equals((byte)1)){
                return new ReturnObject<>(ResponseCode.REGION_OBSOLETE);
            }
            addressPoMapper.insertSelective(addressPo);
            AddressBo addressBo1 = new AddressBo(addressPo);
            retObj = new ReturnObject<>(addressBo1);
            return retObj;

        }catch(DataAccessException e){
            logger.error("addAddress: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 用户查询已有地址
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> getAddresses(Long userId,  Integer page, Integer pageSize){
        AddressPoExample example = new AddressPoExample();
        AddressPoExample.Criteria criteria = example.createCriteria();
        if(userId!=null){
            criteria.andCustomerIdEqualTo(userId);
        }
        List<AddressPo> addressPos = null;
        PageHelper.startPage(page,pageSize,true,true,null);
        try{
            addressPos =  addressPoMapper.selectByExample(example);
        }
        catch (DataAccessException e){
            StringBuilder message = new StringBuilder().append("getAddresses: ").append(e.getMessage());
            logger.error(message.toString());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        List<VoObject> ret =addressPos.stream().map(AddressBo::new).collect(Collectors.toList());
        PageInfo<AddressPo> addressesPoPage = PageInfo.of(addressPos);
        PageInfo<VoObject> addressesPage = new PageInfo<>(ret);
        addressesPage.setPages(addressesPoPage.getPages());
        addressesPage.setPageNum(addressesPoPage.getPageNum());
        addressesPage.setPageSize(addressesPoPage.getPageSize());
        addressesPoPage.setTotal(addressesPoPage.getTotal());
        return new ReturnObject<>(addressesPage);
    }

    /**
     * 买家修改自己的地址信息
     * @param addressBo
     * @return
     */
    public ReturnObject<VoObject> updateAddress(AddressBo addressBo)
    {
        AddressPo addressPo = addressBo.getAddressPo();
        addressPo.setGmtModified(LocalDateTime.now());
        AddressPoExample addressPoExample = new AddressPoExample();
        AddressPoExample.Criteria criteria = addressPoExample.createCriteria();
        criteria.andIdEqualTo(addressBo.getId());
        try{
            addressPoMapper.updateByExampleSelective(addressPo,addressPoExample);
        }catch (DataAccessException e)
        {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("addAddress: DataAccessException:%s",e.getMessage()));
        }catch (Exception e) {
        // 其他Exception错误
        return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("Errors：%s", e.getMessage()));
        }
        return new ReturnObject<>(ResponseCode.OK);
    }

    /**
     * 设置默认地址
     * @param userId
     * @param id
     * @return
     */
    public ReturnObject<VoObject> updateDefaultAddress(Long userId, Long id)
    {
        AddressPoExample addressPoExample = new AddressPoExample();
        AddressPoExample.Criteria criteria = addressPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        criteria.andBeDefaultEqualTo((byte)1);
        List<AddressPo> addressPos = addressPoMapper.selectByExample(addressPoExample);
        try{
            if(addressPos.size()>0){
                for(AddressPo po:addressPos)
                {
                    po.setBeDefault((byte)0);
                    addressPoMapper.updateByPrimaryKey(po);
                }
            }
            AddressPo addressPo = addressPoMapper.selectByPrimaryKey(id);
            if(addressPo == null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"address: Address Not Found");
            }
            else{
                addressPo.setBeDefault((byte)1);
                addressPoMapper.updateByPrimaryKey(addressPo);
            }
        }catch (DataAccessException e)
        {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("addAddress: DataAccessException:%s",e.getMessage()));
        }catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("Errors：%s", e.getMessage()));
        }
        return new ReturnObject<>(ResponseCode.OK);
    }

    /**
     * 买家删除地址
     * @param userId
     * @param id
     * @return
     */
    public ReturnObject<VoObject> deleteAddress(Long userId, Long id){
        try{
            addressPoMapper.deleteByPrimaryKey(id);
        }catch (DataAccessException e)
        {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("addAddress: DataAccessException:%s",e.getMessage()));
        }catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("Errors：%s", e.getMessage()));
        }
        return new ReturnObject<>(ResponseCode.OK);
    }

    /**
     * 查询地区的所有父地区
     * @param id
     * @return
     */
    public ReturnObject<List> getAllParentRegions(Long id)
    {
        List<RegionPo> regionPos = null;
        try{
            RegionPo regionPo = regionPoMapper.selectByPrimaryKey(id);
            while(regionPo.getPid()!=null && regionPo.getPid()!=0)
            {
                regionPo = regionPoMapper.selectByPrimaryKey(regionPo.getPid());
                regionPos.add(regionPo);
            }
        }catch (DataAccessException e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("addAddress: DataAccessException:%s",e.getMessage()));
        }catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("Errors：%s", e.getMessage()));
        }
        List<VoObject> regionBos = regionPos.stream().map(RegionBo::new).collect(Collectors.toList());
        return new ReturnObject<>(regionBos);
    }

    /**
     * 添加子地区
     * @param regionBo
     * @return
     */
    public ReturnObject<VoObject> addSubRegion(RegionBo regionBo){
        RegionPo regionPo = regionBo.gotRegionPo();
        /*判断父地区 是否存在 以及 是否被废弃*/
        RegionPoExample regionPoExample = new RegionPoExample();
        RegionPoExample.Criteria criteria = regionPoExample.createCriteria();
        criteria.andIdEqualTo(regionBo.getPid());
        List<RegionPo> regionPos = regionPoMapper.selectByExample(regionPoExample);
        if(regionPos.isEmpty()){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(regionPos.get(0).getState() == (byte)1){
            return new ReturnObject<>(ResponseCode.REGION_OBSOLETE);
        }
        try{
            regionPo.setGmtCreate(LocalDateTime.now());
            regionPo.setGmtModified(null);
            regionPoMapper.insert(regionPo);
        }catch (DataAccessException e)
        {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("addSubRegion: DataAccessException:%s",e.getMessage()));
        }catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("Errors：%s", e.getMessage()));
        }
        return new ReturnObject<>(ResponseCode.OK);
    }

    /**
     * 管理员修改某个地区
     * @param regionBo
     * @return
     */
    public ReturnObject<VoObject> updateRegion(RegionBo regionBo)
    {
        RegionPoExample regionPoExample = new RegionPoExample();
        RegionPoExample.Criteria criteria = regionPoExample.createCriteria();
        criteria.andIdEqualTo(regionBo.getId());
        List<RegionPo> regionPos = regionPoMapper.selectByExample(regionPoExample);
        if(regionPos.isEmpty()){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        try{
            RegionPo regionPo = regionPoMapper.selectByPrimaryKey(regionBo.getId());
            regionPo.setName(regionBo.getName());
            regionPo.setPostalCode(Long.parseLong(regionBo.getPostalCode()));
            regionPo.setGmtModified(LocalDateTime.now());
            regionPoMapper.updateByPrimaryKey(regionPo);
        }catch (DataAccessException e)
        {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("updateRegion: DataAccessException:%s",e.getMessage()));
        }catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("Errors：%s", e.getMessage()));
        }
        return new ReturnObject<>(ResponseCode.OK);
    }

    /**
     * 使某地区无效
     * @param id
     * @return
     */
    public ReturnObject<VoObject> deleteRegion(Long id)
    {
        RegionPoExample regionPoExample = new RegionPoExample();
        RegionPoExample.Criteria criteria = regionPoExample.createCriteria();
        criteria.andIdEqualTo(id);
        List<RegionPo>regionPos = regionPoMapper.selectByExample(regionPoExample);
        if(regionPos.isEmpty()){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        RegionPo regionPo = regionPos.get(0);
        regionPo.setState((byte)1);
        try{
            regionPoMapper.updateByPrimaryKey(regionPo);
        }catch (DataAccessException e)
        {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR,String.format("updateRegion: DataAccessException:%s",e.getMessage()));
        }catch (Exception e) {
            // 其他Exception错误
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("Errors：%s", e.getMessage()));
        }
        return new ReturnObject<>(ResponseCode.OK);
    }

}
