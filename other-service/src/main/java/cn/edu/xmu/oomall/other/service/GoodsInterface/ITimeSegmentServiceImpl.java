package cn.edu.xmu.oomall.other.service.GoodsInterface;

import cn.edu.xmu.oomall.dto.TimeSegmentDTO;
import cn.edu.xmu.oomall.impl.ITimeSegmentService;
import cn.edu.xmu.oomall.other.dao.TimeSegmentDao;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author XQChen
 * @version 创建时间：2020/12/9 下午10:12
 */
@DubboService(registry = {"provider2"})
public class ITimeSegmentServiceImpl implements ITimeSegmentService {
    @Autowired
    private TimeSegmentDao timeSegmentDao;

    @Override
    public List<TimeSegmentDTO> getFlashSaleSegments() {
        return timeSegmentDao.getFlashSaleSegments();
    }

    @Override
    public TimeSegmentDTO getFlashSaleSegmentById(Long id) {
        return timeSegmentDao.getFlashSaleSegmentById(id);
    }
}
