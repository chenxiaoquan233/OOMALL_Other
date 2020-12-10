package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.AftersaleVo;
import cn.edu.xmu.oomall.other.service.AfterSaleService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author XQChen
 * @version 创建时间：2020/12/10 上午10:07
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class AfterSaleController {
    private static final Logger logger = LoggerFactory.getLogger(AfterSaleController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private AfterSaleService afterSaleService;

    @ApiOperation(value = "获得售后单的所有状态", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/aftersales/states")
    public Object getAfterSaleAllStates() {
        logger.debug("getAfterSaleAllStates");

        return afterSaleService.getAfterSaleAllStates();
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
    @PostMapping("/orderItems/{id}aftersales")
    public Object createAfterSale(@RequestBody AftersaleVo vo, @PathVariable("id") Long orderItemId) {
        return null;
    }

    @GetMapping("/aftersales")
    public Object getAllAfterSale() {
        return null;
    }

    @GetMapping("/shops/{id}/aftersales")
    public Object adminGetAllAfterSale() {
        return null;
    }

    @GetMapping("/aftersales{id}")
    public Object getAfterSaleById() {
        return null;
    }

    @PutMapping("/aftersales/{id}")
    public Object modifyAfterSaleById() {
        return null;
    }

    @DeleteMapping("/aftersales/{id}")
    public Object deleteAfterSaleById() {
        return null;
    }

    @PutMapping("/aftersales/{id}/sendback")
    public Object addWayBillNumber() {
        return null;
    }

    @PutMapping("/aftersales/{id}/confirm")
    public Object confirmAfterSaleEnd() {
        return null;
    }

    @GetMapping("/shops/{shopId}/aftersales/{id}")
    public Object adminGetAfterSaleById() {
        return null;
    }

    @PutMapping("/shops/{shopId}/aftersales/{id}/confirm")
    public Object adminConfirmAfterSale() {
        return null;
    }

    @PutMapping("/shops/{shopId}/aftersales/{id}/receive")
    public Object adminReceive() {
        return null;
    }

    @PutMapping("/shops/{shopId}/aftersales/{id}/deliver")
    public Object adminDeliver() {
        return null;
    }
}
