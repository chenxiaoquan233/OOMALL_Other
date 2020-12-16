package cn.edu.xmu.oomall.other.util;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.oomall.other.dao.ShareDao;
import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import cn.edu.xmu.goods.client.IGoodsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/14 下午4:53
 * 4
 */
@Data
@Slf4j
public abstract class ShareActivityRedisFinder {
    protected String redisName;
    ShareActivityRedisFinder next;
    protected static RedisTemplate redisTemplate;
    protected static IGoodsService goodsService;
    protected static ShareDao shareDao;
    public static void init(RedisTemplate template,IGoodsService service,ShareDao dao){
        redisTemplate=template;
        goodsService=service;
        shareDao=dao;

    }
    public ShareActivityRedisFinder(String redisName){
        this.redisName=redisName;
    }
    final static Long expireTime=600L;
    public static Long getExpireTime(LocalDateTime time){
        if(time==null)return expireTime;
        LocalDateTime now=LocalDateTime.now();
        Long gapTime= Duration.between(now,time).toSeconds();
        return Math.min(expireTime,gapTime);
    }
    public abstract void chainDelete(Long skuId, Long shopId);
    public abstract ShareActivityBo getNext(Long id);
    public ShareActivityBo getBoFromRedis(Long skuId, Long shopId){
        ShareActivityBo bo = null;
        LocalDateTime now=LocalDateTime.now();
        Long id=((skuId==null||skuId==0L)?shopId:skuId);
        log.debug("searching in "+redisName+", id:"+id);
        if(!hashKey(id)){
            log.debug("searching in mysql processing");
            bo=shareDao.findNowOrNextFirstActivity(skuId,shopId);
            log.debug("searching in mysql finish,get result:"+ JacksonUtil.toJson(bo));
            if(bo==null)setRedis(id,null,null);
            else if(bo.getBeginTime().isAfter(now)){
                setRedis(id,bo,bo.getBeginTime());
            }
            else{
                setRedis(id,bo,bo.getEndTime());
            }
        }
        else{
            bo=getRedis(id);
        }
        log.debug("searching result bo:"+JacksonUtil.toJson(bo));
        if(bo==null||bo.getBeginTime().isAfter(now))return getNext(getParentId(id));
        else return bo;
    }
    public abstract Long getParentId(Long id);
    public void setRedis(Long id,ShareActivityBo bo,LocalDateTime time){
        log.debug("putting redis:"+redisName+id+"bo:"+JacksonUtil.toJson(bo));
        redisTemplate.opsForValue().set(redisName+String.valueOf(id),bo);
        redisTemplate.expire(redisName+String.valueOf(id),getExpireTime(time),TimeUnit.SECONDS);
        //redisTemplate.opsForValue().put(redisName,String.valueOf(id),bo);
    }
    public ShareActivityBo getRedis(Long id){
        log.debug("gutting redis:"+redisName+id);
        return (ShareActivityBo) redisTemplate.opsForValue().get(redisName+String.valueOf(id));
        //return (ShareActivityBo) redisTemplate.opsForHash().get(redisName,String.valueOf(id));
    }
    public void deleteRedis(Long id){
        log.debug("is deleting:"+redisName+id);
        redisTemplate.delete(redisName+String.valueOf(id));
    }
    public boolean hashKey(Long id){
        return redisTemplate.hasKey(redisName+String.valueOf(id));
    }
}
