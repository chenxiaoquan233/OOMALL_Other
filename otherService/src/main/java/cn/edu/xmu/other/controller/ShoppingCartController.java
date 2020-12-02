package cn.edu.xmu.other.controller;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.otherCore.annotation.OtherAudit;
import cn.edu.xmu.other.otherCore.annotation.OtherLoginUser;
import cn.edu.xmu.other.service.ShoppingCartService;
import cn.edu.xmu.other.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/carts", produces = "application/json;charset=UTF-8")
public class ShoppingCartController {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /***
     * 买家获得购物车列表
     * @param UserId 用户id
     * @return Object
     */
    @ApiOperation(value = "买家获得购物车列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "用户token"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "用户token")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @OtherAudit
    @GetMapping("")
    public Object getCarts(@OtherLoginUser Long UserId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        System.out.println(pageSize);
        ReturnObject<PageInfo<VoObject>> returnObject = shoppingCartService.getCarts(UserId,page,pageSize);
        return Common.getPageRetObject(returnObject);
    }

}
