package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.dto.BeSharedDTO;
import cn.edu.xmu.oomall.other.mapper.BeSharePoMapper;
import cn.edu.xmu.oomall.other.mapper.ShareActivityPoMapper;
import cn.edu.xmu.oomall.other.mapper.SharePoMapper;
import cn.edu.xmu.oomall.other.model.bo.BeSharedBo;
import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import cn.edu.xmu.oomall.other.model.po.*;
import cn.edu.xmu.oomall.other.model.vo.ShareActivity.ShareActivityRetVo;
import cn.edu.xmu.oomall.other.model.vo.ShareActivity.ShareActivityVo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.accessibility.AccessibleRelation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Jx
 * @version 创建时间：2020/12/5 下午3:10
 */
@Repository
public class ShareDao {
    private static final Logger logger = LoggerFactory.getLogger(ShareDao.class);

    @Autowired
    private BeSharePoMapper beSharePoMapper;

    @Autowired
    private ShareActivityPoMapper shareActivityPoMapper;

    @Autowired
    SharePoMapper sharePoMapper;

    private Byte offline = 0;

    private Byte online = 1;

    public PageInfo<BeSharePo> findBeShare(Long userId, Long shopId, Long skuId, LocalDateTime beginTime, LocalDateTime endTime) {
        BeSharePoExample example=new BeSharePoExample();
        BeSharePoExample.Criteria criteria= example.createCriteria();
        if(userId!=null){
            criteria.andCustomerIdEqualTo(userId);
        }
        if(skuId!=null){
            criteria.andGoodsSkuIdEqualTo(skuId);
        }
        if(beginTime!=null){
            criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        }
        if(endTime!=null){
            criteria.andGmtCreateLessThanOrEqualTo(endTime);
        }
        List<BeSharePo> beSharePos=beSharePoMapper.selectByExample(example);
        if(shopId!=null){
            if(shopId!=0){
                //Todo:检查shopid
            }

        }
        return new PageInfo<>(beSharePos);
    }
    public PageInfo<SharePo> findShares(Long skuId,Long shopId, LocalDateTime beginTime, LocalDateTime endTime){
        SharePoExample example=new SharePoExample();
        SharePoExample.Criteria criteria=example.createCriteria();
        if(beginTime!=null){
            criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        }
        if(endTime!=null){
            criteria.andGmtCreateLessThanOrEqualTo(endTime);
        }
        if(skuId!=null){
            criteria.andGoodsSkuIdEqualTo(skuId);
        }

        List<SharePo> sharePos= sharePoMapper.selectByExample(example);
        if(shopId!=null){
            if(shopId!=0){
                ShareActivityPoExample activityPoExample=new ShareActivityPoExample();
                ShareActivityPoExample.Criteria criteria1=activityPoExample.createCriteria();
                criteria1.andShopIdEqualTo(shopId);
                //TODO:需要完成查询商店下所有活动的逻辑
            }


        }
        return new PageInfo<>(sharePos);
    }
    /*在下单时查找第一个有效的分享成功记录*/
    public BeSharedDTO getFirstBeShared(Long customerId, Long skuId, Long orderItemId) {
        BeSharePoExample example=new BeSharePoExample();
        BeSharePoExample.Criteria criteria=example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);
        criteria.andGoodsSkuIdEqualTo(skuId);
        criteria.andOrderIdEqualTo(0L);
        List<BeSharePo> beSharePos=beSharePoMapper.selectByExample(example);
        BeSharePo po=beSharePos.get(0);
        po.setOrderId(orderItemId);
        beSharePoMapper.updateByPrimaryKey(po);
        return new BeSharedDTO(po.getOrderId(), po.getGoodsSkuId(),po.getId(),po.getCustomerId());
    }

    public ShareActivityBo getShareActivityById(Long id){
        return new ShareActivityBo(shareActivityPoMapper.selectByPrimaryKey(id));
    }

    /*查询sku历史所有的分享活动*/
    public List<ShareActivityBo> getAllShareActivityBySkuId(Long skuId){
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria=example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(skuId);
        List<ShareActivityPo> pos=shareActivityPoMapper.selectByExample(example);
        if(pos==null)
            return null;
        return pos.stream().map(ShareActivityBo::new).collect(Collectors.toList());
    }

    /*查询sku当前生效的分享活动*/
    public ShareActivityBo getValidShareActivityBySkuId(Long skuId){
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria=example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(skuId);
        criteria.andStateEqualTo(online); //状态上架
        criteria.andBeginTimeLessThan(LocalDateTime.now());
        criteria.andEndTimeGreaterThan(LocalDateTime.now());
        List<ShareActivityPo> pos=shareActivityPoMapper.selectByExample(example);
        if(pos==null) return null;
        return new ShareActivityBo(pos.get(0));
    }

    /*查询当前生效的店铺默认分享活动，0L表示平台*/
    public ShareActivityBo getValidDefaultShareActivityByShopId(Long shopId){
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria=example.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        criteria.andGoodsSkuIdEqualTo(0L); //默认分享的skuId为0
        criteria.andStateEqualTo(online); //状态上架
        criteria.andBeginTimeLessThan(LocalDateTime.now());
        criteria.andEndTimeGreaterThan(LocalDateTime.now());
        List<ShareActivityPo> pos=shareActivityPoMapper.selectByExample(example);
        if(pos==null) return null;
        return new ShareActivityBo(pos.get(0));
    }

    /*下架活动*/
    public ResponseCode offlineShareActivity(Long shopId,Long shareActivityId){
        ShareActivityPo oldVal=shareActivityPoMapper.selectByPrimaryKey(shareActivityId);
        /*资源不存在*/
        if(oldVal==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        /*操作资源不是自己对象*/
        if(!oldVal.getShopId().equals(shopId))
            return ResponseCode.RESOURCE_ID_OUTSCOPE;
        ShareActivityPo newVal=new ShareActivityPo();
        newVal.setId(shareActivityId);
        newVal.setState(offline);
        shareActivityPoMapper.updateByPrimaryKeySelective(newVal);
        return ResponseCode.OK;
    }

    /*上架活动*/
    public ResponseCode onlineShareActivity(Long shopId,Long shareActivityId){
        ShareActivityPo oldVal=shareActivityPoMapper.selectByPrimaryKey(shareActivityId);
        /*资源不存在*/
        if(oldVal==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        /*操作资源不是自己对象*/
        if(!oldVal.getShopId().equals(shopId))
            return ResponseCode.RESOURCE_ID_OUTSCOPE;
        /*试图上架的活动时间与已有活动冲突*/
        if(ifTimeConflict(shopId,oldVal.getGoodsSkuId(),oldVal.getBeginTime(),oldVal.getEndTime()))
            return ResponseCode.SHAREACT_CONFLICT;
        ShareActivityPo newVal=new ShareActivityPo();
        newVal.setId(shareActivityId);
        newVal.setState(online);
        shareActivityPoMapper.updateByPrimaryKeySelective(newVal);
        return ResponseCode.OK;
    }

    /*修改分享活动，由于没有分享活动状态禁止的错误码，采用内部错误代替*/
    public ResponseCode updateShareActivity(Long shopId,Long shareActivityId,ShareActivityPo newVal){
        ShareActivityPo oldVal=shareActivityPoMapper.selectByPrimaryKey(shareActivityId);
        /*资源不存在*/
        if(oldVal==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        /*操作资源不是自己对象*/
        if(!oldVal.getShopId().equals(shopId))
            return ResponseCode.RESOURCE_ID_OUTSCOPE;
        /*分享活动还在上架状态*/
        if(oldVal.getState().equals(online))
            return ResponseCode.INTERNAL_SERVER_ERR;
        newVal.setId(shareActivityId);
        shareActivityPoMapper.updateByPrimaryKeySelective(newVal);
        return ResponseCode.OK;
    }

    /*判断该时间段是否与已有时间段冲突*/
    private Boolean ifTimeConflict(Long shopId,Long goodsSkuId,LocalDateTime beginTime,LocalDateTime endTime){
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria=example.createCriteria();
        /*冲突活动的要求:开始时间在新活动的结束时间之前，结束时间在新活动的开始活动之后*/
        criteria.andBeginTimeLessThan(endTime);
        criteria.andEndTimeGreaterThan(beginTime);
        /*是默认分享，查找shopId*/
        if(goodsSkuId==0)
            criteria.andShopIdEqualTo(shopId);
            /*不是默认分享，查找skuId*/
        else criteria.andGoodsSkuIdEqualTo(goodsSkuId);
        long count=shareActivityPoMapper.countByExample(example);
        return count > 0;
    }

    /*新建分享活动：默认为下架*/
    public ReturnObject<VoObject> insertShareActivity(ShareActivityBo shareActivity){
        ShareActivityPo record=shareActivity.createPo();
        record.setGmtCreate(LocalDateTime.now());
        shareActivityPoMapper.insert(record);
        return new ReturnObject<>(new ShareActivityBo(record));
    }


    public PageInfo<ShareActivityPo> findShareActivity(Long shopId, Long skuId) {
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria=example.createCriteria();
        if(shopId!=null){
            criteria.andShopIdEqualTo(shopId);
        }
        if(skuId!=null){
            criteria.andGoodsSkuIdEqualTo(skuId);
        }
        List<ShareActivityPo> shareActivityPos=shareActivityPoMapper.selectByExample(example);
        return new PageInfo<>(shareActivityPos);

    }
}
