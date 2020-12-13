package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.AdvertisementPoMapper;
import cn.edu.xmu.oomall.other.mapper.AftersalePoMapper;
import cn.edu.xmu.oomall.other.model.bo.AdvertiseBo;
import cn.edu.xmu.oomall.other.model.po.AddressPoExample;
import cn.edu.xmu.oomall.other.model.po.AdvertisementPo;
import cn.edu.xmu.oomall.other.model.po.AdvertisementPoExample;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseRetVo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseStatesRetVo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseVo;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/12 上午10:44
 */
@Repository
public class AdvertiseDao {
    private static final Logger logger = LoggerFactory.getLogger(AdvertiseDao.class);

    @Autowired
    private AdvertisementPoMapper advertisementPoMapper;

    public List<AdvertiseStatesRetVo> getAllAdvertiseState(){
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria=example.createCriteria();
        return advertisementPoMapper.selectByExample(example).stream().map(x->new AdvertiseBo(x).getState())
                .map(x->new AdvertiseStatesRetVo(x.getCode(),x.getDescription())).collect(Collectors.toList());
    }
    public ResponseCode setAdvertisementDefaultById(Long id){
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria=example.createCriteria();
        criteria.andBeDefaultEqualTo((byte)1);
        List<AdvertisementPo> poList = advertisementPoMapper.selectByExample(example);
        for(AdvertisementPo po:poList){
            po.setBeDefault((byte) 0);
            advertisementPoMapper.updateByPrimaryKey(po);
        }
        AdvertisementPo po = advertisementPoMapper.selectByPrimaryKey(id);
        if (po ==null)return ResponseCode.INTERNAL_SERVER_ERR;
        po.setBeDefault((byte) 1);
        advertisementPoMapper.updateByPrimaryKey(po);
        return ResponseCode.OK;
    }

    public ResponseCode updateAdvertisementById(AdvertiseBo advertiseBo){
        AdvertisementPo advertisementPo = advertiseBo.getAdvertisePo();
        advertisementPo.setGmtModified(LocalDateTime.now());
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(advertiseBo.getId());
        try{
            advertisementPoMapper.updateByExampleSelective(advertisementPo,example);
        }catch (Exception e){
            return ResponseCode.INTERNAL_SERVER_ERR;
        }
        return ResponseCode.OK;
    }


    public ResponseCode deleteAdvertisementById(Long id) {
        try {
            advertisementPoMapper.deleteByPrimaryKey(id);
        }
        catch (Exception e){
            return ResponseCode.INTERNAL_SERVER_ERR;
        }
        return ResponseCode.OK;
    }


    public List<AdvertiseBo> getCurrentAdvertisements() {
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria  criteria= example.createCriteria();
        criteria.andBeginDateLessThanOrEqualTo(LocalDate.now());
        criteria.andEndDateGreaterThanOrEqualTo(LocalDate.now());
        return advertisementPoMapper.selectByExample(example).stream().map(x->new AdvertiseBo(x)).collect(Collectors.toList());
    }
}
