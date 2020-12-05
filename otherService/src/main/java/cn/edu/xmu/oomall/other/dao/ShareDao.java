package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.oomall.other.mapper.BeSharePoMapper;
import cn.edu.xmu.oomall.other.model.bo.BeSharedBo;
import cn.edu.xmu.oomall.other.model.po.BeSharePo;
import cn.edu.xmu.oomall.other.model.po.BeSharePoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    public List<BeSharedBo> getBeSharedByCustomerIdAndSkuId(Long customerId,Long skuId){
        BeSharePoExample example=new BeSharePoExample();
        BeSharePoExample.Criteria criteria=example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);
        criteria.andGoodsSpuIdEqualTo(skuId);
        List<BeSharePo> beSharePos=beSharePoMapper.selectByExample(example);
        List<BeSharedBo> beSharedBos=beSharePos.stream().map(BeSharedBo::new).collect(Collectors.toList());
        return beSharedBos;
    }

}
