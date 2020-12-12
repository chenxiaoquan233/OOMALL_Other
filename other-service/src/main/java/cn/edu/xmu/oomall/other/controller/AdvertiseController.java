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

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/12 上午10:44
 */
@RestController
@RequestMapping(value = "/advertisement", produces = "application/json;charset=UTF-8")
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
    @GetMapping("/advertisement/states")
    public Object getAdvertisementStates(@LoginUser Long userId){
          return advertiseService.getAllAdvertisementStates();
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
    @PutMapping("/shops/{did}/advertisement/{id}/default")
    public Object setAdvertisementDefaultById(@LoginUser Long user, @PathVariable("did") Integer did, @PathVariable("id") Integer id){
        ResponseCode responseCode=advertiseService.setAdvertisementDefaultById(did,id);
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
    @PutMapping("/shops/{did}/advertisement/{id}")
    public Object setAdvertisementDefaultById(@LoginUser Long user, @PathVariable("id") Integer id, @RequestBody AdvertiseVo advertiseVo){
        return null;
    }

    @ApiOperation(value = "管理员删除某一个广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "广告id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @DeleteMapping("/shops/{did}/advertisement/{id}")
    public Object deleteAdvertisementById(@LoginUser Long user, @PathVariable("id") Integer id){
        return null;
    }


    @ApiOperation(value = "获取当前时段广告列表", produces = "application/json")
    @ApiImplicitParams({})
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @GetMapping("/advertisement/current")
    public Object getCurrentAdvertisements(){
        return null;
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
    @PostMapping("/shops/{did}/advertisement/{id}/uploadImg")
    public Object uploadAdvertisementImgById(@LoginUser Long user, @PathVariable("id") Integer id, @RequestParam("img")MultipartFile multipartFile){
        return null;
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
    @PutMapping("/shops/{did}/advertisement/{id}/onshelves")
    public Object onshelvesAdvertisementById(@LoginUser Long user, @PathVariable("id") Integer id){
        return null;
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
    @PutMapping("/shops/{did}/advertisement/{id}/offshelves")
    public Object offshelvesAdvertisementById(@LoginUser Long user, @PathVariable("id") Integer id){
        return null;
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
    @PutMapping("/shops/{did}/advertisement/{id}/audit")
    public Object auditAdvertisementById(@LoginUser Long user, @PathVariable("id") Integer id){
        return null;
    }


    @ApiOperation(value = "管理员查看某一个广告时间段的广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "id", value = "广告id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @GetMapping("/shops/{did}/timesegments/{id}/advertisement")
    public Object getAdvertisementsByTimeSegmentId(@LoginUser Long user, @PathVariable("id") Integer id, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize){
        return null;
    }


    @ApiOperation(value = "管理员在广告时段下新建广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "id", value = "广告id", required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AdvertiseVo", name = "vo",            value = "可修改的广告信息",  required = true)

    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code=603, message="达到时段广告上限")
    })
    @PostMapping("/shops/{did}/timesegments/{id}/advertisement")
    public Object createAdvertisementsByTimeSegmentId(@LoginUser Long user, @PathVariable("id") Integer id, @RequestBody AdvertiseVo advertiseVo){
        return null;
    }


    @ApiOperation(value = "管理员在广告时段下增加广告", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "广告时间段id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String", name = "id", value = "广告id", required = true)

    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功"),
            @ApiResponse(code=603, message="达到时段广告上限")
    })
    @PostMapping("/shops/{did}/timesegments/{tid}/advertisement/{id}")
    public Object createAdvertisementsByTimeSegmentId(@LoginUser Long user, @PathVariable("id") String tid, @PathVariable("id") String id){
        return null;
    }
}
