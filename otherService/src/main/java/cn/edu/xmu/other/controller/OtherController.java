package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.model.vo.User.UserLoginVo;
import cn.edu.xmu.other.model.vo.User.UserModifyVo;
import cn.edu.xmu.other.model.vo.User.UserSignUpVo;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.other.otherCore.annotation.OtherAudit;
import cn.edu.xmu.other.otherCore.annotation.OtherLoginUser;
import cn.edu.xmu.other.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/other", produces = "application/json;charset=UTF-8")
public class OtherController {
    private static final Logger logger = LoggerFactory.getLogger(OtherController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private UserService userService;

    /***
     * 获得买家的所有状态
     * @return Object
     */
    @OtherAudit
    @GetMapping("/users/states")
    public Object getAllUserState (){
        return null;
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
    @PostMapping("/users")
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
    @GetMapping("/users")
    public Object getUserSelfInfo(@OtherLoginUser Long UserId) {
        ReturnObject<VoObject> returnObject = userService.findUserById(UserId);

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
    @OtherAudit
    @PutMapping("/users")
    public Object modifyUserSelfInfo(@OtherLoginUser Long userId, @Validated @RequestBody UserModifyVo vo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserModifyVo:" + vo);

            return object;
        }

        //ReturnObject<VoObject> returnObject = userService;
        return null;
    }

    /***
     * 用户修改密码
     * @return Object
     */
    @PutMapping("/users/password")
    public Object modifyUserSelfPassword() {
        return null;
    }

    /***
     * 用户重置密码
     * @return Object
     */
    @PutMapping("/users/password/reset")
    public Object resetUserSelfPassword() {
        return null;
    }

    /***
     * 平台管理员获取所有用户列表
     * @return Object
     */
    @GetMapping("/users/all")
    public Object getAllUser() {
        return null;
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
    @PostMapping("/users/login")
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
    @GetMapping("/users/logout")
    @OtherAudit
    public Object logoutUser(@OtherLoginUser Long id) {
        return ResponseUtil.ok();
    }

    /***
     * 管理员查看任意买家信息
     * @return Object
     */
    @ApiOperation(value = "用户登出", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer", name = "id",            value = "用户id",    required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @GetMapping("/users/{id}")
    @OtherAudit
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
    @PutMapping("/users/{id}/ban")
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
    @PutMapping("/users/{id}/release")
    public Object releaseUser(@LoginUser Long adminId, @PathVariable("id") Long id) {
        ResponseCode responseCode = userService.releaseUser(id);

        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }
    }
}
