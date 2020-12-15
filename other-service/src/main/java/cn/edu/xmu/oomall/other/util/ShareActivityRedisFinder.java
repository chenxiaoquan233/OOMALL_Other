package cn.edu.xmu.oomall.other.util;

import cn.edu.xmu.oomall.other.dao.ShareDao;
import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import cn.edu.xmu.goods.client.IGoodsService;
import lombok.Data;
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
        return Math.max(expireTime,gapTime);
    }
    public abstract void deleteNext(Long skuId, Long shopId);
    public abstract ShareActivityBo getNext(Long id);
    public ShareActivityBo getBoFromRedis(Long skuId, Long shopId){
        ShareActivityBo bo = null;
        LocalDateTime now=LocalDateTime.now();
        Long id=(skuId==null?shopId:skuId);
        if(!hashKey(id)){
            bo=shareDao.findNowOrNextFirstActivity(skuId,shopId);
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

        if(bo==null||bo.getBeginTime().isAfter(now))return next.getNext(getParentId(id));
        else return bo;
    }
    public abstract Long getParentId(Long id);
    public void setRedis(Long id,ShareActivityBo bo,LocalDateTime time){
        redisTemplate.opsForValue().set(redisName+String.valueOf(id),bo);
        redisTemplate.expire(redisName+String.valueOf(id),getExpireTime(time),TimeUnit.SECONDS);
        //redisTemplate.opsForValue().put(redisName,String.valueOf(id),bo);
    }
    public ShareActivityBo getRedis(Long id){
        return (ShareActivityBo) redisTemplate.opsForValue().get(redisName+String.valueOf(id));
        //return (ShareActivityBo) redisTemplate.opsForHash().get(redisName,String.valueOf(id));
    }
    public void deleteRedis(Long id){
        redisTemplate.delete(redisName+String.valueOf(id));
    }
    public boolean hashKey(Long id){
        return redisTemplate.hasKey(redisName+String.valueOf(id));
    }
}
