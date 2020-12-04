package cn.edu.xmu.oomall.other.service.OrderInterface;

import cn.edu.xmu.oomall.other.dao.UserDao;
import cn.edu.xmu.oomall.other.dto.Customer;
import cn.edu.xmu.oomall.other.model.bo.UserBo;
import cn.edu.xmu.oomall.other.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author XQChen
 * @version 创建时间：2020/12/4 下午10:47
 */
public class CustomerService implements ICustomerService {
    @Autowired
    private UserDao userDao;

    @Override
    public Customer getCustomer(Long customerId) {
        UserBo userBo = userDao.findUserById(customerId);
        if(userBo == null) return null;
        return userBo.createCustomer();
    }
}
