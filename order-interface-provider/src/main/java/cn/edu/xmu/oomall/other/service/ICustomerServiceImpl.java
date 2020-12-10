package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.other.dao.UserDao;
import cn.edu.xmu.oomall.other.dto.CustomerDTO;
import cn.edu.xmu.oomall.other.impl.ICustomerService;
import cn.edu.xmu.oomall.other.model.bo.UserBo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author XQChen
 * @version 创建时间：2020/12/10 下午7:26
 */
@DubboService(registry = {"provider2"})
public class ICustomerServiceImpl implements ICustomerService {
    @Autowired
    private UserDao userDao;

    @Override
    public CustomerDTO getCustomer(Long customerId) {
        UserBo userBo = userDao.findUserById(customerId);
        if(userBo == null) return null;
        return userBo.createCustomer();
    }
}
