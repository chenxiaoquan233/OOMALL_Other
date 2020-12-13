package cn.edu.xmu.oomall.other.service;


import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.other.dao.AdvertiseDao;
import cn.edu.xmu.oomall.other.dao.AftersaleDao;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseStatesRetVo;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/12 上午10:45
 */
@Service
public class AdvertiseService {
    private static final Logger logger = LoggerFactory.getLogger(AfterSaleService.class);

    @Autowired
    private AdvertiseDao advertiseDao;

    public List<AdvertiseStatesRetVo> getAllAdvertisementStates(){
        return advertiseDao.getAllAdvertiseState();
    }

    public ResponseCode setAdvertisementDefaultById(Integer did, Integer id){
        return advertiseDao.setAdvertisementDefaultById( did,  id);

    }
}
