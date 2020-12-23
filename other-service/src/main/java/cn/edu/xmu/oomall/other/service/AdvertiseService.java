package cn.edu.xmu.oomall.other.service;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ImgHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.AdvertiseDao;
import cn.edu.xmu.oomall.other.model.bo.AdvertiseBo;
import cn.edu.xmu.oomall.other.model.bo.TimeSegmentBo;
import cn.edu.xmu.oomall.other.model.po.AdvertisementPo;
import cn.edu.xmu.oomall.other.model.po.TimeSegmentPo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseAuditVo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseStatesRetVo;
import cn.edu.xmu.oomall.other.model.vo.Advertisement.AdvertiseVo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Jiang Xiao
 * @version 创建时间：2020/12/12 上午10:45
 */
@Service
public class AdvertiseService {
    private static final Logger logger = LoggerFactory.getLogger(AdvertiseService.class);

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

    public ResponseCode setAdvertisementDefaultById(Long id){
        return advertiseDao.setAdvertisementDefaultById(id);
    }

    public ResponseCode updateAdvertisementById(Long id,AdvertiseVo advertiseVo){
        AdvertiseBo oldBo = advertiseDao.getAdvertiseById(id);
        if(oldBo==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        AdvertiseBo bo=advertiseVo.createBo();
        bo.setId(id);
        if(bo.getBeginDate()==null)
            bo.setBeginDate(oldBo.getBeginDate());
        if(bo.getEndDate()==null)
            bo.setEndDate(oldBo.getEndDate());
        bo.setState(AdvertiseBo.State.BACK);
        return advertiseDao.updateAdvertisementById(bo);
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
            ReturnObject returnObject = ImgHelper.remoteSaveImg(multipartFile,2,davUsername,davPassword,baseUrl);

            if(!returnObject.getCode().equals(ResponseCode.OK))return returnObject.getCode();

            String oldFilename = bo.getImageUrl();
            bo.setImageUrl(returnObject.getData().toString());
            bo.setState(AdvertiseBo.State.BACK);
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

    public ReturnObject<PageInfo<VoObject>> getAdvertiseByTimeSegmentId(Long segId, LocalDate beginDate, LocalDate endDate, Integer page, Integer pageSize) {
        if(!segId.equals(0L))
        {
            TimeSegmentPo segPo = advertiseDao.getTimeSeg(segId);
            if (segPo == null)
                return null;
        }
        List<AdvertisementPo> pos=advertiseDao.getAdvertiseByTimeSegmentId(segId,beginDate,endDate,page,pageSize);
        PageInfo<AdvertisementPo> favoritesPoPage = PageInfo.of(pos);
        List<VoObject> ret=pos.stream().map(AdvertiseBo::new)
                //.filter(x -> x.getRepeats() ||(x.getBeginDate().isAfter(beginDate)&&x.getEndDate().isBefore(endDate)))
                .collect(Collectors.toList());
        PageInfo<VoObject> favoritesPage = new PageInfo<>(ret);
        favoritesPage.setPages(favoritesPoPage.getPages());
        favoritesPage.setPageNum(favoritesPoPage.getPageNum());
        favoritesPage.setPageSize(favoritesPoPage.getPageSize());
        favoritesPage.setTotal(favoritesPoPage.getTotal());
        return  new ReturnObject<>(favoritesPage);
    }

    public Object createAdvertiseByTimeSegId(Long segId, AdvertiseVo advertiseVo) {
        if(!segId.equals(0L))
        {
            TimeSegmentPo segPo = advertiseDao.getTimeSeg(segId);
            if (segPo == null || segPo.getType()==(TimeSegmentBo.Type.FLASHSALE.getCode().byteValue()))
                return ResponseCode.RESOURCE_ID_NOTEXIST;
        }
        return advertiseDao.createAdvertiseInTimeSegId(segId,advertiseVo.createBo()).createVo();
    }

    public Object addAdvertiseToSeg(Long segId, Long adId) {
        if(!segId.equals(0L))
        {
            TimeSegmentPo segPo = advertiseDao.getTimeSeg(segId);
            if (segPo == null || segPo.getType()==(TimeSegmentBo.Type.FLASHSALE.getCode().byteValue()))
                return ResponseCode.RESOURCE_ID_NOTEXIST;
        }
        AdvertiseBo advertiseBo= advertiseDao.addAdvertiseToSeg(segId,adId);
        if(advertiseBo==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        return advertiseBo.createVo();
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

    public ResponseCode auditAdvertisementById(Long id, AdvertiseAuditVo vo){
        AdvertiseBo bo = advertiseDao.getAdvertiseById(id);
        if(bo==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(bo.getState().equals(AdvertiseBo.State.BACK)){
            bo.setMessage(vo.getMessage());
            if(vo.getConclusion())
                bo.setState(AdvertiseBo.State.FORBID);
            return  advertiseDao.updateAdvertisementById(bo);
        }
        else return ResponseCode.ADVERTISEMENT_STATENOTALLOW;
    }
}
