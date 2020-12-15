package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.FootprintDao;
import cn.edu.xmu.oomall.other.model.bo.BeSharedBo;
import cn.edu.xmu.oomall.other.model.bo.FootPrintBo;
import cn.edu.xmu.oomall.other.model.po.BeSharePo;
import cn.edu.xmu.oomall.other.model.po.FootPrintPo;
import cn.edu.xmu.oomall.other.model.vo.FootPrint.FootPrintVo;
import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
import cn.edu.xmu.goods.client.IGoodsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author  Ji Cao
 * @version  创建时间  2020/12/6 上午 10:52
 */
@Service
public class FootprintService {
    private static final Logger logger = LoggerFactory.getLogger(FootprintService.class);

    @Autowired
    private FootprintDao footprintDao;

    @DubboReference
    IGoodsService goodsService;

    @Resource
    RocketMQTemplate rocketMQTemplate;

    public ResponseCode addFootprint(Long userId, Long id)
    {
        FootPrintVo footPrintVo = new FootPrintVo();
        footPrintVo.setCustomerId(userId);
        footPrintVo.setGoodsSkuId(id);
        String json = JacksonUtil.toJson(footPrintVo);
        Message message = MessageBuilder.withPayload(json).build();
        rocketMQTemplate.sendOneWay("footprint-topic",message);
        logger.debug(json);
        return ResponseCode.OK;
    }

    public ReturnObject<PageInfo<VoObject>> getFootprints(Long did,Long userId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize){
        PageHelper.startPage(page,pageSize,true,true,null);
        PageInfo<FootPrintPo> footPrintPos = footprintDao.getFootprints(userId, beginTime, endTime, page, pageSize);
        List<VoObject> footprints = footPrintPos.getList().stream().map(FootPrintBo::new).map(x->{
                x.setSkuSimpleVo(new GoodsSkuSimpleVo(goodsService.getSku(x.getSkuId())));
                return x;
        }).collect(Collectors.toList());

        PageInfo<VoObject> retObj=new PageInfo<>(footprints);
        retObj.setPageNum(footPrintPos.getPageNum());
        retObj.setPageSize(footPrintPos.getPageSize());
        retObj.setTotal(footPrintPos.getTotal());
        retObj.setPages(footPrintPos.getPages());
        return new ReturnObject<>(retObj);
    }

}
