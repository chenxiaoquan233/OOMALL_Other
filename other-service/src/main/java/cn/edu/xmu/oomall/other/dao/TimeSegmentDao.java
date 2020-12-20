package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dto.TimeSegmentDTO;
import cn.edu.xmu.oomall.other.mapper.TimeSegmentPoMapper;
import cn.edu.xmu.oomall.other.model.bo.TimeSegmentBo;
import cn.edu.xmu.oomall.other.model.po.TimeSegmentPo;
import cn.edu.xmu.oomall.other.model.po.TimeSegmentPoExample;
import cn.edu.xmu.oomall.other.model.vo.TimeSegment.TimeSegmentVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        return timeSegmentPos.stream().map(x->new TimeSegmentDTO(x.getId(), x.getBeginTime(), x.getEndTime())).collect(Collectors.toList());
    }
    public List<TimeSegmentDTO> getAdsSegments(){
        return getTimeSegments((byte)0);
    }

    public List<TimeSegmentDTO> getFlashSaleSegments() {
        return getTimeSegments((byte)1);
    }

    public ReturnObject<PageInfo<VoObject>> getTimeSegment(byte type, int page, int pageSize){
        TimeSegmentPoExample timeSegmentPoExample=new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria=timeSegmentPoExample.createCriteria();
        criteria.andTypeEqualTo(type);
        List<TimeSegmentPo> timeSegmentPoList=null;
        PageHelper.startPage(page,pageSize,true,true,null);
        try{
            timeSegmentPoList=timeSegmentPoMapper.selectByExample(timeSegmentPoExample);
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        List<VoObject> ret = timeSegmentPoList.stream().map(TimeSegmentBo::new).collect(Collectors.toList());
        PageInfo<TimeSegmentPo> timeSegPoPage = PageInfo.of(timeSegmentPoList);
        PageInfo<VoObject> timeSegPage = new PageInfo<>(ret);
        timeSegPage.setPages(timeSegPoPage.getPages());
        timeSegPage.setPageNum(timeSegPoPage.getPageNum());
        timeSegPage.setPageSize(timeSegPoPage.getPageSize());
        timeSegPage.setTotal(timeSegPoPage.getTotal());
        return new ReturnObject<>(timeSegPage);
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
//        criteria.andBeginTimeLessThan(timeSegmentVo.getEndTime());
//        criteria.andEndTimeGreaterThan(timeSegmentVo.getBeginTime());
        criteria.andTypeEqualTo(type);
        Boolean flag=false;
        LocalTime beginTimeWithoutDate=timeSegmentVo.getBeginTime().toLocalTime();
        LocalTime endTimeWithoutDate=timeSegmentVo.getEndTime().toLocalTime();
        List<TimeSegmentPo> exsitedPoList=timeSegmentPoMapper.selectByExample(timeSegmentPoExample);
        for(TimeSegmentPo x:exsitedPoList){
            LocalTime beginTimeWithoutDate2=x.getBeginTime().toLocalTime();
            LocalTime endTimeWithoutDate2=x.getEndTime().toLocalTime();
            if(beginTimeWithoutDate2.isBefore(endTimeWithoutDate))
                if(endTimeWithoutDate2.isAfter(beginTimeWithoutDate)){
                    flag=true;
                    break;
                }
        }
        if(flag==false) {
            TimeSegmentPo newVal = new TimeSegmentPo();
            newVal.setBeginTime(timeSegmentVo.getBeginTime());
            newVal.setEndTime(timeSegmentVo.getEndTime());
            newVal.setGmtCreate(LocalDateTime.now());
            newVal.setType(type);
            timeSegmentPoMapper.insert(newVal);
            System.out.println("3");
            return new TimeSegmentBo(newVal);
        }else return null;
    }

    public TimeSegmentBo addAdsSegment(TimeSegmentVo timeSegmentVo){
        return addTimeSegment(timeSegmentVo,(byte)0);
    }
    public TimeSegmentBo addFlashSaleSegment(TimeSegmentVo timeSegmentVo){
        return addTimeSegment(timeSegmentVo,(byte)1);
    }

    private ResponseCode deleteTimeSegment(Long timeSegId, byte type){
        TimeSegmentPoExample timeSegmentPoExample=new TimeSegmentPoExample();
        TimeSegmentPoExample.Criteria criteria=timeSegmentPoExample.createCriteria();
        //criteria.andTypeEqualTo(type);
        criteria.andIdEqualTo(timeSegId);
        List<TimeSegmentPo> poList=timeSegmentPoMapper.selectByExample(timeSegmentPoExample);
        if(poList.size()==0)return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(poList.get(0).getType()!=type) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        else{
            timeSegmentPoMapper.deleteByPrimaryKey(poList.get(0).getId());
            return ResponseCode.OK;
        }
    }

    public ResponseCode deleteAdsSegment(Long timeSegId){
        return deleteTimeSegment(timeSegId,(byte)0);
    }

    public ResponseCode deleteFlashSaleSegment(Long timeSegId){
        return deleteTimeSegment(timeSegId,(byte)1);
    }
}
