package cn.edu.xmu.oomall.other.service;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ImgHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.AdvertiseDao;
import cn.edu.xmu.oomall.other.dao.AftersaleDao;
import cn.edu.xmu.oomall.other.model.bo.AdvertiseBo;
import cn.edu.xmu.oomall.other.model.bo.TimeSegmentBo;
import cn.edu.xmu.oomall.other.model.po.AdvertisementPo;
import cn.edu.xmu.oomall.other.model.po.FavouriteGoodsPo;
import cn.edu.xmu.oomall.other.model.po.TimeSegmentPo;
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
import org.springframework.web.bind.annotation.RequestParam;
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

    public ResponseCode setAdvertisementDefaultById(Long id){
        return advertiseDao.setAdvertisementDefaultById(id);
    }

    public ResponseCode updateAdvertisementById(AdvertiseVo advertiseVo){
        return advertiseDao.updateAdvertisementById(advertiseVo.createBo());
    }

    public ResponseCode deleteAdvertisementById(Long id){
        return advertiseDao.deleteAdvertisementById(id);
    }

    public List<AdvertiseBo> getCurrentAdvertisements() {
        TimeSegmentPo timeSeg=advertiseDao.getNowTimeSegment();
        if(timeSeg==null){
            return advertiseDao.getDefaultAd();
        }
        List<AdvertisementPo> advertisePo=advertiseDao.getAdvertisements(timeSeg.getId());
        if(advertisePo==null||advertisePo.size()==0)
            return advertiseDao.getDefaultAd();
        return advertisePo.stream().map(AdvertiseBo::new).collect(Collectors.toList());
    }

    public ResponseCode uploadAdvertiseImgById(Long id, MultipartFile multipartFile) {
        AdvertiseBo bo = advertiseDao.getAdvertiseById(id);
        if(bo==null)return ResponseCode.RESOURCE_ID_NOTEXIST;
        try{
            System.out.println(davUsername+davPassword+baseUrl);
            ReturnObject returnObject = ImgHelper.remoteSaveImg(multipartFile,2,davUsername,davPassword,baseUrl);
            if(!returnObject.getCode().equals(ResponseCode.OK))
                return returnObject.getCode();
            String oldFilename = bo.getImageUrl();
            bo.setImageUrl(baseUrl+returnObject.getData().toString());
            ResponseCode updateRetCode=advertiseDao.updateAdvertisementById(bo);
            if(!updateRetCode.equals(ResponseCode.OK)){
                ImgHelper.deleteRemoteImg(returnObject.getData().toString(),davUsername,davPassword,"");
                return updateRetCode;
            }
            if(oldFilename!=null){
                ImgHelper.deleteRemoteImg(oldFilename,davUsername,davPassword,"");
            }
        }catch (Exception e){
            return ResponseCode.FILE_NO_WRITE_PERMISSION;
        }
        return ResponseCode.OK;
    }

    public ReturnObject<PageInfo<VoObject>> getAdvertiseByTimeSegmentId(Long id, LocalDate beginDate, LocalDate endDate, Integer page, Integer pageSize) {
        List<AdvertisementPo> pos=advertiseDao.getAdvertiseByTimeSegmentId(id,beginDate,endDate,page,pageSize);
        PageInfo<AdvertisementPo> favoritesPoPage = PageInfo.of(pos);
        List<VoObject> ret=pos.stream().map(AdvertiseBo::new).collect(Collectors.toList());
        PageInfo<VoObject> favoritesPage = new PageInfo<>(ret);
        favoritesPage.setPages(favoritesPoPage.getPages());
        favoritesPage.setPageNum(favoritesPoPage.getPageNum());
        favoritesPage.setPageSize(favoritesPoPage.getPageSize());
        favoritesPage.setTotal(favoritesPoPage.getTotal());
        return  new ReturnObject<>(favoritesPage);
    }

    public Object createAdvertiseByTimeSegId(Long segId, AdvertiseVo advertiseVo) {
        TimeSegmentPo segPo=advertiseDao.getTimeSeg(segId);
        if(segPo==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        return advertiseDao.createAdvertiseInTimeSegId(segId,advertiseVo.createBo()).createVo();
    }

    public Object addAdvertiseToSeg(Long segId, Long adId) {
        TimeSegmentPo segPo=advertiseDao.getTimeSeg(segId);
        if(segPo==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        return advertiseDao.addAdvertiseToSeg(segId,adId).createVo();
    }

    public ResponseCode onshelvesAdvertisementById(Long id) {
        AdvertiseBo bo = advertiseDao.getAdvertiseById(id);
        if(bo==null)return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(bo.getState()==AdvertiseBo.State.FORBID){
            bo.setState(AdvertiseBo.State.NORM);
            return  advertiseDao.updateAdvertisementById(bo);
        }
        else return ResponseCode.ADVERTISEMENT_STATENOTALLOW;
    }

    public ResponseCode offshelvesAdvertisementById(Long id){
        AdvertiseBo bo = advertiseDao.getAdvertiseById(id);
        if(bo==null)return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(bo.getState()==AdvertiseBo.State.NORM){
            bo.setState(AdvertiseBo.State.FORBID);
            return  advertiseDao.updateAdvertisementById(bo);
        }
        else return ResponseCode.ADVERTISEMENT_STATENOTALLOW;
    }

    public ResponseCode auditAdvertisementById(Long id){
        AdvertiseBo bo = advertiseDao.getAdvertiseById(id);
        if(bo==null)return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(bo.getState()==AdvertiseBo.State.BACK){
            bo.setState(AdvertiseBo.State.FORBID);
            return  advertiseDao.updateAdvertisementById(bo);
        }
        else return ResponseCode.ADVERTISEMENT_STATENOTALLOW;
    }
}
