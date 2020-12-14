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
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import javax.imageio.ImageIO;
import javax.swing.text.DateFormatter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/12 上午10:45
 */
@Service
public class AdvertiseService {

    private static final Logger logger = LoggerFactory.getLogger(AdvertiseService.class);

    @Value("${advertisement-service.dav.username}")
    private String davUsername;

    @Value("${advertisement-service.dav.password}")
    private String davPassword;

    @Value("${advertisement-service.dav.baseUrl}")
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
        System.out.println("Bo Ok");
        try{
            System.out.println(davUsername+davPassword+baseUrl);
            ReturnObject returnObject = ImgHelper.remoteSaveImg(multipartFile,2,davUsername,davPassword,baseUrl);
            System.out.println("Save Img");
            if(!returnObject.getCode().equals(ResponseCode.OK))
                return returnObject.getCode();
            System.out.println("Save Img Ok");
            String oldFilename = bo.getImageUrl();
            bo.setImageUrl(baseUrl+returnObject.getData().toString());
            ResponseCode updateRetCode=advertiseDao.updateAdvertisementById(bo);
            System.out.println("Update DB");
            if(!updateRetCode.equals(ResponseCode.OK)){
                ImgHelper.deleteRemoteImg(returnObject.getData().toString(),davUsername,davPassword,"");
                return updateRetCode;
            }
            System.out.println("Update DB Ok");
            if(oldFilename!=null){
                ImgHelper.deleteRemoteImg(oldFilename,davUsername,davPassword,"");
            }
            System.out.println("remove old File Ok");
        }catch (Exception e){
            System.out.println("Exception");
            return ResponseCode.FILE_NO_WRITE_PERMISSION;
        }
        return ResponseCode.OK;
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
