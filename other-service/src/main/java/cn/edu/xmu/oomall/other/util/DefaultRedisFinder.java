package cn.edu.xmu.oomall.other.util;

import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/14 下午10:05
 * 4
 */
public class DefaultRedisFinder extends ShareActivityRedisFinder{
    public DefaultRedisFinder(String redisName) {
        super(redisName);
    }

    @Override
    public ShareActivityBo getNext(Long id) {
        return null;
    }

    @Override
    public Long getParentId(Long id) {
        return null;
    }
}
