package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.bo.AdvertiseBo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseAuditVo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseVo;
import cn.edu.xmu.oomall.other.service.AdvertiseService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/12 上午10:44
 */
@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class AdvertiseController {
    @Autowired
    private HttpServletResponse httpServletResponse;

    private static final Logger logger = LoggerFactory.getLogger(AdvertiseController.class);

    @Autowired
    private AdvertiseService advertiseService;

    @ApiOperation(value = "获得广告的所有状态", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @GetMapping("/advertisement/states")
    public Object getAdvertisementStates(){
          return ResponseUtil.ok(advertiseService.getAllAdvertisementStates());
    }

    @ApiOperation(value = "管理员设置默认广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Long",           name = "did",           value = "店ID"),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/shops/{did}/advertisement/{id}/default")
    public Object setAdvertisementDefaultById(@LoginUser Long user, @PathVariable("did") Long did, @PathVariable("id") Long id){
        ResponseCode responseCode=advertiseService.setAdvertisementDefaultById(id);
        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST))
        {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(responseCode);
        }
        return ResponseUtil.ok();
    }

    @ApiOperation(value = "管理员修改广告内容", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AdvertiseVo", name = "vo",  value = "可修改的广告信息",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/shops/{did}/advertisement/{id}")
    public Object updateAdvertisementById(@LoginUser Long user, @PathVariable("id") Long id, @RequestBody @Validated AdvertiseVo advertiseVo, BindingResult bindingResult){
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserSignUpVo:" + advertiseVo);
            return object;
        }
        if(advertiseVo.getBeginDate()!=null&&advertiseVo.getEndDate()!=null){
            if(advertiseVo.getBeginDate().isAfter(advertiseVo.getEndDate())){
                httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                return ResponseUtil.fail(ResponseCode.Log_Bigger);
            }
        }
        ResponseCode responseCode=advertiseService.updateAdvertisementById(id,advertiseVo);
        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(responseCode);
        }
        if(responseCode.equals(ResponseCode.Log_Bigger)){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseUtil.fail(responseCode);
        }
        else {
            return ResponseUtil.ok();
        }
    }

    @ApiOperation(value = "管理员删除某一个广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @DeleteMapping("/shops/{did}/advertisement/{id}")
    public Object deleteAdvertisementById(@LoginUser Long user, @PathVariable("id") Long id){
        ResponseCode responseCode= advertiseService.deleteAdvertisementById(id);
        if(responseCode.equals(ResponseCode.OK))
            return ResponseUtil.ok();
        else {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(responseCode);
        }
    }


    @ApiOperation(value = "获取当前时段广告列表", produces = "application/json")
    @ApiImplicitParams({})
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @GetMapping("/advertisement/current")
    public Object getCurrentAdvertisements(){
        List<AdvertiseBo> bo=advertiseService.getCurrentAdvertisements();
        return ResponseUtil.ok(bo.stream().map(x->x.createVo()));
    }



    @ApiOperation(value = "管理员上传广告图片", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true),
            @ApiImplicitParam(paramType = "formData", dataType = "file", name = "img", value = "文件", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PostMapping("/shops/{did}/advertisement/{id}/uploadImg")
    public Object uploadAdvertisementImgById(@LoginUser Long user, @PathVariable("id") Long id, @RequestParam("img")MultipartFile multipartFile){
        ResponseCode responseCode=advertiseService.uploadAdvertiseImgById(id,multipartFile);
        if(responseCode.equals(ResponseCode.OK)){
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return ResponseUtil.ok();
        }

        else {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(responseCode);
        }
    }



    @ApiOperation(value = "管理员上架广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 608,   message = "广告状态禁止")
    })
    @Audit
    @PutMapping("/shops/{did}/advertisement/{id}/onshelves")
    public Object onshelvesAdvertisementById(@LoginUser Long user, @PathVariable("id") Long id){
        ResponseCode responseCode=advertiseService.onshelvesAdvertisementById(id);
        if(responseCode.equals(ResponseCode.OK))
            ResponseUtil.ok();
        else if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(responseCode);
        }
        return ResponseUtil.fail(responseCode);
    }


    @ApiOperation(value = "管理员下架广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 608,   message = "广告状态禁止")
    })
    @Audit
    @PutMapping("/shops/{did}/advertisement/{id}/offshelves")
    public Object offshelvesAdvertisementById(@LoginUser Long user, @PathVariable("id") Long id){
        ResponseCode responseCode=advertiseService.offshelvesAdvertisementById(id);
        if(responseCode.equals(ResponseCode.OK))
            ResponseUtil.ok();
        else if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(responseCode);
        }
        return ResponseUtil.fail(responseCode);
    }


    @ApiOperation(value = "管理员审核广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code = 608,   message = "广告状态禁止")
    })
    @Audit
    @PutMapping("/shops/{did}/advertisement/{id}/audit")
    public Object auditAdvertisementById(@LoginUser Long user, @PathVariable("id") Long id,
                                         @Validated @RequestBody AdvertiseAuditVo Vo, BindingResult bindingResult){
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserSignUpVo:" + Vo);
            return object;
        }
        ResponseCode responseCode=advertiseService.auditAdvertisementById(id,Vo);
        if(responseCode.equals(ResponseCode.OK))
            ResponseUtil.ok();
        else if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(responseCode);
        }
        return ResponseUtil.fail(responseCode);
    }


    @ApiOperation(value = "管理员查看某一个广告时间段的广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "did", value = "店ID", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginDate", value = "广告开始日期"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endDate", value = "广告结束日期")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/shops/{did}/timesegments/{id}/advertisement")
    public Object getAdvertisementsByTimeSegmentId(@LoginUser Long user,@PathVariable("did") Long did, @PathVariable("id") Long id,
                                                   @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate beginDate,
                                                   @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate,
                                                   @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize){
        ReturnObject<PageInfo<VoObject>> ret=advertiseService.getAdvertiseByTimeSegmentId(id, beginDate, endDate,page,pageSize);
        if(ret==null){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return Common.getPageRetObject(ret);
    }


    @ApiOperation(value = "管理员在广告时段下新建广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "did", value = "店ID", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AdvertiseVo", name = "vo",            value = "可修改的广告信息",  required = true)

    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
    })
    @Audit
    @PostMapping("/shops/{did}/timesegments/{id}/advertisement")
    public Object createAdvertisementsByTimeSegmentId(@LoginUser Long user,  @PathVariable("did") Long did, @PathVariable("id") Long id, @Validated @RequestBody AdvertiseVo advertiseVo,BindingResult bindingResult){
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseUtil.fail(ResponseCode.FIELD_NOTVALID);
        }
        if(advertiseVo.getBeginDate().isAfter(advertiseVo.getEndDate())){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseUtil.fail(ResponseCode.Log_Bigger);
        }
        Object ret=advertiseService.createAdvertiseByTimeSegId(id,advertiseVo);
        if(ret.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return ResponseUtil.ok(ret);
    }


    @ApiOperation(value = "管理员在广告时段下增加广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "did", value = "店ID", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "tid", value = "广告时间段id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true)

    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
    })
    @Audit
    @PostMapping("/shops/{did}/timesegments/{tid}/advertisement/{id}")
    public Object createAdvertisementsByTimeSegmentIdAndId(@LoginUser Long user, @PathVariable("did") Long did, @PathVariable("tid") Long tid, @PathVariable("id") Long id){
        Object ret=advertiseService.addAdvertiseToSeg(tid,id);
        if(ret.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return ResponseUtil.ok(ret);
    }
}
