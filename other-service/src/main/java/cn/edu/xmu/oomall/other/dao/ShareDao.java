package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.dto.EffectiveShareDto;
import cn.edu.xmu.oomall.dto.ShareDto;
import cn.edu.xmu.oomall.other.mapper.*;
import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import cn.edu.xmu.oomall.other.model.bo.ShareBo;
import cn.edu.xmu.oomall.other.model.po.*;
import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
import cn.edu.xmu.oomall.other.service.factory.CalcPoint;
import cn.edu.xmu.oomall.other.service.factory.CalcPointFactory;
import cn.edu.xmu.oomall.other.util.DefaultRedisFinder;
import cn.edu.xmu.oomall.other.util.ShareActivityRedisFinder;
import cn.edu.xmu.oomall.other.util.ShopRedisFinder;
import cn.edu.xmu.oomall.other.util.SkuRedisFinder;
import cn.edu.xmu.goods.client.IGoodsService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author Jx
 * @version 创建时间：2020/12/5 下午3:10
 */
@Repository
public class ShareDao implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(ShareDao.class);

    @Autowired
    private BeSharePoMapper beSharePoMapper;

    @Autowired
    private ShareActivityPoMapper shareActivityPoMapper;

    @Autowired
    SharePoMapper sharePoMapper;

    @Autowired
    CustomerPoMapper customerPoMapper;

    @Autowired
    UpdateRebateMapper updateRebateMapper;

    @DubboReference(version = "0.0.1-SNAPSHOT", check = false)
    IGoodsService goodsService;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Autowired
    RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    CalcPointFactory calcPointFactory;

    ShareActivityRedisFinder redisFinder;

    @Value("${share-activity.enable-redis}")
    boolean redisEnable;

    private Byte offline = 0;

    private Byte online = 1;

    public boolean createBeShare(Long customerId,Long skuId,Long shareId){
        SharePoExample example=new SharePoExample();
        SharePoExample.Criteria criteria=example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(skuId);
        criteria.andIdEqualTo(shareId);
        List<SharePo> sharePos=sharePoMapper.selectByExample(example);
        if(sharePos.size()==0)return false;
        SharePo sharePo=sharePos.get(0);
        BeSharePo beSharePo=new BeSharePo();
        beSharePo.setCustomerId(customerId);
        beSharePo.setGmtCreate(LocalDateTime.now());
        beSharePo.setGoodsSkuId(skuId);
        beSharePo.setShareActivityId(sharePo.getShareActivityId());
        beSharePo.setShareId(sharePo.getId());
        beSharePo.setSharerId(sharePo.getSharerId());
        String message=JacksonUtil.toJson(beSharePo);
        rocketMQTemplate.sendOneWay("createBeShare-topic",message);
        return true;
    }
    public PageInfo<BeSharePo> findBeShare(Long userId, Long skuId, LocalDateTime beginTime, LocalDateTime endTime) {
        BeSharePoExample example=new BeSharePoExample();
        BeSharePoExample.Criteria criteria= example.createCriteria();
        if(userId!=null){
            criteria.andSharerIdEqualTo(userId);
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
        return new PageInfo<>(beSharePos);
    }
    public PageInfo<SharePo> findShares(Long userId, Long skuId, LocalDateTime beginTime, LocalDateTime endTime){
        SharePoExample example=new SharePoExample();
        SharePoExample.Criteria criteria=example.createCriteria();
        if(userId!=null){
            criteria.andSharerIdEqualTo(userId);
        }
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
        return new PageInfo<>(sharePos);
    }
    /*在下单时查找第一个有效的分享成功记录*/
    public ShareDto getFirstBeShared(Long customerId, Long skuId, Long orderItemId) {
        BeSharePoExample example=new BeSharePoExample();
        BeSharePoExample.Criteria criteria=example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);
        criteria.andGoodsSkuIdEqualTo(skuId);
        //criteria.andOrderIdEqualTo(null);
        criteria.andOrderIdIsNull();
        List<BeSharePo> beSharePos=beSharePoMapper.selectByExample(example);
        if(beSharePos.size()==0)return new ShareDto(orderItemId,customerId,skuId,null);
        BeSharePo po=beSharePos.get(0);
        po.setOrderId(orderItemId);
        beSharePoMapper.updateByPrimaryKey(po);
        return new ShareDto(po.getOrderId(),po.getCustomerId(),po.getGoodsSkuId(),po.getId());
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
        if(redisEnable){
            logger.debug("cleaning redis...");
            redisTemplate.delete("ssa_" + newVal.getId());
            logger.debug("chain deleting newVal:"+JacksonUtil.toJson(newVal));
            redisFinder.chainDelete(oldVal.getGoodsSkuId(),oldVal.getShopId());
            logger.debug("deleting success");
        }
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
        //newVal中有更新后的值
        logger.debug(JacksonUtil.toJson(newVal));
        if(redisEnable){
            redisTemplate.delete("ssa_" + newVal.getId());
            redisFinder.chainDelete(oldVal.getGoodsSkuId(),oldVal.getShopId());
        }
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
        criteria.andStateEqualTo(online);
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
        logger.debug("find share activity total:"+shareActivityPos.size());
        return new PageInfo<>(shareActivityPos);

    }

    public void retPointByShareDTOS(List<EffectiveShareDto> shareDTOS) {
        Map<Long, CalcPoint> shareActivityMap=new HashMap<>();
        for(EffectiveShareDto shareDTO:shareDTOS){
            BeSharePo beSharePo=beSharePoMapper.selectByPrimaryKey(shareDTO.getBeSharedId());
            CalcPoint calcPoint;
            Long activityId=beSharePo.getShareActivityId();
            if(!shareActivityMap.containsKey(activityId)){
                calcPoint=calcPointFactory.getInstance(shareActivityPoMapper.selectByPrimaryKey(activityId).getStrategy());
                shareActivityMap.put(activityId,calcPoint);
            }
            else{
                calcPoint=shareActivityMap.get(activityId);
            }
            SharePo sharePo= sharePoMapper.selectByPrimaryKey(beSharePo.getShareId());
            Integer point=calcPoint.getPoint(shareDTO.getPrice(),shareDTO.getQuantity(),sharePo.getQuantity());
            sharePo.setQuantity(shareDTO.getQuantity()+sharePo.getQuantity());
            sharePoMapper.updateByPrimaryKey(sharePo);
            updateRebateMapper.updateRebateByPrimaryKey(sharePo.getSharerId(),Long.valueOf(point));
        }
    }
    public ShareActivityBo loadShareActivity(Long skuId){
        ShareActivityBo bo = null;
        if(redisEnable){
            logger.debug("redis searching...");
            bo=redisFinder.getBoFromRedis(skuId,null);
        }
        else{
            bo=getValidShareActivityBySkuId(skuId);
            if(bo==null)bo=getValidDefaultShareActivityByShopId(goodsService.getShopBySKUId(skuId).getId());
            if(bo==null)bo=getValidDefaultShareActivityByShopId(0L);
        }
        return bo;
    }

    /**
     * 找到当前或下一个满足skuid和shopid的活动 shopid为0表示平台
     * @param skuId
     * @param shopId
     * @return
     */
    public ShareActivityBo findNowOrNextFirstActivity(Long skuId,Long shopId){
        LocalDateTime now=LocalDateTime.now();
        ShareActivityPoExample nowExample =new ShareActivityPoExample();
        ShareActivityPoExample.Criteria nowCriteria= nowExample.createCriteria();
        nowCriteria.andBeginTimeLessThanOrEqualTo(now);
        nowCriteria.andEndTimeGreaterThan(now);
        nowCriteria.andStateEqualTo(online);
        if(skuId!=null){
            nowCriteria.andGoodsSkuIdEqualTo(skuId);
        }
        if(shopId!=null){
            nowCriteria.andShopIdEqualTo(shopId);
        }
        List<ShareActivityPo> nowPos=shareActivityPoMapper.selectByExample(nowExample);
        if(nowPos.size()>0){
            return new ShareActivityBo(nowPos.get(0));
        }
        ShareActivityPoExample nextExample=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria nextCriteria=nextExample.createCriteria();
        nextCriteria.andBeginTimeGreaterThan(now);
        nextCriteria.andStateEqualTo(online);
        if(skuId!=null){
            nextCriteria.andGoodsSkuIdEqualTo(skuId);
        }
        if(shopId!=null){
            nextCriteria.andShopIdEqualTo(shopId);
        }
        nextExample.setOrderByClause("begin_time asc");
        List<ShareActivityPo> nextPos=shareActivityPoMapper.selectByExample(nextExample);
        if(nextPos.size()>0){
            return new ShareActivityBo(nextPos.get(0));
        }
        else return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ShareActivityRedisFinder.init(redisTemplate,goodsService,this);
        ShareActivityRedisFinder skuFinder=new SkuRedisFinder("sku","shop"),
                shopFinder=new ShopRedisFinder("shop"),
                defaultFinder=new DefaultRedisFinder("default");
        redisFinder=skuFinder;
        skuFinder.setNext(shopFinder);
        shopFinder.setNext(defaultFinder);
    }

    public ReturnObject<VoObject> createShare(Long skuId, Long customerId, ShareActivityBo shareActivityBo) {
        ShareBo bo=null;
        SharePo po=null;
        if(redisEnable) {
            bo= (ShareBo) redisTemplate.opsForHash().get("ssa_" + shareActivityBo.getId(),String.valueOf(customerId));
        }
        if(bo==null){
            SharePoExample example=new SharePoExample();
            SharePoExample.Criteria criteria=example.createCriteria();
            criteria.andGoodsSkuIdEqualTo(skuId);
            criteria.andSharerIdEqualTo(customerId);
            criteria.andShareActivityIdEqualTo(shareActivityBo.getId());
            List<SharePo> sharePos=sharePoMapper.selectByExample(example);
            if(sharePos.size()>0)po=sharePos.get(0);
            else{
                po=new SharePo();
                po.setQuantity(0);
                po.setGmtCreate(LocalDateTime.now());
                po.setGoodsSkuId(skuId);
                po.setSharerId(customerId);
                po.setShareActivityId(shareActivityBo.getId());
                sharePoMapper.insertSelective(po);
            }

        }
        if(redisEnable){
            if(po!=null) {
                redisTemplate.opsForHash().put("ssa_" + po.getShareActivityId(),String.valueOf(customerId),new ShareBo(po));
                redisTemplate.expire("ssa_" + po.getShareActivityId(),
                        ShareActivityRedisFinder.getExpireTime(shareActivityBo.getEndTime()), TimeUnit.SECONDS);
            }
        }
        ShareBo ret=bo==null?new ShareBo(po):bo;
        ret.setSku(new GoodsSkuSimpleVo(goodsService.getSku(ret.getSkuId())));
        return new ReturnObject<>(ret);

    }
}
