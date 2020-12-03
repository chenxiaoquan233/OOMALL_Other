package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.otherCore.annotation.OtherAudit;
import cn.edu.xmu.other.otherCore.annotation.OtherLoginUser;
import cn.edu.xmu.other.service.FavoriteService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @OtherAudit
    @GetMapping
    public Object getCarts(@OtherLoginUser Long UserId, @RequestParam(required = true) Integer page, @RequestParam(required = true
    ) Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = FavoriteService.getFavorites(UserId,page==null?1:page, pageSize==null?10:pageSize);
        return Common.getPageRetObject(returnObject);
    }


}
