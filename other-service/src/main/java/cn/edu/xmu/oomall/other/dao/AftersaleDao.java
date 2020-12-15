package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.oomall.other.mapper.AftersalePoMapper;
import cn.edu.xmu.oomall.other.model.po.AftersalePo;
import cn.edu.xmu.oomall.other.model.po.AftersalePoExample;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.AftersaleModifyVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author XQChen
 * @version 创建时间：2020/12/11 下午7:36
 */
@Repository
public class AftersaleDao {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteDao.class);

    @Autowired
    private AftersalePoMapper aftersalePoMapper;

    public AftersalePo insertAftersale(AftersalePo po) {
        logger.debug("PO here1:" + po);
        aftersalePoMapper.insertSelective(po);
        logger.debug("PO here2:" + po);
        return po;
    }

    public PageInfo<AftersalePo> getAllAftersales(
            Long userId,
            Long shopId,
            LocalDateTime beginTime,
            LocalDateTime endTime,
            Integer page,
            Integer pageSize,
            Integer type,
            Integer state) {
        AftersalePoExample example = new AftersalePoExample();
        AftersalePoExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        if(shopId != null) criteria.andShopIdEqualTo(shopId);
        if(beginTime != null) criteria.andGmtCreateGreaterThanOrEqualTo(beginTime);
        if(endTime != null) criteria.andGmtCreateLessThanOrEqualTo(endTime);
        if(type != null) criteria.andTypeEqualTo(type.byteValue());
        if(state != null) criteria.andStateEqualTo(state.byteValue());

        PageHelper.startPage(page, pageSize);
        List<AftersalePo> aftersalePos = aftersalePoMapper.selectByExample(example);

        return new PageInfo<>(aftersalePos);
    }

    public AftersalePo getAftersaleById(Long id) {
        return aftersalePoMapper.selectByPrimaryKey(id);
    }

    public Integer updateAftersale(AftersalePo po) {
        return aftersalePoMapper.updateByPrimaryKeySelective(po);
    }
}
