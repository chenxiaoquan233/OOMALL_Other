package cn.edu.xmu.oomall.other.service;


import cn.edu.xmu.ooad.util.ImgHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.AdvertiseDao;
import cn.edu.xmu.oomall.other.dao.AftersaleDao;
import cn.edu.xmu.oomall.other.model.bo.AdvertiseBo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseRetVo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseStatesRetVo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/12 上午10:45
 */
@Service
public class AdvertiseService {
    private static final Logger logger = LoggerFactory.getLogger(AfterSaleService.class);

    //@Value("${privilegeservice.dav.username}")
    private String davUsername;

    //@Value("${privilegeservice.dav.password}")
    private String davPassword;

    //@Value("${privilegeservice.dav.baseUrl}")
    private String baseUrl;


    @Autowired
    private AdvertiseDao advertiseDao;

    public List<AdvertiseStatesRetVo> getAllAdvertisementStates(){
        return advertiseDao.getAllAdvertiseState();
    }

    public ResponseCode setAdvertisementDefaultById(Integer id){
        return advertiseDao.setAdvertisementDefaultById(id.longValue());
    }

    public ResponseCode updateAdvertisementById(AdvertiseVo advertiseVo){
        return advertiseDao.updateAdvertisementById(advertiseVo.createBo());
    }

    public ResponseCode deleteAdvertisementById(Integer id){
        return advertiseDao.deleteAdvertisementById(id.longValue());
    }

    public List<Object> getCurrentAdvertisements() {
        return advertiseDao.getCurrentAdvertisements().stream().map(x->x.createVo()).collect(Collectors.toList());
    }

    public ResponseCode uploadAdvertiseImgById(Integer id, MultipartFile multipartFile) {
        AdvertiseBo bo = advertiseDao.getAdvertiseById(id.longValue());
        if(bo==null)return ResponseCode.RESOURCE_ID_NOTEXIST;
        try{
            ReturnObject returnObject = ImgHelper.remoteSaveImg(multipartFile,2,davUsername,davPassword,baseUrl);

            if(!returnObject.getCode().equals(ResponseCode.OK))return returnObject.getCode();

            String oldFilename = bo.getImageUrl();
            bo.setImageUrl(returnObject.getData().toString());
            ResponseCode updateRetCode=advertiseDao.updateAdvertisementById(bo);

            if(!updateRetCode.equals(ResponseCode.OK)){
                ImgHelper.deleteRemoteImg(returnObject.getData().toString(),davUsername,davPassword,baseUrl);
                return updateRetCode;
            }
            if(oldFilename!=null){
                ImgHelper.deleteRemoteImg(oldFilename,davUsername,davPassword,baseUrl);
            }
        }catch (Exception e){
            return ResponseCode.FILE_NO_WRITE_PERMISSION;
        }
        return ResponseCode.INTERNAL_SERVER_ERR;
    }

    public Object getAdvertiseByTimeSegmentId(Integer id, LocalDate beginDate, LocalDate endDate, Integer page, Integer pageSize) {
        return advertiseDao.getAdvertiseByTimeSegmentId(id.longValue(),beginDate,endDate,page,pageSize);
    }

    public ResponseCode createAdvertiseByTimeSegId(Integer id, AdvertiseVo advertiseVo) {
        return advertiseDao.createAdvertiseByTimeSegId(id.longValue(),advertiseVo.createBo());
    }

    public ResponseCode createAdvertiseByTimeSegIdAndId(Integer id, Integer tid) {
        return  advertiseDao.createAdvertiseByTimeSegIdAndId(id.longValue(),tid.longValue());
    }

    public ResponseCode onshelvesAdvertisementById(Integer id) {
        AdvertiseBo bo = advertiseDao.getAdvertiseById(id.longValue());
        if(bo==null)return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(bo.getState()==AdvertiseBo.State.FORBID){
            bo.setState(AdvertiseBo.State.NORM);
            return  advertiseDao.updateAdvertisementById(bo);
        }
        else return ResponseCode.ADVERTISEMENT_STATENOTALLOW;
    }

    public ResponseCode offshelvesAdvertisementById(Integer id){
        AdvertiseBo bo = advertiseDao.getAdvertiseById(id.longValue());
        if(bo==null)return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(bo.getState()==AdvertiseBo.State.NORM){
            bo.setState(AdvertiseBo.State.FORBID);
            return  advertiseDao.updateAdvertisementById(bo);
        }
        else return ResponseCode.ADVERTISEMENT_STATENOTALLOW;
    }

    public ResponseCode auditAdvertisementById(Integer id){
        AdvertiseBo bo = advertiseDao.getAdvertiseById(id.longValue());
        if(bo==null)return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(bo.getState()==AdvertiseBo.State.BACK){
            bo.setState(AdvertiseBo.State.FORBID);
            return  advertiseDao.updateAdvertisementById(bo);
        }
        else return ResponseCode.ADVERTISEMENT_STATENOTALLOW;
    }
}
