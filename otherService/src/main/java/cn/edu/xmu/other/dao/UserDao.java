package cn.edu.xmu.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.mapper.CustomerPoMapper;
import cn.edu.xmu.other.model.bo.UserBo;
import cn.edu.xmu.other.model.po.CustomerPo;
import cn.edu.xmu.other.model.po.CustomerPoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @author XQChen
 * @version 创建时间：2020/11/24 上午8:23
 */
@Repository
public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    private CustomerPoMapper customerPoMapper;

    public ReturnObject<VoObject> signUp(UserBo userBo) {
        CustomerPo customerPo = userBo.createUserPo();
        ReturnObject<VoObject> returnObject = null;

        CustomerPoExample example1 = new CustomerPoExample();
        CustomerPoExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andUserNameEqualTo(customerPo.getUserName());
        if(customerPoMapper.selectByExample(example1).size() != 0) {
            logger.debug("UserName registered:" + customerPo);
            return new ReturnObject<>(ResponseCode.USER_NAME_REGISTERED, "用户名已被注册");
        }

        CustomerPoExample example2 = new CustomerPoExample();
        CustomerPoExample.Criteria criteria2 = example2.createCriteria();
        criteria2.andMobileEqualTo(customerPo.getMobile());
        if(customerPoMapper.selectByExample(example2).size() != 0) {
            logger.debug("Mobile registered:" + customerPo);
            return new ReturnObject<>(ResponseCode.MOBILE_REGISTERED, "手机号已被注册");
        }

        CustomerPoExample example3 = new CustomerPoExample();
        CustomerPoExample.Criteria criteria3 = example3.createCriteria();
        criteria3.andEmailEqualTo(customerPo.getEmail());
        if(customerPoMapper.selectByExample(example3).size() != 0) {
            logger.debug("Email registered:" + customerPo);
            return new ReturnObject<>(ResponseCode.EMAIL_REGISTERED, "邮箱已被注册");
        }

        try {
            int ret = customerPoMapper.insertSelective(customerPo);
            if(ret == 0) {
                logger.debug("User sign up failed");
                logger.debug("UserPo:" + customerPo);
                logger.debug("result:" + ret);

                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("用户" + customerPo.getUserName() + "注册失败"));
            } else {
                logger.debug("User sign up success");
                logger.debug("UserPo:" + customerPo);
                userBo.setId(customerPo.getId());
                returnObject = new ReturnObject<>(userBo);
            }
        } catch (Exception e) {
            logger.debug("error info: " + e.getMessage());
            //if(Objects.requireNonNull(e.getMessage()).contains())
            returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return returnObject;
    }
}
