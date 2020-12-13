package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.other.mapper.AdvertisementPoMapper;
import cn.edu.xmu.oomall.other.mapper.AftersalePoMapper;
import cn.edu.xmu.oomall.other.model.bo.AdvertiseBo;
import cn.edu.xmu.oomall.other.model.po.AdvertisementPo;
import cn.edu.xmu.oomall.other.model.po.AdvertisementPoExample;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseStatesRetVo;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    public ResponseCode setAdvertisementDefaultById(Integer did, Integer id){
        return  null;
    }



}
