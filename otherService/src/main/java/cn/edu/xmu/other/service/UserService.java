package cn.edu.xmu.other.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.UserDao;
import cn.edu.xmu.other.model.bo.UserBo;
import cn.edu.xmu.other.model.vo.User.UserLoginVo;
import cn.edu.xmu.other.model.vo.User.UserSignUpVo;
import cn.edu.xmu.other.otherCore.util.OtherJwtHelper;
import org.aspectj.bridge.IMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author XQChen
 * @version 创建时间：2020/11/24 上午8:21
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private OtherJwtHelper otherJwtHelper = new OtherJwtHelper();

    @Autowired
    private UserDao userDao;

    @Transactional
    public ReturnObject<VoObject> signUp(UserSignUpVo vo) {
        UserBo userBo = vo.createBo();
        userBo.setGmtCreate(LocalDateTime.now());

        ReturnObject<VoObject> returnObject = userDao.signUp(userBo);

        return returnObject;
    }

    @Transactional
    public ReturnObject<Object> login(UserLoginVo vo) {
        UserBo userBo = vo.createBo();

        ReturnObject<Object> returnObject = userDao.login(userBo);
        if(returnObject.getCode().equals(ResponseCode.OK)) {
            String token = otherJwtHelper.createToken(userBo.getId(), 200);
            return new ReturnObject<>(token);
        } else {
            return returnObject;
        }
    }

}
