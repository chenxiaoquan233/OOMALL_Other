package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.oomall.other.mapper.CustomerPoMapper;
import cn.edu.xmu.oomall.other.model.bo.UserBo;
import cn.edu.xmu.oomall.other.model.po.CustomerPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author XQChen
 * @version 创建时间：2020/12/10 下午7:25
 */
@Repository
public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    private CustomerPoMapper customerPoMapper;

    public UserBo findUserById(Long id) {
        CustomerPo customerPo = customerPoMapper.selectByPrimaryKey(id);

        if(customerPo == null) {
            logger.debug("not found user, id:" + id);
            return null;
        } else {
            logger.debug("customerPo: " + customerPo.getId());
            return new UserBo(customerPo);
        }
    }
}
