package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.FootPrintPoMapper;
import cn.edu.xmu.oomall.other.model.bo.FootPrintBo;
import cn.edu.xmu.oomall.other.model.po.FootPrintPo;
import cn.edu.xmu.oomall.other.model.po.FootPrintPoExample;
import cn.edu.xmu.oomall.other.model.vo.FootPrint.FootPrintVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/6 10:42
 */
@Repository
public class FootprintDao {
    private static final Logger logger = LoggerFactory.getLogger(FootprintDao.class);

    @Autowired
    private FootPrintPoMapper footPrintPoMapper;

    /**
     * 增加足迹
     * @param footPrintVo 足迹Vo
     * @return ResponseCode
     */
    public ResponseCode addFootprint(FootPrintVo footPrintVo)
    {
        FootPrintPo footPrintPo = new FootPrintPo();
        footPrintPo.setGoodsSkuId(footPrintVo.getGoodsSkuId());
        footPrintPo.setCustomerId(footPrintVo.getCustomerId());
        footPrintPo.setGmtCreate(LocalDateTime.now());
        footPrintPo.setGmtModified(null);
        try{
            int ret = footPrintPoMapper.insertSelective(footPrintPo);
            if(ret == 0){
                logger.debug("insertFootprint : insert footprint fail " + footPrintPo.getCustomerId() +" " + footPrintPo.getGoodsSkuId());
                return ResponseCode.INTERNAL_SERVER_ERR;
            }else{
                logger.debug("insertFootprint: insert footprint" + footPrintPo.getCustomerId() +" " + footPrintPo.getGoodsSkuId());
                footPrintPo.setId(footPrintPo.getId());
                return ResponseCode.OK;
            }
        }catch (DataAccessException e)
        {
            logger.debug("insertFootprint : insert footprint fail " + footPrintPo.getCustomerId() +" " + footPrintPo.getGoodsSkuId());
            return ResponseCode.INTERNAL_SERVER_ERR;
        }

    }
    /**
     * 分页查询所有足迹
     * @param userId  用户ID
     * @param beginTime  开始时间
     * @param endTime  结束时间
     * @param page 页数
     * @param pageSize 每页大小
     * @return ReturnObject<List> 角色列表
     */
    public List<FootPrintPo> getFootprints(Long userId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize){
        System.out.println("entry dao");
        PageHelper.startPage(page,pageSize,true,true,null);
        FootPrintPoExample example=new FootPrintPoExample();
        FootPrintPoExample.Criteria criteria=example.createCriteria();
        if(userId!=null)
            criteria.andCustomerIdEqualTo(userId);
        if(beginTime!=null)
            criteria.andGmtCreateGreaterThan(beginTime);
        if(endTime!=null)
            criteria.andGmtCreateLessThan(endTime);
        List<FootPrintPo> footPrints = footPrintPoMapper.selectByExample(example);
        System.out.println("exit dao");
        return footPrints;
    }

    
}
