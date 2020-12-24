package cn.edu.xmu.oomall.other.controller;


import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.service.FootprintService;
import cn.edu.xmu.oomall.other.service.mq.FootprintConsumerListener;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/6 上午10:56
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class FootprintController {
    private static final Logger logger = LoggerFactory.getLogger(FootprintController.class);

    @Autowired
    private FootprintService footprintService;

    @Autowired
    FootprintConsumerListener footprintConsumerListener;

    @Autowired
    private HttpServletResponse httpServletResponse;
    /**
     * 管理员分页查询浏览记录
     * @param did 店铺ID
     * @param userId 用户ID
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param page 页码
     * @param pageSize  每页数目
     * @return
     */
    @ApiOperation(value = "管理员查看浏览记录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "did", value = "店ID"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "userId", value = "用户id"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/shops/{did}/footprints")
    public Object getFootprints(@PathVariable("did")Long did, @RequestParam(required = false)Long userId,
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) LocalDateTime beginTime,
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(required = false) LocalDateTime endTime,
                                @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        if(beginTime!=null&&endTime!=null)
            if(beginTime.isAfter(endTime)){
                httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ResponseUtil.fail(ResponseCode.Log_Bigger);
            }
        ReturnObject<PageInfo<VoObject>> returnObject = footprintService.getFootprints(userId,beginTime,endTime,page, pageSize);
        return Common.getPageRetObject(returnObject);
    }


//    /**
//     * 增加足迹
//     * @param UserId
//     * @param id
//     * @return
//     */
//    @ApiOperation(value = "增加足迹", produces = "application/json")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
//            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "sku_id"),
//    })
//    @ApiResponses({
//            @ApiResponse(code = 0,   message = "成功")
//    })
//    @Audit
//    @PostMapping("/skus/{id}/footprints")
//    public Object addFootprint(@LoginUser Long UserId, @PathVariable("id") Long id)
//    {
//        ResponseCode responseCode = footprintService.addFootprint(UserId,id);
//        if(responseCode.equals(ResponseCode.OK)){
//            return ResponseUtil.ok();
//        } else {
//            return ResponseUtil.fail(responseCode);
//        }
//    }

}

/**
 * 修改的点：
 * 取消了二元运算赋page 和 pageSize默认值 改用了defaultvalue
 * @PathVariable 注解后面加上路径里的名字
 */
