package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.dto.TimeSegmentDTO;
import cn.edu.xmu.oomall.other.mapper.TimeSegmentPoMapper;
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

    public List<TimeSegmentDTO> getFlashSaleSegments() {
        TimeSegmentPoExample timeSegmentPoExample = new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria = timeSegmentPoExample.createCriteria();
        criteria.andTypeEqualTo((byte) 1);

        List<TimeSegmentPo> timeSegmentPos = timeSegmentPoMapper.selectByExample(timeSegmentPoExample);

        return timeSegmentPos.stream().map(x->new TimeSegmentDTO(x.getId(), x.getBeginTime(), x.getEndTime())).collect(Collectors.toList());
    }

    public TimeSegmentDTO getFlashSaleSegmentById(Long id) {
        TimeSegmentPo timeSegmentPo = timeSegmentPoMapper.selectByPrimaryKey(id);
        if(timeSegmentPo.getType().equals((byte) 1))
            return new TimeSegmentDTO(timeSegmentPo.getId(), timeSegmentPo.getBeginTime(), timeSegmentPo.getEndTime());
        else
            return null;
    }

    //public ReturnObject<VoObject> addTimeSegments(TimeSegmentVo)
}
