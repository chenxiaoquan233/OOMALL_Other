package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.oomall.other.mapper.BeSharePoMapper;
import cn.edu.xmu.oomall.other.mapper.ShareActivityPoMapper;
import cn.edu.xmu.oomall.other.model.bo.BeSharedBo;
import cn.edu.xmu.oomall.other.model.bo.ShareActivityBo;
import cn.edu.xmu.oomall.other.model.po.BeSharePo;
import cn.edu.xmu.oomall.other.model.po.BeSharePoExample;
import cn.edu.xmu.oomall.other.model.po.ShareActivityPo;
import cn.edu.xmu.oomall.other.model.po.ShareActivityPoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    public BeSharedBo getFirstBeShared(Long customerId,Long skuId,Long shareActicityId){
        BeSharePoExample example=new BeSharePoExample();
        BeSharePoExample.Criteria criteria=example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);
        criteria.andGoodsSpuIdEqualTo(skuId);
        criteria.andShareActivityIdEqualTo(shareActicityId);
        List<BeSharePo> beSharePos=beSharePoMapper.selectByExample(example);
        return new BeSharedBo(beSharePos.get(0));
    }

    public ShareActivityBo getShareActivityById(Long id){
        return new ShareActivityBo(shareActivityPoMapper.selectByPrimaryKey(id));
    }

    /*查询sku历史所有的分享活动*/
    public List<ShareActivityBo> getAllShareActivityBySkuId(Long skuId){
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria=example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(skuId);
        List<ShareActivityPo> pos=shareActivityPoMapper.selectByExample(example);
        if(pos==null)
            return null;
        return pos.stream().map(ShareActivityBo::new).collect(Collectors.toList());
    }

    /*查询sku当前生效的分享活动*/
    public ShareActivityBo getValidShareActivityBySkuId(Long skuId){
        ShareActivityPoExample example=new ShareActivityPoExample();
        ShareActivityPoExample.Criteria criteria=example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(skuId);
        criteria.andBeDeletedEqualTo((byte)0); //未被逻辑删除
        criteria.andStateEqualTo((byte)1); //状态上架
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
        criteria.andBeDeletedEqualTo((byte)0); //未被逻辑删除
        criteria.andStateEqualTo((byte)1); //状态上架
        criteria.andBeginTimeLessThan(LocalDateTime.now());
        criteria.andEndTimeGreaterThan(LocalDateTime.now());
        List<ShareActivityPo> pos=shareActivityPoMapper.selectByExample(example);
        if(pos==null) return null;
        return new ShareActivityBo(pos.get(0));
    }

    /**/

}
