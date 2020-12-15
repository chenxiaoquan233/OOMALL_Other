package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.*;
import cn.edu.xmu.oomall.other.service.AftersaleService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author XQChen
 * @version 创建时间：2020/12/10 上午10:07
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class AftersaleController {
    private static final Logger logger = LoggerFactory.getLogger(AftersaleController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private AftersaleService aftersaleService;

    @ApiOperation(value = "获得售后单的所有状态", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/aftersales/states")
    public Object getAftersaleAllStates() {
        logger.debug("getAfterSaleAllStates");

        return ResponseUtil.ok(aftersaleService.getAftersaleAllStates());
    }

    @ApiOperation(value = "买家提交售后单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",      name = "authorization", value = "用户token",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",     name = "id",            value = "订单明细id",   required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleVo", name = "body",          value = "售后服务信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PostMapping("/orderItems/{id}/aftersales")
    public Object createAftersale(@LoginUser Long userId, @Validated @RequestBody AftersaleVo vo, @PathVariable("id") Long orderItemId, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");

            return object;
        }

        AftersaleRetVo aftersaleRetVo = aftersaleService.createAftersale(vo, orderItemId, userId);

        logger.debug("VO here:" + aftersaleRetVo);

        return ResponseUtil.ok(aftersaleRetVo);
    }

    @ApiOperation(value = "买家查询所有的售后单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token",    required = true),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "spuId",         value = "SPU Id"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "skuId",         value = "sku ID"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "orderItemId",   value = "orderItem Id"),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "beginTime",     value = "开始时间"),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "endTime",       value = "结束时间"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "page",          value = "页码"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "pageSize",      value = "每页数目"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "type",          value = "售后类型"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "state",         value = "售后状态")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/aftersales")
    public Object getAllAftersale(
            @LoginUser Long userId,
            @RequestParam(required = false) Long spuId,
            @RequestParam(required = false) Long skuId,
            @RequestParam(required = false) Long orderItemId,
            @RequestParam(required = false) LocalDateTime beginTime,
            @RequestParam(required = false) LocalDateTime endTime,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer state) {

        return ResponseUtil.ok(aftersaleService.getAllAftersales(userId, null, beginTime, endTime, page, pageSize, type, state));
    }

    @ApiOperation(value = "买家查询所有的售后单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token",    required = true),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "spuId",         value = "SPU Id"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "skuId",         value = "sku ID"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "orderItemId",   value = "orderItem Id"),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "beginTime",     value = "开始时间"),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "endTime",       value = "结束时间"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "page",          value = "页码"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "pageSize",      value = "每页数目"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "type",          value = "售后类型"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "state",         value = "售后状态")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/shops/{id}/aftersales")
    public Object adminGetAllAftersale(
            @LoginUser Long userId,
            @Depart Long did,
            @PathVariable("id") Long shopId,
            @RequestParam(required = false) Long spuId,
            @RequestParam(required = false) Long skuId,
            @RequestParam(required = false) Long orderItemId,
            @RequestParam(required = false) LocalDateTime beginTime,
            @RequestParam(required = false) LocalDateTime endTime,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer state) {

        if(!did.equals(shopId)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResponseUtil.fail(ResponseCode.AUTH_NOT_ALLOW);
        }

        return ResponseUtil.ok(aftersaleService.getAllAftersales(userId, shopId, beginTime, endTime, page, pageSize, type, state));
    }

    @ApiOperation(value = "买家根据售后单id查询售后单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer", name = "id",         value = "售后单id")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/aftersales/{id}")
    public Object getAftersaleById(@LoginUser Long userId, @PathVariable("id") Long aftersaleId) {
        Object object = aftersaleService.getAftersaleById(userId, aftersaleId);

        if(object.equals(ResponseCode.RESOURCE_ID_NOTEXIST))
        {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail((ResponseCode) object);
        }
        if(object.equals(ResponseCode.RESOURCE_ID_OUTSCOPE))
            return ResponseUtil.fail((ResponseCode) object, ((ResponseCode) object).getMessage());
        return ResponseUtil.ok(object);
    }

    @ApiOperation(value = "买家修改售后单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",            name = "authorization", value = "用户token",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",           name = "id",            value = "售后单id",     required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleModifyVo", name = "body", value = "买家可修改的信息：地址，售后商品的数量，申请售后的原因，联系人以及联系电话", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/aftersales/{id}")
    public Object modifyAftersaleById(@LoginUser Long userId, @PathVariable("id") Long aftersaleId, @Validated AftersaleModifyVo vo) {
        ResponseCode responseCode = aftersaleService.modifyAftersaleById(userId, aftersaleId, vo);

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        return ResponseUtil.ok(responseCode);
    }

    @ApiOperation(value = "买家填写售后的运单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",              name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",             name = "id",            value = "售后单id",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @DeleteMapping("/aftersales/{id}")
    public Object deleteAftersaleById(@LoginUser Long userId, @PathVariable("id") Long id) {
        ResponseCode responseCode = aftersaleService.deleteAftersaleById(userId, id);

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        return ResponseUtil.ok(responseCode);
    }

    @ApiOperation(value = "买家填写售后的运单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",              name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",             name = "id",            value = "售后单id",  required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleSendbackVo", name = "body",          value = "运单号",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/aftersales/{id}/sendback")
    public Object addWayBillNumber(@LoginUser Long userId, @PathVariable("id") Long id, @Validated @RequestBody AftersaleSendbackVo vo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserSignUpVo:" + vo);

            return object;
        }

        ResponseCode responseCode = aftersaleService.addWayBillNumber(userId, id, vo.getLogsn());

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        return ResponseUtil.ok(responseCode);
    }

    @ApiOperation(value = "买家确认售后单结束", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",             name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "id",            value = "售后单id",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/aftersales/{id}/confirm")
    public Object confirmAftersaleEnd(@LoginUser Long userId, @PathVariable("id") Long id) {
        ResponseCode responseCode = aftersaleService.confirmAftersaleEnd(userId, id);

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        return ResponseUtil.ok(responseCode);
    }

    @ApiOperation(value = "管理员根据售后单id查询售后单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",             name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "shopId",        value = "店铺id",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "id",            value = "售后单id",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/shops/{shopId}/aftersales/{id}")
    public Object adminGetAftersaleById(@LoginUser Long userId, @Depart Long did, @PathVariable("shopId") Long shopId, @PathVariable("id") Long id) {
        if(!did.equals(shopId)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResponseUtil.fail(ResponseCode.AUTH_NOT_ALLOW);
        }

        Object object = aftersaleService.adminGetAftersaleById(shopId, id);
        if(object.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail((ResponseCode) object);
        }
        return ResponseUtil.ok(object);
    }

    @ApiOperation(value = "管理员同意/不同意（退款，换货，维修）", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",             name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "shopId",        value = "店铺id",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "id",            value = "售后单id",  required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleConfirmVo", name = "body",          value = "处理意见",    required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/shops/{shopId}/aftersales/{id}/confirm")
    public Object adminConfirmAftersale(
            @LoginUser Long userId,
            @Depart Long did,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @RequestBody AftersaleConfirmVo vo,
            BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserSignUpVo:" + vo);

            return object;
        }

        if(!did.equals(shopId)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResponseUtil.fail(ResponseCode.AUTH_NOT_ALLOW);
        }

        ResponseCode responseCode = aftersaleService.adminConfirm(id, shopId, vo);

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        return ResponseUtil.ok(responseCode);
    }

    @ApiOperation(value = "店家确认收到买家的退（换）货", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",             name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "shopId",        value = "店铺id",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "id",            value = "售后单id",  required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleReceiveVo", name = "body",          value = "处理意见",    required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/shops/{shopId}/aftersales/{id}/receive")
    public Object adminReceive(
            @LoginUser Long userId,
            @Depart Long did,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @RequestBody AftersaleReceiveVo vo,
            BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserSignUpVo:" + vo);

            return object;
        }

        if(!did.equals(shopId)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResponseUtil.fail(ResponseCode.AUTH_NOT_ALLOW);
        }

        ResponseCode responseCode = aftersaleService.adminReceive(id, shopId, vo);

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        return ResponseUtil.ok(responseCode);
    }

    @ApiOperation(value = "店家寄出维修好的货物", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",             name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "shopId",        value = "店铺id",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "id",            value = "售后单id",  required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleDeliverVo", name = "body",          value = "运单号",    required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/shops/{shopId}/aftersales/{id}/deliver")
    public Object adminDeliver(
            @LoginUser Long userId,
            @Depart Long did,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @Validated @RequestBody AftersaleDeliverVo vo,
            BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserSignUpVo:" + vo);

            return object;
        }

        if(!did.equals(shopId)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ResponseUtil.fail(ResponseCode.AUTH_NOT_ALLOW);
        }

        ResponseCode responseCode = aftersaleService.adminDeliver(id, shopId, vo.getLogSn());

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        return ResponseUtil.ok(responseCode);
    }
}
