package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.*;
import cn.edu.xmu.oomall.other.model.vo.ShareActivity.ShareActivityVo;
import cn.edu.xmu.oomall.other.service.ShareService;
import cn.edu.xmu.oomall.other.service.factory.CalcPointFactory;
import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.goods.client.dubbo.ShopDTO;
import com.github.pagehelper.PageInfo;
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

    @DubboReference(registry = {"provider2"}, version = "0.0.1-SNAPSHOT", check = false)
    IGoodsService goodsService;


    //DONE:生成分享链接
    //Done:接受点击分享连接信息
    //以完成：下单后更改分享成功
    //Done:七天后返回返点
    //TODO:分享活动加入redis中 ps:在下线或上限活动时，记得清空对应商品的缓存
    //DONE:支付时扣除返点
    //Topic
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
    public Object addShareActivity(@LoginUser Long userId, @PathVariable("shopId") Long shopId, @PathVariable("skuId") Long skuId,
                                   @Validated @RequestBody ShareActivityVo shareActivityVo, BindingResult bindingResult){
        //DONE:校验strategy是否正确
        if(!CalcPointFactory.validateStrategy(shareActivityVo.getStrategy())){
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResponseUtil.fail(ResponseCode.OK,"分享策略不合法");
        }
        Object object= Common.processFieldErrors(bindingResult,httpServletResponse);
        if(object!=null){
            return object;
        }
        //SHOPID为0 skuId为0 代表平台默认
        //shopId不为0 skuId为0 代表店铺默认
        //skuid不为0 shopid不为0 代表商店某个商品
        if(shopId==0&&skuId!=0){
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(ResponseCode.OK,"平台活动无法设置skuId");
        }
        if(skuId!=0) {
            ShopDTO shopDTO = goodsService.getShopBySKUId(skuId);
            if (shopDTO == null) {
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return ResponseUtil.fail(ResponseCode.OK, "资源不存在");
            }
            Long realShopId=shopDTO.getId();
            if(!realShopId.equals(shopId)) {
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return ResponseUtil.fail(ResponseCode.OK, "非法资源");
            }
        }
        //logger.info("read this?"+ JacksonUtil.toJson(shareActivityVo));
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
    public Object getShares(@LoginUser Long userId, @RequestParam(required = false) Long skuId,
                            @RequestParam(required = false) LocalDateTime beginTime, @RequestParam(required = false) LocalDateTime endTime,
                            @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize){
        if(beginTime!=null&&endTime!=null&&endTime.isBefore(beginTime)){
            return ResponseUtil.fail(ResponseCode.Log_Bigger);
        }
        if(page <= 0 || pageSize <= 0) {
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        }
        ReturnObject<PageInfo<VoObject>> retObj=shareService.findShares(skuId, beginTime,endTime,page,pageSize);
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
    public Object offlineShareActivity(@LoginUser Long userId, @PathVariable("shopId") Long shopId, @PathVariable("id") Long shareActivityId){
        ResponseCode ret=shareService.offlineShareActivity(shopId,shareActivityId);
        if(ret==ResponseCode.OK){
            return ResponseUtil.ok();
        }
        else{
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(ret);
        }
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
    public Object shareActivityOnline(@LoginUser Long userId, @PathVariable("shopId") Long shopId, @PathVariable("id") Long shareActivityId){
        ResponseCode ret=shareService.onlineShareActivity(shopId,shareActivityId);
        if (ret != ResponseCode.SHAREACT_CONFLICT && ret != ResponseCode.OK) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return ret;
    }

    /***
     * 商铺管理员查询分享记录
     */
    @Audit
    @GetMapping("/shop/{did}/skus/{id}/shares")
    public Object getSharesByShopId(@LoginUser Long userId,@PathVariable("did")Long shopId,
                                    @PathVariable("id") Long skuId,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer pageSize)
    {
        if(shopId!=0) {
            ShopDTO shopDTO = goodsService.getShopBySKUId(skuId);
            if(shopDTO==null){
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return ResponseUtil.fail(ResponseCode.OK, "资源不存在");
            }
            Long realShopId = shopDTO.getId();
            if (!realShopId.equals(shopId)) {
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return ResponseUtil.fail(ResponseCode.OK, "路径资源不匹配");
            }
        }
        ReturnObject<PageInfo<VoObject>> ret=shareService.findShares(skuId, null,null,page,pageSize);
        return Common.getPageRetObject(ret);
    }
    /***
     * 买家查询所有的分享成功记录
     */
    @Audit
    @GetMapping("/beshared")
    public Object getBeShared(@LoginUser Long userId,@RequestParam(required = false)Long skuId,
                              @RequestParam(required = false)LocalDateTime beginTime,
                              @RequestParam(required = false)LocalDateTime endTime,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer pageSize){
        if(beginTime!=null&&endTime!=null&&endTime.isBefore(beginTime)){
            return ResponseUtil.fail(ResponseCode.Log_Bigger);
        }
        ReturnObject<PageInfo<VoObject>> retObj=shareService.getBeShared(userId, skuId,beginTime,endTime,page,pageSize);
        return Common.getPageRetObject(retObj);
    }

    /**
     * 管理员查询店铺分享成功记录
     * @param userId
     * @param shopId
     * @param skuId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    @Audit
    @GetMapping("/shops/{did}/sku/{id}/beshared")
    public Object getBeSharedByShopId(@LoginUser Long userId,@PathVariable("did")Long shopId,
                              @PathVariable("id") Long skuId,
                              @RequestParam(required = false)LocalDateTime beginTime,
                              @RequestParam(required = false)LocalDateTime endTime,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer pageSize){
        if(beginTime!=null&&endTime!=null&&endTime.isBefore(beginTime)){
            return ResponseUtil.fail(ResponseCode.Log_Bigger);
        }
        if(shopId!=0) {
            ShopDTO shopDTO = goodsService.getShopBySKUId(skuId);
            if(shopDTO==null){
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return ResponseUtil.fail(ResponseCode.OK, "资源不存在");
            }
            Long realShopId = shopDTO.getId();
            if (!realShopId.equals(shopId)) {
                httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return ResponseUtil.fail(ResponseCode.OK, "路径资源不匹配");
            }
        }
        ReturnObject<PageInfo<VoObject>> retObj=shareService.getBeShared(null, skuId,beginTime,endTime,page,pageSize);
        return Common.getPageRetObject(retObj);
    }

    /**
     * 修改分享活动
     * @param userId
     * @param shopId
     * @param shareActivityId
     * @param vo
     * @return
     */
    @Audit
    @PutMapping("/shops/{shopId}/shareactivities/{id}")
    public Object modifyShareActivity(@LoginUser Long userId,@PathVariable("shopId")Long shopId,
                                         @PathVariable("id")Long shareActivityId,
                                         @RequestBody ShareActivityVo vo){
        if(!CalcPointFactory.validateStrategy(vo.getStrategy())){
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResponseUtil.fail(ResponseCode.OK,"分享策略不合法");
        }
        ResponseCode ret=shareService.updateShareActivity(shopId,shareActivityId,vo);
        if (ret != ResponseCode.INTERNAL_SERVER_ERR && ret != ResponseCode.OK) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return ret;
    }

    /**
     * 查询所有分享活动
     * @param userId
     * @param shopId
     * @param skuId
     * @param page
     * @param pageSize
     * @return
     */
    @Audit
    @GetMapping("/shareactivities")
    public Object getShareActivities(@LoginUser Long userId,@RequestParam(required = false)Long shopId,
                                     @RequestParam(required = false)Long skuId,
                                     @RequestParam(defaultValue = "1")Integer page,
                                     @RequestParam(defaultValue = "10")Integer pageSize){
        //是否需要判断SKUID属于SHOPID
        ReturnObject<PageInfo<VoObject>> retObj=shareService.getShareActivities(shopId,skuId,page,pageSize);
        return Common.getPageRetObject(retObj);
    }
    @Audit
    @PostMapping("/skus/{id}/shares")
    public Object getLink(@LoginUser Long userId,@PathVariable("id")Long skuId){
        ReturnObject ret=shareService.getShareLink(skuId,userId);
        if(ret==null){
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(ResponseCode.OK,"没有有效的活动");
        }
        return Common.getRetObject(ret);
    }

}
