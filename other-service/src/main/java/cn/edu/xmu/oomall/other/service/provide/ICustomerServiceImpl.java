package cn.edu.xmu.oomall.other.service.provide;

import cn.edu.xmu.oomall.other.controller.UserController;
import cn.edu.xmu.oomall.other.dao.UserDao;
import cn.edu.xmu.oomall.other.dto.CustomerDTO;
import cn.edu.xmu.oomall.other.impl.ICustomerService;
import cn.edu.xmu.oomall.other.model.bo.UserBo;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author XQChen
 * @version 创建时间：2020/12/14 下午8:37
 */
@DubboService(version = "0.0.1-SNAPSHOT")
public class ICustomerServiceImpl implements ICustomerService {
    @Autowired
    private UserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Override
    public CustomerDTO getCustomer(Long customerId) {
        UserBo userBo = userDao.findUserById(customerId);
        if(userBo == null) return null;
        return userBo.createCustomer();
    }
}