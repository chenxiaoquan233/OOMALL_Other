package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.FavoriteDao;
import cn.edu.xmu.oomall.other.dao.ShareDao;
import cn.edu.xmu.oomall.other.model.bo.BeSharedBo;
import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import cn.edu.xmu.oomall.other.model.bo.ShareBo;
import cn.edu.xmu.oomall.other.model.po.BeSharePo;
import cn.edu.xmu.oomall.other.model.po.ShareActivityPo;
import cn.edu.xmu.oomall.other.model.po.SharePo;
import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
import cn.edu.xmu.oomall.other.model.vo.Share.ShareRetVo;
import cn.edu.xmu.oomall.other.model.vo.ShareActivity.ShareActivityRetVo;
import cn.edu.xmu.oomall.other.model.vo.ShareActivity.ShareActivityVo;
import cn.xmu.edu.goods.client.IGoodsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jx
 * @version 创建时间：2020/12/5 下午2:57
 */
@Service
public class ShareService {
    private static final Logger logger = LoggerFactory.getLogger(ShareService.class);

    @DubboReference
    IGoodsService goodsService;
    @Autowired
    private ShareDao shareDao;

    public ResponseCode offlineShareActivity(Long shopId,Long shareActivityId){
        return shareDao.offlineShareActivity(shopId,shareActivityId);
    }

    public ResponseCode onlineShareActivity(Long shopId,Long shareActivityId){
        return shareDao.onlineShareActivity(shopId,shareActivityId);
    }

    public ReturnObject<VoObject> addShareActivity(Long shopId, Long skuId, ShareActivityVo vo){
        ShareActivityBo bo=vo.createBo();
        bo.setState((byte)0);
        bo.setShopId(shopId);
        bo.setGoodSkuId(skuId);
        return shareDao.insertShareActivity(bo);
    }
    public ReturnObject<PageInfo<VoObject>> findShares(Long skuId,Long shopId, LocalDateTime beginTime,LocalDateTime endTime,Integer page,Integer pageSize){
        PageHelper.startPage(page,pageSize,true,true,null);
        PageInfo<SharePo> sharePos=shareDao.findShares(skuId,shopId,beginTime,endTime);

        List<VoObject> shares=sharePos.getList().stream().map(ShareBo::new)
                .map(shareBo -> {shareBo.setSku(new GoodsSkuSimpleVo(goodsService.getSku(skuId)));return shareBo;
        }).collect(Collectors.toList());

        PageInfo<VoObject> retObj=new PageInfo<>(shares);
        retObj.setPageNum(sharePos.getPageNum());
        retObj.setPageSize(sharePos.getPageSize());
        retObj.setTotal(sharePos.getTotal());
        retObj.setPages(sharePos.getPages());
        return new ReturnObject<>(retObj);
    }

    public ReturnObject<PageInfo<VoObject>> getBeShared(Long userId, Long shopId, Long skuId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize,true,true,null);
        PageInfo<BeSharePo> beSharePos=shareDao.findBeShare(userId,shopId,skuId,beginTime,endTime);

        List<VoObject> beShares=beSharePos.getList().stream().map(BeSharedBo::new).map(beSharedBo -> {
            beSharedBo.setSku(new GoodsSkuSimpleVo(goodsService.getSku(skuId)));return beSharedBo;
        }).collect(Collectors.toList());

        PageInfo<VoObject> retObj=new PageInfo<>(beShares);
        retObj.setPageNum(beSharePos.getPageNum());
        retObj.setPageSize(beSharePos.getPageSize());
        retObj.setTotal(beSharePos.getTotal());
        retObj.setPages(beSharePos.getPages());
        return new ReturnObject<>(retObj);
    }
    public ReturnObject<PageInfo<VoObject>> getShareActivities(Long shopId, Long skuId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize,true,true,null);
        PageInfo<ShareActivityPo> shareActivityPos=shareDao.findShareActivity(shopId,skuId);

        List<VoObject> shareActivities=shareActivityPos.getList().stream().map(ShareActivityBo::new).collect(Collectors.toList());

        PageInfo<VoObject> retObj=new PageInfo<>(shareActivities);
        retObj.setPageNum(shareActivityPos.getPageNum());
        retObj.setPageSize(shareActivityPos.getPageSize());
        retObj.setTotal(shareActivityPos.getTotal());
        retObj.setPages(shareActivityPos.getPages());
        return new ReturnObject<>(retObj);
    }

    public ResponseCode updateShareActivity(Long shopId, Long shareActivityId, ShareActivityVo vo) {
        return shareDao.updateShareActivity(shopId,shareActivityId,vo.createBo().createPo());
    }



//    public BeSharedBo selectValidFirstBeShared(Long customerId, Long skuId){
//        /*根据skuId查找当前有效的分享活动*/
//        ShareActivityBo shareActivityBo=shareDao.getValidShareActivityBySkuId(skuId);
//        /*如果没有，查找店铺默认分享活动*/
//        if(shareActivityBo==null)
//            shareActivityBo=shareDao.getValidDefaultShareActivityByShopId(2L); //这里需要去调用商品模块查shopId
//        /*如果没有，查找平台默认分享活动*/
//        if(shareActivityBo==null)
//            shareActivityBo=shareDao.getValidDefaultShareActivityByShopId(0L);
//        if(shareActivityBo==null)
//            return null;
//        /*查找点击者在该活动内第一个分享成功记录*/
//        BeSharedBo beSharedBo=shareDao.getFirstBeShared(customerId, skuId);
//        return beSharedBo;
//    }



}
