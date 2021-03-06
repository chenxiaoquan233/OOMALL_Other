package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.service.FavoriteService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jx
 * @version 创建时间：2020/12/3 上午8:56
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/favorites", produces = "application/json;charset=UTF-8")
public class FavoriteController {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private FavoriteService favoriteService;

    /***
     * 买家查看所有收藏的商品
     * @param UserId 用户id
     * @param page 页码
     * @param pageSize 每页数目
     * @return Object
     */
    @ApiOperation(value = "买家查看所有收藏的商品", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping
    public Object getFavorites(@LoginUser Long UserId, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
//        if(page<=0||pageSize<=0)
//        {
//            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//            return ResponseUtil.fail(ResponseCode.OK,"page或pageSize格式不符");
//        }
        ReturnObject<PageInfo<VoObject>> returnObject = favoriteService.getFavorites(UserId,page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    /***
     * 买家收藏商品
     * @param UserId 用户id
     * @param skuId 商品spuId
     * @return Object
     */
    @ApiOperation(value = "买家收藏商品", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "spuId", value = "商品spuId")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PostMapping("/goods/{skuId}")
    public Object addFavorites(@LoginUser Long UserId, @PathVariable("skuId") Long skuId) {
        if(skuId<=0)
        {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseUtil.fail(ResponseCode.FIELD_NOTVALID,"skuId格式不符");
        }
        Object ret=favoriteService.addFavorites(UserId,skuId);
        if(ret==null){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_NOTEXIST,"sku不存在");
        }
        httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return ret;
    }

    /***
     * 买家删除收藏商品
     * @param UserId 用户id
     * @param id 收藏id
     * @return Object
     */
    @ApiOperation(value = "买家删除收藏商品", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "id")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @DeleteMapping("/{id}")
    public Object deleteFavorites(@LoginUser Long UserId, @PathVariable("id") Long id) {
        if(id<=0)
        {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseUtil.fail(ResponseCode.FIELD_NOTVALID);
        }
        ResponseCode responseCode = favoriteService.deleteFavorites(UserId,id);
        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(responseCode.equals(ResponseCode.RESOURCE_ID_OUTSCOPE)) {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        else {
            return ResponseUtil.ok();
        }
    }
}
