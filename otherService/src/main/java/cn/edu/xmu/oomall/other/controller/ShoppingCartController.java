package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.vo.ShoppingCart.*;
import cn.edu.xmu.oomall.other.annotation.OtherAudit;
import cn.edu.xmu.oomall.other.annotation.OtherLoginUser;
import cn.edu.xmu.oomall.other.service.ShoppingCartService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
     * @param page 页码
     * @param pageSize 每页数目
     * @return Object
     */
    @ApiOperation(value = "买家获得购物车列表", produces = "application/json")
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
    public Object getCarts(@OtherLoginUser Long UserId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = shoppingCartService.getCarts(UserId,page==null?1:page, pageSize==null?10:pageSize);
        return Common.getPageRetObject(returnObject);
    }

    /***
     * 买家清空购物车
     * @param UserId 用户id
     * @return Object
     */
    @ApiOperation(value = "买家清空购物车", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @OtherAudit
    @DeleteMapping
    public Object clearCarts(@OtherLoginUser Long UserId) {
        ResponseCode responseCode = shoppingCartService.clearCart(UserId);
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }
    }

    /***
     * 买家删除购物车中商品
     * @param UserId 用户id
     * @return Object
     */
    @ApiOperation(value = "买家删除购物车中商品", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Long", name = "id",            value = "购物车id",    required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @OtherAudit
    @DeleteMapping("/{id}")
    public Object deleteCart(@OtherLoginUser Long UserId,@PathVariable("id") Long id) {
        ResponseCode responseCode = shoppingCartService.deleteCart(UserId,id);
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }
    }

    /***
     * 买家将商品加入购物车
     * @param UserId 用户id
     * @param vo 购物车视图
     * @return Object
     */
    @ApiOperation(value = "买家将商品加入购物车", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @OtherAudit
    @PostMapping
    public Object addToCart(@OtherLoginUser Long UserId,@RequestBody ShoppingCartVo vo) {
        return null;
    }

    /***
     * 买家修改购物车单个商品的数量或规格
     * @param UserId 用户id
     * @param id 购物车Id
     * @param vo 购物车视图
     * @return Object
     */
    @ApiOperation(value = "买家修改购物车单个商品的数量或规格", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "购物车id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @OtherAudit
    @PutMapping
    public Object changeCartInfo(@OtherLoginUser Long UserId,@PathVariable("id") Long id,@RequestBody ShoppingCartVo vo) {
        return null;
    }
}
