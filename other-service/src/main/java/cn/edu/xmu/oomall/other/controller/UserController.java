package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.bo.UserBo;
import cn.edu.xmu.oomall.other.model.vo.User.*;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.oomall.other.service.UserService;
import cn.edu.xmu.oomall.other.util.IpUtil;
import cn.xmu.edu.goods.client.IGoodsService;
import cn.xmu.edu.goods.client.IShopService;
import cn.xmu.edu.goods.client.dubbo.ShopDTO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/users", produces = "application/json;charset=UTF-8")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private UserService userService;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    private IShopService shopService;

    @DubboReference(version = "0.0.1-SNAPSHOT")
    private IGoodsService goodsService;

    /***
     * 获得买家的所有状态
     * @return Object
     */
    @Audit
    @GetMapping("/states")
    public Object getAllUserState (){
        List<UserStateRetVo> states = Arrays.stream(UserBo.State.values()).map(UserBo.State::getCode).map(UserStateRetVo::new).collect(Collectors.toList());
        return ResponseUtil.ok(states);
    }

    /***
     * 注册用户
     * @param vo 用户视图
     * @param bindingResult 校验错误
     * @return Object 用户返回视图
     */
    @ApiOperation(value = "注册用户", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserSignUpVo", name = "vo", value = "可填写的用户信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 731, message = "用户名已被注册"),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 733, message = "电话已被注册")
    })
    @PostMapping("")
    public Object signUpUser(@Validated @RequestBody UserSignUpVo vo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserSignUpVo:" + vo);

            return object;
        }

        ReturnObject<VoObject> returnObject = userService.signUp(vo);
        if(returnObject.getCode().equals(ResponseCode.OK)) {
            Object returnVo = returnObject.getData().createVo();
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            logger.debug("User:" + vo.getUserName() + "signup success");
            return Common.decorateReturnObject(new ReturnObject(returnVo));
        } else {
            logger.debug("User:" + vo.getUserName() + "signup failed");
            return Common.getRetObject(returnObject);
        }
    }

    /***
     * 买家查看自己信息
     * @param UserId 用户id
     * @return Object
     */
    @ApiOperation(value = "买家查看自己信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping(value = "")
    public Object getUserSelfInfo(@LoginUser Long UserId) {
        ReturnObject<VoObject> returnObject = userService.findUserById(UserId);

        logger.debug(returnObject.getData().createVo().toString());

        return Common.getRetObject(returnObject);
    }

    /***
     * 买家修改自己的信息
     * @param userId 用户id
     * @return Object
     */
    @ApiOperation(value = "买家修改自己信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserModifyVo", name = "vo", value = "可修改的用户信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("")
    public Object modifyUserSelfInfo(@LoginUser Long userId, @Validated @RequestBody UserModifyVo vo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserModifyVo:" + vo);

            return object;
        }

        ResponseCode responseCode = userService.modifyUserById(userId, vo);
        if(responseCode.equals(ResponseCode.OK)) return ResponseUtil.ok();
        else return ResponseUtil.fail(responseCode);
    }

    /***
     * 用户修改密码
     * @return Object
     */
    @PutMapping("/password")
    public Object modifyUserSelfPassword() {
        return null;
    }

    /***
     * 用户重置密码
     * @return Object
     */
    @ApiOperation(value = "用户重置密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserResetPasswordVo", name = "vo", value = "邮箱和用户名", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 745, message = "与系统预留的邮箱不一致"),
            @ApiResponse(code = 746, message = "与系统预留的电话不一致")
    })
    @PutMapping("/password/reset")
    public Object resetUserSelfPassword(
            @RequestBody UserResetPasswordVo vo,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest) {



        /* 处理参数校验错误 */
        Object o = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(o != null){
            return o;
        }

        logger.debug("email:" + vo.getEmail() + "userName:" + vo.getUserName());

        String ip = IpUtil.getIpAddr(httpServletRequest);

        ReturnObject returnObject = userService.resetPassword(vo,ip);
        return Common.decorateReturnObject(returnObject);
    }

    /***
     * 平台管理员获取所有用户列表
     * @return Object
     */
    @ApiOperation(value = "平台管理员获取所有用户列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "userName",      value = "testuser"),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "email",         value = "test@test.com"),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "mobile",        value = "12300010002"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "page",          value = "1"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "pageSize",      value = "10")
    })
    @GetMapping("/all")
    @Audit
    public Object getAllUser(
            @LoginUser    Long    adminId,
            @RequestParam String  userName,
            @RequestParam String  email,
            @RequestParam String  mobile,
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ) {
        Object object = null;

        if(page <= 0 || pageSize <= 0) {
            object = Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        } else {
            ReturnObject<PageInfo<VoObject>> returnObject = userService.getAllUsers(userName, email, mobile, page, pageSize);
            logger.debug("fingUserById: getUsers = " + returnObject);
            object = Common.getPageRetObject(returnObject);
        }

        return object;
    }

    /***
     * 用户名密码登录
     * @param vo 用户视图
     * @param bindingResult 校验错误
     * @return Object
     */
    @ApiOperation(value = "用户名密码登录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserLoginVo", name = "vo", value = "用户名和密码", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或密码错误"),
            @ApiResponse(code = 702, message = "用户被禁止登录")
    })
    @PostMapping("/login")
    public Object loginUser(@Validated @RequestBody UserLoginVo vo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserLoginVo:" + vo);

            return object;
        }

        ReturnObject<Object> returnObject = userService.login(vo);
        if(returnObject.getCode().equals(ResponseCode.OK)) {
            logger.debug("User login success");
            logger.debug("token:" + returnObject.getData());

            return ResponseUtil.ok(returnObject.getData());
        } else {
            logger.debug("User login failed");
            logger.debug("error:" + returnObject.getCode().getMessage());

            return ResponseUtil.fail(returnObject.getCode());
        }

    }

    /***
     * 用户登出
     * @return Object
     */
    @ApiOperation(value = "用户登出", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @GetMapping("/logout")
    @Audit
    public Object logoutUser(@LoginUser Long id) {
        return ResponseUtil.ok();
    }

    /***
     * 管理员查看任意买家信息
     * @return Object
     */
    @ApiOperation(value = "查找特定用户信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer", name = "id",            value = "用户id",    required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @GetMapping("/{id}")
    @Audit
    public Object findUserInfo(@LoginUser Long adminId, @PathVariable("id") Long id) {
        return Common.getRetObject(userService.findUserById(id));
    }

    /***
     * 平台管理员封禁买家
     * @return Object
     */
    @ApiOperation(value = "平台管理员封禁买家", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer", name = "id",            value = "用户id",    required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @Audit
    @PutMapping("/{id}/ban")
    public Object banUser(@LoginUser Long adminId, @PathVariable("id") Long id) {
        ResponseCode responseCode = userService.banUser(id);

        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }

    }

    /***
     * 平台管理员解禁买家
     * @return Object
     */
    @ApiOperation(value = "平台管理员解禁买家", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer", name = "id",            value = "用户id",    required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @Audit
    @PutMapping("/{id}/release")
    public Object releaseUser(@LoginUser Long adminId, @PathVariable("id") Long id) {
        ResponseCode responseCode = userService.releaseUser(id);

        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }
    }

    @GetMapping("/test")
    public Object test() {
        ShopDTO shop = shopService.getShopById(0L);
        goodsService.getPrice(1L);
        return new ReturnObject<>(shop);
    }
}
