package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.vo.Address.AddressRetVo;
import cn.edu.xmu.oomall.other.model.vo.Address.AddressVo;
import cn.edu.xmu.oomall.other.model.vo.Address.RegionVo;
import cn.edu.xmu.oomall.other.service.AddressService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/6  下午16:11
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/addresses", produces = "application/json;charset=UTF-8")
public class AddressController {
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @ApiOperation(value = "买家新增地址", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "AddressVo", name = "addressVo", value = "addressInfo",required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PostMapping
    public Object addAddress(@LoginUser Long userId, @RequestBody AddressVo addressVo, BindingResult result)
    {
        if(result.hasErrors())
        {
            return Common.processFieldErrors(result,httpServletResponse);
        }
        ReturnObject<VoObject> returnObj = addressService.addAddress(userId,addressVo);
        if(returnObj.getCode() == ResponseCode.OK){
            Object returnVo = returnObj.getData().createSimpleVo();
            return Common.decorateReturnObject(new ReturnObject(returnVo));
        }
        else return ResponseUtil.fail(returnObj.getCode());
    }

    /**
     * 用户查看已有地址
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "买家查看所有已有的地址信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "userId", value = "用户id"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping
    public  Object getAddresses(@LoginUser Long userId,@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize){
        ReturnObject<PageInfo<VoObject>> returnObject = addressService.getAddresses(userId, page, pageSize);
        return Common.getPageRetObject(returnObject);
    }

    /**
     * 买家设置默认地址
     * @param userId
     * @param id
     * @return
     */
    @ApiOperation(value = "买家设置默认地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/addresses/{id}/default")
    public Object updateDefaultAddress(@LoginUser Long userId, @PathVariable("id")Long id)
    {
        ResponseCode responseCode = addressService.updateDefaultAddress(userId,id).getCode();
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }
    }

    /**
     * 用户修改自己的地址信息
     * @param userId
     * @param id
     * @param addressVo
     * @return
     */

    @ApiOperation(value = "买家修改自己的地址信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
            @ApiImplicitParam(paramType = "body", dataType = "AddressVo", name = "addressVo", value = "可修改的地址信息")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/addresses/{id}")
    public Object updateAddress(@LoginUser Long userId, @PathVariable("id") Long id,@RequestBody AddressVo addressVo,BindingResult result) {
        if(result.hasErrors()){
            return Common.processFieldErrors(result,httpServletResponse);
        }
        ResponseCode responseCode = addressService.updateAddress(userId,id,addressVo).getCode();
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }
    }

    /**
     * 买家删除地址
     * @param userId
     * @param id
     * @return
     */
    @ApiOperation(value = "买家删除地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @DeleteMapping("/addresses/{id}")
    public Object deleteAddress(@LoginUser Long userId, @PathVariable Long id){
        ResponseCode responseCode = addressService.deleteAddress(userId,id).getCode();
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }
    }

    /**
     * 查询某个地区的所有上级地区
     * @param id
     * @return
     */
    @ApiOperation(value = "询某个地区的所有上级地区")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/region/{id}/ancestor")
    public Object getAllParentRegions(@PathVariable("id")Long id)
    {
        ReturnObject<List> returnObject = addressService.getAllParentRegions(id);
        return Common.getListRetObject(returnObject);
    }

    /**
     * 管理员在地区下新增子地区
     * @param id
     * @param regionVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员在地区下新增子地区")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
            @ApiImplicitParam(paramType = "body", dataType = "RegionVo", name = "regionVo", value = "地区信息")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PostMapping("/regions/{id}/subregions")
    public Object addSubRegion(@PathVariable("id")Long id, @RequestBody(required = true) RegionVo regionVo,BindingResult bindingResult)
    {
        Object returnObject = Common.processFieldErrors(bindingResult,httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        ResponseCode responseCode = addressService.addSubRegion(id,regionVo).getCode();
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }
    }

    /**
     * 管理员修改某个地区
     * @param id
     * @param regionVo
     * @return
     */
    @ApiOperation(value = "管理员修改某个地区")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
            @ApiImplicitParam(paramType = "body", dataType = "RegionVo", name = "regionVo", value = "地区信息")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/regions/{id}")
    public Object updateRegion(@PathVariable("id") Long id, @RequestBody(required = true) RegionVo regionVo,BindingResult bindingResult)
    {
        Object returnObject = Common.processFieldErrors(bindingResult,httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        ResponseCode responseCode = addressService.updateRegion(id, regionVo).getCode();
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }
    }

    /**
     * 管理员让某个地区无效
     * @param id
     * @return
     */
    @ApiOperation(value = "管理员让某个地区无效")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @DeleteMapping("/regions/{id}")
    public Object deleteRegion(@PathVariable("id")Long id)
    {
        ResponseCode responseCode = addressService.deleteRegion(id).getCode();
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            return ResponseUtil.fail(responseCode);
        }
    }

}
