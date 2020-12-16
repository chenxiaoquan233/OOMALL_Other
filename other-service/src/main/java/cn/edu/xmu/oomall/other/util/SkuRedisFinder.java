package cn.edu.xmu.oomall.other.util;

import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import cn.edu.xmu.goods.client.dubbo.ShopDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/14 下午5:08
 * 4
 */
@Slf4j
public class SkuRedisFinder extends ShareActivityRedisFinder{
    private String parentName;

    public SkuRedisFinder(String redisName,String parentName) {
        super(redisName);
        this.parentName=redisName+"_"+parentName;
    }


    @Override
    public void chainDelete(Long skuId, Long shopId) {
        if(skuId!=null&&skuId!=0)deleteRedis(skuId);
        else next.chainDelete(skuId,shopId);
    }

    @Override
    public ShareActivityBo getNext(Long id) {
        log.debug("sku getting next:"+id);
        return next.getBoFromRedis(0L,id);
    }

    //controller校验
    @Override
    public Long getParentId(Long id) {
        log.debug("is getting parent id:"+id);
        Long parentId=null;
        if(redisTemplate.opsForHash().hasKey(parentName,String.valueOf(id))){
            parentId=Long.parseLong(redisTemplate.opsForHash().get(parentName,String.valueOf(id)).toString());
        }
        //Object obj=redisTemplate.opsForHash().get(parentName,String.valueOf(id));
        //log.debug("get object:"+ JacksonUtil.toJson(obj));
        //log.debug(String.valueOf((Long)obj));

        //log.debug("got parent id:"+id+"parentId:"+parentId.toString());
        if(parentId==null){
            parentId=setParentId(id);
        }
        return parentId;
    }
    private Long setParentId(Long id){
        ShopDTO dto=goodsService.getShopBySKUId(id);
        log.debug("is setting parent id:[id:"+id+",parentId:"+dto.getId()+"]");
        redisTemplate.opsForHash().put(parentName,String.valueOf(id),dto.getId());
        redisTemplate.expire(parentName,30, TimeUnit.MINUTES);
        return dto.getId();
    }
}
