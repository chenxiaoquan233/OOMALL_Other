package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.TimeSegmentDao;
import cn.edu.xmu.oomall.other.model.bo.TimeSegmentBo;
import cn.edu.xmu.oomall.other.model.vo.TimeSegment.TimeSegmentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/11 下午8:35
 */
@Service
public class TimeSegmentService {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    @Autowired
    private TimeSegmentDao timeSegmentDao;

    public ReturnObject addAdsSegment(TimeSegmentVo timeSegmentVo){
         TimeSegmentBo timeSegmentBo=timeSegmentDao.addAdsSegment(timeSegmentVo);
         if(timeSegmentBo==null)return new ReturnObject<>(ResponseCode.TIMESEG_CONFLICT);
         else return new ReturnObject<>(timeSegmentBo.createVo());
    }
}
