package cn.edu.xmu.oomall.other.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author XQChen
 * @version 创建时间：2020/12/9 下午8:33
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/time", produces = "application/json;charset=UTF-8")
public class TimeSegmentController {
    private static final Logger logger = LoggerFactory.getLogger(TimeSegmentController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @ApiOperation(value = "平台管理员新增广告时间段", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",         name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "timeSegementVo", name = "vo",            value = "起止时间",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 604, message = "时段冲突")
    })
    @PostMapping("/advertisement/timesegements")
    public Object addAdsTimeSegement() {
        return null;
    }

    @ApiOperation(value = "管理员获取广告时间段列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "page",          value = "页码"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "pageSize",      value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @GetMapping("/advertisement/timesegements")
    public Object getAllAdsTimeSegement() {
        return null;
    }

    @ApiOperation(value = "平台管理员新增秒杀时间段", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",         name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "timeSegementVo", name = "vo",            value = "起止时间",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 604, message = "时段冲突")
    })
    @PostMapping("/flashsale/timesegements")
    public Object addFlashsaleTimeSegement() {
        return null;
    }

    @ApiOperation(value = "管理员获取秒杀时间段列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "page",          value = "页码"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "pageSize",      value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @GetMapping("/advertisement/timesegements")
    public Object getAllFlashTimeSegement() {
        return null;
    }
}
