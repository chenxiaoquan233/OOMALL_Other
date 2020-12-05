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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jx
 * @version 创建时间：2020/12/3 上午8:56
 */
@Service
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
    public Object getFavorites(@LoginUser Long UserId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = favoriteService.getFavorites(UserId,page==null?1:page, pageSize==null?10:pageSize);
        return Common.getPageRetObject(returnObject);
    }

    /***
     * 买家收藏商品
     * @param UserId 用户id
     * @param spuId 商品spuId
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
    @PostMapping("/goods/{spuId}")
    public Object addFavorites(@LoginUser Long UserId, @PathVariable("spuId") Long spuId) {
        return Common.getRetObject(favoriteService.addFavorites(UserId,spuId));
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
        ResponseCode responseCode = favoriteService.deleteFavorites(UserId,id);
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }
    }
}
