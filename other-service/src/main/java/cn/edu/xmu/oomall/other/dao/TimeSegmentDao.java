package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.dto.TimeSegmentDTO;
import cn.edu.xmu.oomall.other.mapper.TimeSegmentPoMapper;
import cn.edu.xmu.oomall.other.model.bo.TimeSegmentBo;
import cn.edu.xmu.oomall.other.model.po.TimeSegmentPo;
import cn.edu.xmu.oomall.other.model.po.TimeSegmentPoExample;
import cn.edu.xmu.oomall.other.model.vo.TimeSegment.TimeSegmentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XQChen
 * @version 创建时间：2020/12/9 下午10:12
 */
@Repository
public class TimeSegmentDao {
    private static final Logger logger = LoggerFactory.getLogger(TimeSegmentDao.class);

    @Autowired
    private TimeSegmentPoMapper timeSegmentPoMapper;

    private List<TimeSegmentDTO> getTimeSegments(byte type){
        TimeSegmentPoExample timeSegmentPoExample = new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria = timeSegmentPoExample.createCriteria();
        criteria.andTypeEqualTo((byte) type);

        List<TimeSegmentPo> timeSegmentPos = timeSegmentPoMapper.selectByExample(timeSegmentPoExample);

        return timeSegmentPos.stream().map(x->new TimeSegmentDTO(x.getId(), x.getBeTime(), x.getEndTime())).collect(Collectors.toList());
    }
    public List<TimeSegmentDTO> getAdsSegments(){
        return getTimeSegments((byte)0);
    }
    public List<TimeSegmentDTO> getFlashSaleSegments() {
        return getTimeSegments((byte)1);
    }

    private TimeSegmentDTO getTimeSegmentById(Long id, byte type){
        TimeSegmentPo timeSegmentPo = timeSegmentPoMapper.selectByPrimaryKey(id);
        if(timeSegmentPo.getType().equals((byte) type))
            return new TimeSegmentDTO(timeSegmentPo.getId(), timeSegmentPo.getBeginTime(), timeSegmentPo.getEndTime());
        else
            return null;
    }

    public TimeSegmentDTO getAdsTimeSegmentById(Long id){
        return getTimeSegmentById(id,(byte)0);
    }
    public TimeSegmentDTO getFlashSaleSegmentById(Long id) {
        return  getTimeSegmentById(id,(byte)1);
    }


    private TimeSegmentBo addTimeSegment(TimeSegmentVo timeSegmentVo, byte type){
        TimeSegmentPoExample timeSegmentPoExample=new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria=timeSegmentPoExample.createCriteria();
        criteria.andBeginTimeLessThan(timeSegmentVo.getEndTime());
        criteria.andEndTimeGreaterThan(timeSegmentVo.getBeginTime());
        criteria.andTypeEqualTo(type);
        if(timeSegmentPoMapper.countByExample(timeSegmentPoExample)>0) {
            TimeSegmentPo newVal = new TimeSegmentPo();
            newVal.setBeginTime(timeSegmentVo.getBeginTime());
            newVal.setEndTime(timeSegmentVo.getEndTime());
            newVal.setType(type);
            timeSegmentPoMapper.insert(newVal);
            return new TimeSegmentBo(newVal);
        }else return null;
    }
    public TimeSegmentBo addAdsSegment(TimeSegmentVo timeSegmentVo){
        return addTimeSegment(timeSegmentVo,(byte)0);
    }
    public TimeSegmentBo addFlashSaleSegment(TimeSegmentVo timeSegmentVo){
        return addTimeSegment(timeSegmentVo,(byte)1);
    }

}
