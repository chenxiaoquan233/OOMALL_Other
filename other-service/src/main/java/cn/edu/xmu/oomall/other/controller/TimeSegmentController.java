package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.oomall.other.model.vo.TimeSegment.TimeSegmentVo;
import cn.edu.xmu.oomall.other.service.TimeSegmentService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.Comment;

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

    @Autowired
    private TimeSegmentService timeSegmentService;

    @ApiOperation(value = "平台管理员新增广告时间段", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",         name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "timeSegmentVo", name = "vo",            value = "起止时间",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 604, message = "时段冲突")
    })
    @PostMapping("/advertisement/timesegments")
    public Object addAdsTimeSegment(@LoginUser Long userId, @RequestBody TimeSegmentVo timeSegmentVo) {
        return Common.getRetObject(timeSegmentService.addAdsSegment(timeSegmentVo));
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
    @GetMapping("/advertisement/timesegments")
    public Object getAllAdsTimeSegment() {
        return null;
    }

    @ApiOperation(value = "平台管理员新增秒杀时间段", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",         name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "timeSegmentVo", name = "vo",            value = "起止时间",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 604, message = "时段冲突")
    })
    @PostMapping("/flashsale/timesegments")
    public Object addFlashsaleTimeSegment() {
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
    @GetMapping("/flashsale/timesegments")
    public Object getAllFlashTimeSegment() {
        return null;
    }
}
