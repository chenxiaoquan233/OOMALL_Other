package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.other.dao.FavoriteDao;
import cn.edu.xmu.oomall.other.dao.ShareDao;
import cn.edu.xmu.oomall.other.model.bo.BeSharedBo;
import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jx
 * @version 创建时间：2020/12/5 下午2:57
 */
@Service
public class ShareService {
    private static final Logger logger = LoggerFactory.getLogger(ShareService.class);

    @Autowired
    private ShareDao shareDao;

    public BeSharedBo selectValidFirstBeShared(Long customerId, Long skuId){
        /*根据skuId查找当前有效的分享活动*/
        ShareActivityBo shareActivityBo=shareDao.getValidShareActivityBySkuId(skuId);
        /*如果没有，查找店铺默认分享活动*/
        if(shareActivityBo==null)
            shareActivityBo=shareDao.getValidDefaultShareActivityByShopId(2L); //这里需要去调用商品模块查shopId
        /*如果没有，查找平台默认分享活动*/
        if(shareActivityBo==null)
            shareActivityBo=shareDao.getValidDefaultShareActivityByShopId(0L);
        if(shareActivityBo==null)
            return null;
        /*查找点击者在该活动内第一个分享成功记录*/
        BeSharedBo beSharedBo=shareDao.getFirstBeShared(customerId, skuId);
        return beSharedBo;
    }


}
