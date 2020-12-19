package cn.edu.xmu.oomall.other.service.provide;

import cn.edu.xmu.oomall.other.dao.AddressDao;
import cn.edu.xmu.oomall.other.impl.IDubboRegionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "0.0.1-SNAPSHOT")
public class IDubboRegionServiceImpl implements IDubboRegionService {

    @Autowired
    private AddressDao addressDao;


    public Long getSuperiorRegionId(Long regionId){
        Long ret=addressDao.getParent(regionId);
        System.out.println("get region here, region:" + ret);
        return ret;
    }
}
