package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.vo.ShoppingCart.*;
import cn.edu.xmu.oomall.other.service.ShoppingCartService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Audit
    @GetMapping
    public Object getCarts(@LoginUser Long UserId, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        ReturnObject<PageInfo<VoObject>> returnObject = shoppingCartService.getCarts(UserId,page, pageSize);
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
    @Audit
    @DeleteMapping
    public Object clearCarts(@LoginUser Long UserId) {
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
    @Audit
    @DeleteMapping("/{id}")
    public Object deleteCart(@LoginUser Long UserId,@PathVariable("id") Long id) {
        ResponseCode responseCode = shoppingCartService.deleteCart(UserId,id);
        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)){
                httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
                return ResponseUtil.fail(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        else if(responseCode.equals(ResponseCode.RESOURCE_ID_OUTSCOPE)){
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        return ResponseUtil.ok();
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
    @Audit
    @PostMapping
    public Object addToCart(@LoginUser Long UserId,@RequestBody(required = true) ShoppingCartVo vo) {
        Object ret=shoppingCartService.addCart(UserId,vo.getGoodsSkuId(),vo.getQuantity());
        if(ret==null)
        {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(ResponseCode.OK,"没有该sku存在");
        }
        return ResponseUtil.ok(ret);
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
    @Audit
    @PutMapping("/{id}")
    public Object changeCartInfo(@LoginUser Long UserId, @PathVariable("id") Long id, @RequestBody ShoppingCartVo vo) {
        ResponseCode ret=shoppingCartService.modifyCart(UserId,id,vo.getGoodsSkuId(),vo.getQuantity());
        if(ret==null){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_NOTEXIST,"没有该sku存在");
        }
        if(ret.equals(ResponseCode.RESOURCE_ID_NOTEXIST)){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        else if(ret.equals(ResponseCode.RESOURCE_ID_OUTSCOPE)){
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        return ResponseUtil.ok();

    }
}
