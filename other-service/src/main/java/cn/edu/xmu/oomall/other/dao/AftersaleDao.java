package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.oomall.other.mapper.AftersalePoMapper;
import cn.edu.xmu.oomall.other.model.po.AftersalePo;
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
        aftersalePoMapper.insertSelective(po);
        return po;
    }

    public List<AftersalePo> getAllAftersales(
            Long userId,
            Long spuId,
            Long skuId,
            Long orderItemId,
            LocalDateTime beginTime,
            LocalDateTime endTime,
            Integer page,
            Integer pageSize,
            Integer type,
            Integer state) {
        return null;
    }
}
