package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.bo.TimeSegmentBo;
import cn.edu.xmu.oomall.other.model.vo.TimeSegment.TimeSegmentVo;
import cn.edu.xmu.oomall.other.service.TimeSegmentService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.Comment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author XQChen
 * @version 创建时间：2020/12/9 下午8:33
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class TimeSegmentController {
    private static final Logger logger = LoggerFactory.getLogger(TimeSegmentController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private TimeSegmentService timeSegmentService;

    @ApiOperation(value = "平台管理员新增广告时间段", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",         name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "timeSegmentVo",  name = "vo",            value = "起止时间",  required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Long",           name = "did",           value = "店ID")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 604, message = "时段冲突")
    })
    @PostMapping("/shops/{did}/advertisement/timesegments")
    @Audit
    public Object addAdsTimeSegment(@LoginUser Long userId, @RequestBody @Validated TimeSegmentVo timeSegmentVo, @PathVariable("did") Long shopId, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            return object;
        }
        LocalTime beginTimeWithoutDate=timeSegmentVo.getBeginTime().toLocalTime();
        LocalTime endTimeWithoutDate=timeSegmentVo.getEndTime().toLocalTime();
        if(beginTimeWithoutDate.isAfter(endTimeWithoutDate)){
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResponseUtil.fail(ResponseCode.Log_Bigger);
        }
        TimeSegmentBo bo=timeSegmentService.addAdsSegment(timeSegmentVo);
        if(bo==null){
            return ResponseUtil.fail(ResponseCode.TIMESEG_CONFLICT);
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
        return ResponseUtil.ok(bo.createVo());
    }


    @ApiOperation(value = "管理员获取广告时间段列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "page",          value = "页码"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "pageSize",      value = "每页数目"),
            @ApiImplicitParam(paramType = "path",   dataType = "Long",           name = "did",           value = "店ID")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @GetMapping("/shops/{did}/advertisement/timesegments")
    @Audit
    public Object getAllAdsTimeSegment(@LoginUser Long userId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,@PathVariable("did") Long shopId) {
        ReturnObject<PageInfo<VoObject>> returnObject = timeSegmentService.getAdsSegments(page==null?1:page, pageSize==null?10:pageSize);
        return Common.getPageRetObject(returnObject);
    }


    @ApiOperation(value = "平台管理员新增秒杀时间段", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",         name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "timeSegmentVo", name = "vo",            value = "起止时间",  required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Long",           name = "did",           value = "店ID")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 604, message = "时段冲突")
    })
    @PostMapping("/shops/{did}/flashsale/timesegments")
    @Audit
    public Object addFlashsaleTimeSegment(@LoginUser Long userId, @RequestBody @Validated TimeSegmentVo timeSegmentVo, @PathVariable("did") Long shopId, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            return object;
        }
        LocalTime beginTimeWithoutDate=timeSegmentVo.getBeginTime().toLocalTime();
        LocalTime endTimeWithoutDate=timeSegmentVo.getEndTime().toLocalTime();
        if(beginTimeWithoutDate.isAfter(endTimeWithoutDate)){
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResponseUtil.fail(ResponseCode.Log_Bigger);
        }
        TimeSegmentBo bo=timeSegmentService.addFlashSaleSegment(timeSegmentVo);
        if(bo==null){
            return ResponseUtil.fail(ResponseCode.TIMESEG_CONFLICT);
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
        return ResponseUtil.ok(bo.createVo());
    }

    @ApiOperation(value = "管理员获取秒杀时间段列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "page",          value = "页码"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "pageSize",      value = "每页数目"),
            @ApiImplicitParam(paramType = "path",   dataType = "Long",           name = "did",           value = "店ID")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @GetMapping("/shops/{did}/flashsale/timesegments")
    @Audit
    public Object getAllFlashTimeSegment(@LoginUser Long userId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,@PathVariable("did") Long shopId) {
        ReturnObject<PageInfo<VoObject>> returnObject = timeSegmentService.getFlashSaleSegments(page==null?1:page, pageSize==null?10:pageSize);
        return Common.getPageRetObject(returnObject);
    }

    @ApiOperation(value="平台管理员删除广告时间段", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",name="authorization",value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "did",value = "店ID"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "时段id")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @DeleteMapping("/shops/{did}/advertisement/timesegments/{id}")
    @Audit
    public Object deleteAdsSegmentById(@LoginUser Long userId ,@PathVariable("did") Long shopId,@PathVariable("id") Long timeSegId){
        ResponseCode responseCode = timeSegmentService.deleteAdsSegmentById(timeSegId);
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        }
        if(responseCode.equals(ResponseCode.RESOURCE_ID_OUTSCOPE))
        {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseUtil.fail(responseCode);
        }
        else {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }

    }


    @ApiOperation(value="平台管理员删除秒杀时间段", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",name="authorization",value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "did",value = "店ID"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "时段id")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @DeleteMapping("/shops/{did}/flashsale/timesegments/{id}")
    @Audit
    public Object deleteFlashSaleSegmentById(@LoginUser Long userId ,@PathVariable("did") Long shopId,@PathVariable("id") Long timeSegId){
        ResponseCode responseCode = timeSegmentService.deleteFlashSaleSegmentById(timeSegId);
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        }
        if(responseCode.equals(ResponseCode.RESOURCE_ID_OUTSCOPE))
        {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseUtil.fail(responseCode);
        }
        else {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
    }
}
