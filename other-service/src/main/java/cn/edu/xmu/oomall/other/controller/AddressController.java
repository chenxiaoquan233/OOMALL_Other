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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/6  下午16:11
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
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
    @PostMapping("/addresses")
    public Object addAddress(@LoginUser Long userId, @RequestBody AddressVo addressVo, BindingResult result)
    {
        Object object = Common.processFieldErrors(result,httpServletResponse);
        if(null != object)
        {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return object;
        }
        if(!addressVo.isFormated()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseUtil().fail(ResponseCode.FIELD_NOTVALID);
        }
        try{
            ReturnObject<VoObject> returnObj = addressService.addAddress(userId,addressVo);
           if(returnObj.getCode().equals(ResponseCode.OK)) {
               Object returnVo = returnObj.getData().createSimpleVo();
               httpServletResponse.setStatus(HttpStatus.CREATED.value());
               return Common.decorateReturnObject(new ReturnObject(returnVo));
           }
           else if(returnObj.getCode().equals(ResponseCode.RESOURCE_ID_NOTEXIST))
            {
               httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
               return ResponseUtil.fail(returnObj.getCode());
           }
           else if(returnObj.getCode().equals(ResponseCode.REGION_OBSOLETE))
           {
               httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
               return new ResponseUtil().fail(ResponseCode.REGION_OBSOLETE);
           }
           else{
               return ResponseUtil.fail(returnObj.getCode(),returnObj.getErrmsg());
           }
        }catch (Exception e)
        {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseUtil.fail(ResponseCode.INTERNAL_SERVER_ERR);
        }

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
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/addresses")
    public  Object getAddresses(@LoginUser Long userId,@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize){
        if(page<=0||pageSize<=0)
        {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseUtil.fail(ResponseCode.OK,"page或pageSize格式不符");
        }
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
        }
        else if(responseCode.equals(ResponseCode.RESOURCE_ID_OUTSCOPE)){
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return ResponseUtil.fail(responseCode);
        }
        else if(responseCode.equals(ResponseCode.REGION_OBSOLETE))
        {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseUtil.fail(ResponseCode.REGION_OBSOLETE);
        }
        else {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
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
        if(!addressVo.isFormated()) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return new ResponseUtil().fail(ResponseCode.FIELD_NOTVALID);
        }
        ResponseCode responseCode = addressService.updateAddress(userId,id,addressVo).getCode();
        if(responseCode.equals(responseCode.equals(ResponseCode.OK)))
        {
            return ResponseUtil.ok();
        }
        else if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        else if(responseCode.equals(ResponseCode.REGION_OBSOLETE)){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseUtil.fail(ResponseCode.REGION_OBSOLETE);
        }
        else if(responseCode.equals(ResponseCode.RESOURCE_ID_OUTSCOPE))
        {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return ResponseUtil.fail(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        else {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
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
        }
        else {
            if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST))httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
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
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地区id"),
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/region/{id}/ancestor")
    public Object getAllParentRegions(@PathVariable("id")Long id)
    {
        ReturnObject<List> returnObject = addressService.getAllParentRegions(id);
        if(returnObject.getCode().equals(ResponseCode.OK)){
            return Common.getListRetObject(returnObject);
        }
        else if(returnObject.getCode().equals(ResponseCode.REGION_OBSOLETE))
        {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseUtil.fail(ResponseCode.REGION_OBSOLETE);
        }
        else {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(returnObject.getCode());
        }

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
            @ApiImplicitParam(paramType = "path", dataType = "Integer",name = "did",value = "店铺id"),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
            @ApiImplicitParam(paramType = "body", dataType = "RegionVo", name = "regionVo", value = "地区信息")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PostMapping("/shops/{did}/regions/{id}/subregions")
    public Object addSubRegion(@PathVariable("did")Long did,@PathVariable("id")Long id, @RequestBody(required = true) RegionVo regionVo,BindingResult bindingResult)
    {
        Object returnObject = Common.processFieldErrors(bindingResult,httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        ResponseCode responseCode = addressService.addSubRegion(id,regionVo).getCode();
        if(!regionVo.isFormated()){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return ResponseUtil.fail(ResponseCode.FIELD_NOTVALID);
        }
        if(responseCode.equals(ResponseCode.OK)){
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return ResponseUtil.ok();
        }
        else if(responseCode.equals(ResponseCode.REGION_OBSOLETE))
        {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return  ResponseUtil.fail(responseCode,"地区已废弃");
        }
        else
            {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
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
            @ApiImplicitParam(paramType = "path", dataType = "Integer",name = "did",value = "店铺id"),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
            @ApiImplicitParam(paramType = "body", dataType = "RegionVo", name = "regionVo", value = "地区信息")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/shops/{did}/regions/{id}")
    public Object updateRegion(@PathVariable("did")Long did,@PathVariable("id") Long id, @RequestBody(required = true) RegionVo regionVo,BindingResult bindingResult)
    {
        Object returnObject = Common.processFieldErrors(bindingResult,httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        ResponseCode responseCode = addressService.updateRegion(id, regionVo).getCode();
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        }
        else if(responseCode.equals(ResponseCode.REGION_OBSOLETE))
        {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return  ResponseUtil.fail(responseCode,"地区已废弃");
        }
        else {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
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
            @ApiImplicitParam(paramType = "path", dataType = "Integer",name = "did",value = "店铺id"),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @DeleteMapping("/shops/{did}/regions/{id}")
    public Object deleteRegion(@PathVariable("did")Long did,@PathVariable("id")Long id)
    {
        ResponseCode responseCode = addressService.deleteRegion(id).getCode();
        if(responseCode.equals(ResponseCode.OK)){
            return ResponseUtil.ok();
        } else {
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
            return ResponseUtil.fail(responseCode);
        }
    }

}
