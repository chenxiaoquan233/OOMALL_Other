package cn.edu.xmu.oomall.other.service.OrderInterface;

import cn.edu.xmu.oomall.other.dao.UserDao;
import cn.edu.xmu.oomall.other.service.IRebateService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author XQChen
 * @version 创建时间：2020/12/5 上午12:09
 */
public class RebateService implements IRebateService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer useRebate(Long customerId, Integer num) {
        return userDao.useDebate(customerId, num);
    }
}
