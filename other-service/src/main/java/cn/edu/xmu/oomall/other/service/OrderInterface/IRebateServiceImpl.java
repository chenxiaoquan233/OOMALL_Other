package cn.edu.xmu.oomall.other.service.OrderInterface;

import cn.edu.xmu.oomall.other.dao.UserDao;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author XQChen
 * @version 创建时间：2020/12/5 上午12:09
 */
@DubboService(registry = {"provider1"})
public class IRebateServiceImpl{

    @Autowired
    private UserDao userDao;

    @Transactional
    public Integer useRebate(Long customerId, Integer num) {
        return userDao.useDebate(customerId, num);
    }
}
