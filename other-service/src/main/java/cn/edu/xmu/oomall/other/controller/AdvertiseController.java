package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseVo;
import cn.edu.xmu.oomall.other.service.AdvertiseService;
import cn.edu.xmu.oomall.other.service.AfterSaleService;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/12 上午10:44
 */
@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class AdvertiseController {
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
    @Audit
    @GetMapping("/advertisement/states")
    public Object getAdvertisementStates(@LoginUser Long userId){
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
    public Object setAdvertisementDefaultById(@LoginUser Long user, @PathVariable("did") Integer did, @PathVariable("id") Integer id){
        ResponseCode responseCode=advertiseService.setAdvertisementDefaultById(id);
        if(responseCode.equals(ResponseCode.OK))
            return ResponseUtil.ok();
        else return ResponseUtil.fail(responseCode);
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
    public Object updateAdvertisementById(@LoginUser Long user, @PathVariable("id") Integer id, @RequestBody AdvertiseVo advertiseVo){
        ResponseCode responseCode=advertiseService.updateAdvertisementById(advertiseVo);
        if(responseCode.equals(ResponseCode.OK))
            return ResponseUtil.ok();
        else return ResponseUtil.fail(responseCode);
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
    public Object deleteAdvertisementById(@LoginUser Long user, @PathVariable("id") Integer id){
        ResponseCode responseCode=advertiseService.deleteAdvertisementById(id);
        if(responseCode.equals(ResponseCode.OK))
            return ResponseUtil.ok();
        else return ResponseUtil.fail(responseCode);
    }


    @ApiOperation(value = "获取当前时段广告列表", produces = "application/json")
    @ApiImplicitParams({})
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @GetMapping("/advertisement/current")
    public Object getCurrentAdvertisements(){
        return ResponseUtil.ok(advertiseService.getCurrentAdvertisements());
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
    public Object uploadAdvertisementImgById(@LoginUser Long user, @PathVariable("id") Integer id, @RequestParam("img")MultipartFile multipartFile){
        ResponseCode responseCode=advertiseService.uploadAdvertiseImgById(id,multipartFile);
        if(responseCode.equals(ResponseCode.OK))
            return ResponseUtil.ok();
        else return ResponseUtil.fail(responseCode);
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
    public Object onshelvesAdvertisementById(@LoginUser Long user, @PathVariable("id") Integer id){
        ResponseCode responseCode=advertiseService.onshelvesAdvertisementById(id);
        if(responseCode.equals(ResponseCode.OK))
            return ResponseUtil.ok();
        else return ResponseUtil.fail(responseCode);
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
    public Object offshelvesAdvertisementById(@LoginUser Long user, @PathVariable("id") Integer id){
        ResponseCode responseCode=advertiseService.offshelvesAdvertisementById(id);
        if(responseCode.equals(ResponseCode.OK))
            return ResponseUtil.ok();
        else return ResponseUtil.fail(responseCode);
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
    public Object auditAdvertisementById(@LoginUser Long user, @PathVariable("id") Integer id){
        ResponseCode responseCode=advertiseService.auditAdvertisementById(id);
        if(responseCode.equals(ResponseCode.OK))
            return ResponseUtil.ok();
        else return ResponseUtil.fail(responseCode);
    }


    @ApiOperation(value = "管理员查看某一个广告时间段的广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "did", value = "店ID", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginDate", value = "广告开始日期"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endDate", value = "广告结束日期"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/shops/{did}/timesegments/{id}/advertisement")
    public Object getAdvertisementsByTimeSegmentId(@LoginUser Long user,@PathVariable("did") Integer did, @PathVariable("id") Integer id,
                                                   @RequestParam(required = false) String beginDate, @RequestParam(required = false) String endDate,
                                                   @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize){
        // 进来的query是String的LocalDate我贞德找不到例子了QAQ
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate beginDate_,endDate_;
        try {
            beginDate_ = LocalDate.parse(beginDate, dateFormatter);
            endDate_ = LocalDate.parse(endDate,dateFormatter);
        }catch (Exception e){
            return ResponseUtil.fail(ResponseCode.INTERNAL_SERVER_ERR);
        }
        return advertiseService.getAdvertiseByTimeSegmentId(id,beginDate_,endDate_,page,pageSize);
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
            @ApiResponse(code=603, message="达到时段广告上限")
    })
    @Audit
    @PostMapping("/shops/{did}/timesegments/{id}/advertisement")
    public Object createAdvertisementsByTimeSegmentId(@LoginUser Long user,  @PathVariable("did") Integer did, @PathVariable("id") Integer id, @RequestBody AdvertiseVo advertiseVo){
        ResponseCode responseCode=advertiseService.createAdvertiseByTimeSegId(id,advertiseVo);
        if(responseCode.equals(ResponseCode.OK))
            return ResponseUtil.ok();
        else return ResponseUtil.fail(responseCode);
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
            @ApiResponse(code=603, message="达到时段广告上限")
    })
    @Audit
    @PostMapping("/shops/{did}/timesegments/{tid}/advertisement/{id}")
    public Object createAdvertisementsByTimeSegmentIdAndId(@LoginUser Long user, @PathVariable("did") Integer did, @PathVariable("tid") Integer tid, @PathVariable("id") Integer id){
        ResponseCode responseCode=advertiseService.createAdvertiseByTimeSegIdAndId(id,tid);
        if(responseCode.equals(ResponseCode.OK))
            return ResponseUtil.ok();
        else return ResponseUtil.fail(responseCode);
    }
}
