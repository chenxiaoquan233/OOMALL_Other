package cn.edu.xmu.oomall.other.util;

import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/14 下午10:04
 * 4
 */
public class ShopRedisFinder extends ShareActivityRedisFinder{
    public ShopRedisFinder(String redisName) {
        super(redisName);
    }

    @Override
    public void chainDelete(Long skuId, Long shopId) {
        if(shopId!=0){
            deleteRedis(shopId);
        }
        else{
            chainDelete(skuId,shopId);
        }
    }

    @Override
    public ShareActivityBo getNext(Long id) {
        return next.getBoFromRedis(null,0L);
    }

    @Override
    public Long getParentId(Long id) {
        return 0L;
    }
}
