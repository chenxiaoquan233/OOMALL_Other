package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.CustomerPoMapper;
import cn.edu.xmu.oomall.other.model.bo.UserBo;
import cn.edu.xmu.oomall.other.model.po.CustomerPo;
import cn.edu.xmu.oomall.other.model.po.CustomerPoExample;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public ReturnObject<Object> login(UserBo userBo) {
        CustomerPo customerPo = userBo.createUserPo();
        ReturnObject<VoObject> returnObject = null;

        CustomerPoExample example = new CustomerPoExample();
        CustomerPoExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(customerPo.getUserName());

        if(customerPoMapper.selectByExample(example).size() == 0) {
            logger.debug("User not exist:" + customerPo);
            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        }

        CustomerPo returnCustomerPo = customerPoMapper.selectByExample(example).get(0);

        if(returnCustomerPo.getPassword().contentEquals(userBo.getPassword())) {
            if(UserBo.State.getTypeByCode(Integer.valueOf(returnCustomerPo.getState())).equals(UserBo.State.NORM)) {
                logger.debug("User login success");
                logger.debug("Username:" + returnCustomerPo.getUserName());

                userBo.setId(returnCustomerPo.getId());

                return new ReturnObject<>(returnCustomerPo);
            } else {
                logger.debug("User is forbidden");
                logger.debug("Username:" + returnCustomerPo.getUserName());

                return new ReturnObject<>(ResponseCode.AUTH_USER_FORBIDDEN);
            }
        } else {
            logger.debug("Wrong login password, username:" + userBo.getUserName());

            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        }
    }

    public UserBo findUserById(Long id) {
        CustomerPo customerPo = customerPoMapper.selectByPrimaryKey(id);

        if(customerPo == null) {
            logger.debug("not found user, id:" + id);

            return null;
        } else {
            return new UserBo(customerPo);
        }
    }

    public Boolean switchUserStateById(Long id, Byte state) {
        CustomerPo customerPo = customerPoMapper.selectByPrimaryKey(id);

        if(customerPo == null) {
            logger.debug("user not find, id:" + id);

            return false;
        }

        customerPo.setState(state);
        customerPoMapper.updateByPrimaryKey(customerPo);
        return true;
    }

    public ResponseCode updateUser(UserBo userBo) {
        CustomerPo customerPo = userBo.createUserPo();

        logger.debug("userID: " + userBo.getId());
        customerPoMapper.updateByPrimaryKey(customerPo);

        return ResponseCode.OK;
    }

    public Integer useDebate(Long userId, Integer num) {
        CustomerPo customerPo = customerPoMapper.selectByPrimaryKey(userId);

        if(customerPo == null) return null;

        int point = customerPo.getPoint();
        if(point >= num) {
            customerPo.setPoint(point - num);
            customerPoMapper.updateByPrimaryKey(customerPo);
            return num;
        } else {
            customerPo.setPoint(0);
            customerPoMapper.updateByPrimaryKey(customerPo);
            return point;
        }
    }

    public PageInfo<CustomerPo> getAllUsers(String userName, String email, String mobile) {
        CustomerPoExample example = new CustomerPoExample();
        CustomerPoExample.Criteria criteria = example.createCriteria();

        if(!userName.isBlank()) criteria.andUserNameEqualTo(userName);
        if(!email.isBlank()) criteria.andEmailEqualTo(email);
        if(!mobile.isBlank()) criteria.andMobileEqualTo(mobile);

        List<CustomerPo> customers = customerPoMapper.selectByExample(example);

        logger.debug("getUserById: retUsers = " + customers);

        return new PageInfo<>(customers);
    }
}
