package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.AdvertisementPoMapper;
import cn.edu.xmu.oomall.other.mapper.AftersalePoMapper;
import cn.edu.xmu.oomall.other.model.bo.AdvertiseBo;
import cn.edu.xmu.oomall.other.model.bo.TimeSegmentBo;
import cn.edu.xmu.oomall.other.model.bo.UserBo;
import cn.edu.xmu.oomall.other.model.po.AddressPoExample;
import cn.edu.xmu.oomall.other.model.po.AdvertisementPo;
import cn.edu.xmu.oomall.other.model.po.AdvertisementPoExample;
import cn.edu.xmu.oomall.other.model.po.TimeSegmentPo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseRetVo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseStatesRetVo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.config.AdviceEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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

    @Autowired
    private RedisTemplate redisTemplate;

    public List<AdvertiseStatesRetVo> getAllAdvertiseState(){
        return Arrays.asList(AdvertiseBo.State.values()).stream()
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

    public AdvertiseBo getAdvertiseById(Long id){
        AdvertisementPo po = advertisementPoMapper.selectByPrimaryKey(id);
        if(po == null)return null;
        return new AdvertiseBo(po);
    }

    public List<AdvertiseBo> getAdvertiseByTimeSegmentId(Long id, LocalDate beginDate, LocalDate endDate) {
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria=example.createCriteria();
        criteria.andSegIdEqualTo(id);
        if(beginDate!=null)
            criteria.andBeginDateEqualTo(beginDate);
        if(endDate!=null)
            criteria.andEndDateEqualTo(endDate);
        example.setOrderByClause("weight DESC");
        List<AdvertisementPo> advertisementPoList=advertisementPoMapper.selectByExample(example);
        if(advertisementPoList.size()<=8)
            return advertisementPoList.stream().map(AdvertiseBo::new).collect(Collectors.toList());
        else
            return advertisementPoList.subList(0,8).stream().map(AdvertiseBo::new).collect(Collectors.toList());
//        PageHelper.startPage(page,pageSize,true,true,null);
//        try{
//            advertisementPoList=advertisementPoMapper.selectByExample(example);
//        }catch (Exception e){
//            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
//        }
//        List<Object> ret = advertisementPoList.stream().map(AdvertiseBo::new).map(AdvertiseBo::createVo).collect(Collectors.toList());
//        PageInfo<AdvertisementPo> advertisementPoPageInfo = PageInfo.of(advertisementPoList);
//        PageInfo<Object> pageRet = new PageInfo<>(ret);
//        pageRet.setPages(advertisementPoPageInfo.getPages());
//        pageRet.setPageNum(advertisementPoPageInfo.getPageNum());
//        pageRet.setPageSize(advertisementPoPageInfo.getPageSize());
//        pageRet.setTotal(advertisementPoPageInfo.getTotal());
//        return new ReturnObject<>(pageRet);
    }

    public ResponseCode createAdvertiseByTimeSegId(long segId, AdvertiseBo bo) {
//        AdvertisementPoExample example=new AdvertisementPoExample();
//        AdvertisementPoExample.Criteria criteria=example.createCriteria();
//        criteria.andSegIdEqualTo(segId);
//        List<AdvertisementPo> advertisementPoList=advertisementPoMapper.selectByExample(example);
//        if(advertisementPoList.size()>=8)return ResponseCode.ADVERTISEMENT_OUTLIMIT;
//        AdvertisementPo po;
//        try{po = bo.getAdvertisePo();
//        po.setSegId(segId);
//        advertisementPoMapper.insert(po);}
//        catch(Exception e){return ResponseCode.INTERNAL_SERVER_ERR;}
//        return ResponseCode.OK; // qm你wsm要这么对我
        try {
            advertisementPoMapper.insert(bo.getAdvertisePo());
        }catch (Exception e){
            return  ResponseCode.INTERNAL_SERVER_ERR;
        }return ResponseCode.OK;
    }

    public ResponseCode createAdvertiseByTimeSegIdAndId(long id, long tid) {
//        AdvertisementPoExample example = new AdvertisementPoExample();
//        AdvertisementPoExample.Criteria criteria=example.createCriteria();
//        criteria.andSegIdEqualTo(id);
//        List<AdvertisementPo> advertisementPoList=advertisementPoMapper.selectByExample(example);
//        AdvertisementPo po=advertisementPoMapper.selectByPrimaryKey(id);
//        if(po!=null && po.getSegId()==tid)return ResponseCode.OK;
//        if(advertisementPoList.size()>=8)return ResponseCode.ADVERTISEMENT_OUTLIMIT;
//        try {
//            if (po == null) {
//                po = new AdvertisementPo();
//                po.setSegId(tid);
//                advertisementPoMapper.insert(po);
//            } else {
//                po.setSegId(tid);
//                advertisementPoMapper.updateByPrimaryKey(po);
//            }
//            return ResponseCode.OK;
//        }catch (Exception e){
//            return ResponseCode.INTERNAL_SERVER_ERR;
//        }
        AdvertisementPo po = advertisementPoMapper.selectByPrimaryKey(id);
        try{
            if(po==null){
                po = new AdvertisementPo();
                po.setId(id);
                po.setSegId(tid);
                advertisementPoMapper.insert(po);
            }else{
                po.setSegId(id);
                advertisementPoMapper.updateByPrimaryKey(po);
            }
            return ResponseCode.OK;
        }catch (Exception e){return ResponseCode.INTERNAL_SERVER_ERR;}
    }
}
