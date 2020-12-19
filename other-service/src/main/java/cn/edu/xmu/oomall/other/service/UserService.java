package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.RandomCaptcha;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.UserDao;
import cn.edu.xmu.oomall.other.model.bo.UserBo;
import cn.edu.xmu.oomall.other.model.po.CustomerPo;
import cn.edu.xmu.oomall.other.model.vo.User.*;
import cn.edu.xmu.oomall.other.util.MailUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author XQChen
 * @version 创建时间：2020/11/24 上午8:21
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private JwtHelper jwtHelper = new JwtHelper();

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    private final MailUtil mailUtil = new MailUtil();

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
            String token = jwtHelper.createToken(userBo.getId(), -2L,1000000);
            return new ReturnObject<>(token);
        } else {
            return returnObject;
        }
    }

    public ReturnObject<VoObject> findUserById(Long id) {
        UserBo userBo = userDao.findUserById(id);
        if(userBo == null) {
            logger.debug("not found user, id:" + id);

            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            return new ReturnObject<>(userBo);
        }
    }

    @Transactional
    public ResponseCode banUser(Long id) {
        if(userDao.switchUserStateById(id, UserBo.State.FORBID.getCode().byteValue())) {
            return ResponseCode.OK;
        } else {
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        }
    }

    @Transactional
    public ResponseCode releaseUser(Long id) {
        if(userDao.switchUserStateById(id, UserBo.State.NORM.getCode().byteValue())) {
            return ResponseCode.OK;
        } else {
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        }
    }

    @Transactional
    public ResponseCode modifyUserById(Long userId, UserModifyVo vo) {
        UserBo userBo = userDao.findUserById(userId);

        if(userBo == null) {
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        }

        logger.debug("realname:" + vo.getRealName());
        logger.debug("gender:" + vo.getGender());
        logger.debug("birthday:" + vo.getBirthday());

        if(!(vo.getRealName().isBlank())) userBo.setRealName(vo.getRealName());
        if(!(vo.getGender() == null) && !(vo.getGender().isBlank())) userBo.setGender(UserBo.Gender.getTypeByCode(Integer.valueOf(vo.getGender())));
        if(!(vo.getBirthday() == null)) userBo.setBirthday(vo.getBirthday());

        logger.debug("userId:" + userBo.getId());

        ResponseCode responseCode = userDao.updateUser(userBo);
        return responseCode;
    }

    public ReturnObject<PageInfo<VoObject>> getAllUsers(String userName, String email, String mobile, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);
        PageInfo<CustomerPo> customerPos = userDao.getAllUsers(userName, email, mobile);

        List<VoObject> users = customerPos.getList().stream().map(UserBo::new).collect(Collectors.toList());

        PageInfo<VoObject> returnObject = new PageInfo<>(users);
        returnObject.setPages(customerPos.getPages());
        returnObject.setPageNum(customerPos.getPageNum());
        returnObject.setPageSize(customerPos.getPageSize());
        returnObject.setTotal(customerPos.getTotal());

        return new ReturnObject<>(returnObject);
    }

    @Transactional
    public ResponseCode resetPassword(UserResetPasswordVo vo, String ip) {

        //防止重复请求验证码
        if(redisTemplate.hasKey("ip_" + ip))
            return ResponseCode.AUTH_USER_FORBIDDEN;
        else {
            //1 min中内不能重复请求
            redisTemplate.opsForValue().set("ip_"+ip,ip);
            redisTemplate.expire("ip_" + ip, 0, TimeUnit.MILLISECONDS);
        }

        UserBo userBo = vo.createUserBo();
        ResponseCode responseCode = userDao.resetPassword(userBo);

        if(!responseCode.equals(ResponseCode.OK)) return responseCode;

        //随机生成验证码
        String captcha = RandomCaptcha.getRandomString(6);
        while(redisTemplate.hasKey(captcha))
            captcha = RandomCaptcha.getRandomString(6);

        String id = userBo.getId().toString();
        String key = "cp_" + captcha;
        //key:验证码,value:id存入redis
        redisTemplate.opsForValue().set(key,id);
        //五分钟后过期
        redisTemplate.expire(key, 5 * 60 * 1000, TimeUnit.MILLISECONDS);

        return mailUtil.sendEmail(userBo.getEmail(), userBo.getUserName(), "您的验证码是：" + captcha + "，5分钟内有效。");
    }

    @Transactional
    public ResponseCode modifyUserSelfPassword(UserModifyPasswordVo vo) {
        if(!redisTemplate.hasKey("cp_" + vo.getCaptcha())) return ResponseCode.AUTH_INVALID_ACCOUNT;

        Long id = Long.valueOf(redisTemplate.opsForValue().get("cp_" + vo.getCaptcha()).toString());

        UserBo userBo = userDao.findUserById(id);

        if(userBo.getPassword().equals(vo.getNewPassword())) return ResponseCode.PASSWORD_SAME;

        userBo.setPassword(vo.getNewPassword());

        userDao.updateUser(userBo);

        return ResponseCode.OK;
    }
}
