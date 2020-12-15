package cn.edu.xmu.oomall.other.util;

import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import cn.xmu.edu.goods.client.dubbo.ShopDTO;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/14 下午5:08
 * 4
 */
public class SkuRedisFinder extends ShareActivityRedisFinder{
    private String parentName;

    public SkuRedisFinder(String redisName,String parentName) {
        super(redisName);
        this.parentName=redisName+"_"+parentName;
    }


    @Override
    public ShareActivityBo getNext(Long id) {
        return next.getBoFromRedis(null,getParentId(id));
    }

    //controller校验
    @Override
    public Long getParentId(Long id) {
        Long parentId=(Long) redisTemplate.opsForHash().get(parentName,String.valueOf(id));
        if(parentId==null){
            parentId=setParentId(id);
        }
        return parentId;
    }
    private Long setParentId(Long id){
        ShopDTO dto=goodsService.getShopBySKUId(id);
        //if(dto==null)return null;
        redisTemplate.opsForHash().put(parentName,String.valueOf(id),dto.getId());
        redisTemplate.expire(parentName,30, TimeUnit.MINUTES);
        return dto.getId();
    }
}
