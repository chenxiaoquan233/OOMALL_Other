package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.vo.ShareActivity.ShareActivityVo;
import cn.edu.xmu.oomall.other.service.ShareService;
import cn.edu.xmu.oomall.other.service.ShoppingCartService;
import cn.xmu.edu.goods.client.IGoodsService;
import cn.xmu.edu.goods.client.IShopService;
import cn.xmu.edu.goods.client.dubbo.ShopDTO;
import com.github.pagehelper.PageInfo;
import com.github.sardine.model.Bind;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author Jx
 * @version 创建时间：2020/12/5 下午2:57
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value="/share", produces = "application/json;charset=UTF-8")
public class ShareController {
    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private ShareService shareService;

    @DubboReference
    IGoodsService goodsService;

    /***
     * 平台或店家创建新的分享活动
     */
    @ApiOperation(value= "平台或店家创建新的分享活动", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "店铺Id"),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "skuId", value = "商品skuId")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PostMapping("/shops/{shopId}/goods/{skuId}/shareactivities")
    public Object addShareActivity(@LoginUser Long UserId, @PathVariable("shopId") Long shopId, @PathVariable("skuId") Long skuId,
                                   @Validated @RequestBody ShareActivityVo shareActivityVo, BindingResult bindingResult){
        Object object= Common.processFieldErrors(bindingResult,httpServletResponse);
        if(object!=null){
            return object;
        }
        ShopDTO shopDTO=goodsService.getShopBySKUId(skuId);
        if(shopDTO==null){
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(ResponseCode.OK,"资源不存在");
        }
        else if(shopId!=0){
            Long realShopId=shopDTO.getId();
            if(!realShopId.equals(shopId)){
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return ResponseUtil.fail(ResponseCode.OK,"非法资源");
            }
        }

        return Common.getRetObject(shareService.addShareActivity(shopId,skuId,shareActivityVo));
    }

    /***
     * 买家查询所有分享记录
     */
    @ApiOperation(value= "买家查询所有分享记录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "skuId", value = "商品skuId"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/shares")
    public Object getShares(@LoginUser Long UserId, @RequestParam Long skuId,
                            @RequestParam(required = false) LocalDateTime beginTime, @RequestParam(required = false) LocalDateTime endTime,
                            @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize){
        if(endTime.isBefore(beginTime)){
            return ResponseUtil.fail(ResponseCode.Log_Bigger);
        }
        if(page <= 0 || pageSize <= 0) {
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        }
        ReturnObject<PageInfo<VoObject>> retObj=shareService.findShares(skuId,beginTime,endTime,page,pageSize);
        return Common.getPageRetObject(retObj);
    }

    /***
     * 管理员下线指定商品的分享活动
     */
    @ApiOperation(value= "管理员下线指定商品的分享活动", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "店铺Id"),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "分享活动id")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @DeleteMapping("/shops/{shopId}/shareactivities/{id}")
    public Object deleteShareActivity(@LoginUser Long UserId, @PathVariable("shopId") Long shopId, @PathVariable("id") Long shareActivityId){
        return null;
    }

    /***
     * 管理员上线分享活动
     */
    @ApiOperation(value= "管理员上线分享活动", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "店铺Id"),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "分享活动id")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/shops/{shopId}/shareactivities/{id}/online")
    public Object shareActivityOnline(@LoginUser Long UserId, @PathVariable("shopId") Long shopId, @PathVariable("id") Long shareActivityId){
        return null;
    }


}
