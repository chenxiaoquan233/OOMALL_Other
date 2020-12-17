package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.AdvertisementPoMapper;
import cn.edu.xmu.oomall.other.mapper.AftersalePoMapper;
import cn.edu.xmu.oomall.other.mapper.TimeSegmentPoMapper;
import cn.edu.xmu.oomall.other.model.bo.AdvertiseBo;
import cn.edu.xmu.oomall.other.model.bo.TimeSegmentBo;
import cn.edu.xmu.oomall.other.model.bo.UserBo;
import cn.edu.xmu.oomall.other.model.po.*;
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
import org.springframework.transaction.annotation.Transactional;

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
    private TimeSegmentPoMapper timeSegmentPoMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    public List<AdvertiseStatesRetVo> getAllAdvertiseState(){
        return Arrays.asList(AdvertiseBo.State.values()).stream()
                .map(x->new AdvertiseStatesRetVo(x.getCode(),x.getDescription())).collect(Collectors.toList());
    }

    @Transactional
    public ResponseCode setAdvertisementDefaultById(Long id){
        AdvertisementPo po = advertisementPoMapper.selectByPrimaryKey(id);
        if (po ==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(po.getBeDefault().equals((byte)1)){
            po.setBeDefault((byte)0);
            advertisementPoMapper.updateByPrimaryKey(po);
            return ResponseCode.OK;
        }
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria=example.createCriteria();
        criteria.andBeDefaultEqualTo((byte)1);
        List<AdvertisementPo> poList = advertisementPoMapper.selectByExample(example);
        for(AdvertisementPo oldDefaultPo:poList){
            po.setBeDefault((byte) 0);
            advertisementPoMapper.updateByPrimaryKeySelective(oldDefaultPo);
        }
        po.setBeDefault((byte) 1);
        advertisementPoMapper.updateByPrimaryKey(po);
        return ResponseCode.OK;
    }

    public ResponseCode updateAdvertisementById(AdvertiseBo advertiseBo){
        System.out.println("111");
        AdvertisementPo advertisementPo = advertiseBo.getAdvertisePo();
        System.out.println("222");
        System.out.println(advertisementPo.getBeginDate());
        advertisementPo.setGmtModified(LocalDateTime.now());
        try{
            advertisementPoMapper.updateByPrimaryKeySelective(advertisementPo);
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


    /*查询默认广告*/
    public List<AdvertiseBo> getDefaultAd(){
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria  criteria= example.createCriteria();
        criteria.andStateEqualTo(AdvertiseBo.State.NORM.getCode().byteValue()); //上架
        criteria.andBeDefaultEqualTo((byte)1); //默认
        List<AdvertisementPo> advertisementPoList=advertisementPoMapper.selectByExample(example);
        return advertisementPoList.stream().map(AdvertiseBo::new).collect(Collectors.toList());
    }

    /*查询当前时间段*/
    public TimeSegmentPo getNowTimeSegment(){
        TimeSegmentPoExample timeSegmentPoExample=new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria timeCriteria=timeSegmentPoExample.createCriteria();
        timeCriteria.andBeginTimeLessThan(LocalDateTime.now());
        timeCriteria.andEndTimeGreaterThan(LocalDateTime.now());
        timeCriteria.andTypeEqualTo(TimeSegmentBo.Type.ADS.getCode().byteValue());
        List<TimeSegmentPo> timePoList=timeSegmentPoMapper.selectByExample(timeSegmentPoExample);
        if(timePoList.size()==0)
            return null;
        else return timePoList.get(0);
    }

    /*根据segId获得广告 List*/
    public List<AdvertisementPo> getAdvertisements(Long segId) {
        AdvertisementPoExample example = new AdvertisementPoExample();
        AdvertisementPoExample.Criteria criteria=example.createCriteria();
        criteria.andSegIdEqualTo(segId);
        example.setOrderByClause("weight DESC");
        List<AdvertisementPo> advertisementPoList=advertisementPoMapper.selectByExample(example);
        if(advertisementPoList.size()<=8)
            return advertisementPoList;
        else
            return advertisementPoList.subList(0,8);
    }

    /*根据Id查广告*/
    public AdvertiseBo getAdvertiseById(Long id){
        AdvertisementPo po = advertisementPoMapper.selectByPrimaryKey(id);
        if(po == null)return null;
        return new AdvertiseBo(po);
    }

    /*根据segId获得广告 page*/
    public List<AdvertisementPo> getAdvertiseByTimeSegmentId(Long id, LocalDate beginDate, LocalDate endDate,Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize,true,true,null);
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
            return advertisementPoList;
        else
            return advertisementPoList.subList(0,8);
    }

    /*根据id查seg*/
    public TimeSegmentPo getTimeSeg(Long segId){
        TimeSegmentPo po = timeSegmentPoMapper.selectByPrimaryKey(segId);
        if(po == null)return null;
        return po;
    }


    public AdvertiseBo createAdvertiseInTimeSegId(Long segId, AdvertiseBo bo) {
        AdvertisementPo advertisementPo=bo.getAdvertisePo();
        advertisementPo.setSegId(segId);
        advertisementPoMapper.insertSelective(advertisementPo);
        return new AdvertiseBo(advertisementPo);
    }

    public AdvertiseBo addAdvertiseToSeg(Long segId, Long adId) {
        AdvertisementPo advertisementPo=new AdvertisementPo();
        advertisementPo.setId(adId);
        advertisementPo.setSegId(segId);
        advertisementPoMapper.updateByPrimaryKeySelective(advertisementPo);
        return new AdvertiseBo(advertisementPo);
    }
}
