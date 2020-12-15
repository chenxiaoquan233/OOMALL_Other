package cn.edu.xmu.oomall.other.service.provide;

import cn.edu.xmu.oomall.other.dao.UserDao;
import cn.edu.xmu.oomall.other.impl.IRebateService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author XQChen
 * @version 创建时间：2020/12/5 上午12:09
 */
@DubboService(version = "0.0.1-SNAPSHOT")
public class IRebateServiceImpl implements IRebateService {

    @Autowired
    private UserDao userDao;

    @Transactional
    public Integer useRebate(Long customerId, Integer num) {
        return userDao.useDebate(customerId, num);
    }
}
