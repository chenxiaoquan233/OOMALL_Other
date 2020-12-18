package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.FootprintDao;
import cn.edu.xmu.oomall.other.model.bo.BeSharedBo;
import cn.edu.xmu.oomall.other.model.bo.FootPrintBo;
import cn.edu.xmu.oomall.other.model.po.BeSharePo;
import cn.edu.xmu.oomall.other.model.po.FavouriteGoodsPo;
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

    @DubboReference(version = "0.0.1-SNAPSHOT", check = false)
    IGoodsService iGoodsService;

    public ReturnObject<PageInfo<VoObject>> getFootprints(Long userId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize){
        List<FootPrintPo> footPrintPos = footprintDao.getFootprints(userId, beginTime, endTime, page, pageSize);
        List<VoObject> footprints = footPrintPos.stream().map(FootPrintBo::new).map(x->{
                //x.setSkuSimpleVo(new GoodsSkuSimpleVo(goodsService.getSku(x.getSkuId())));
            System.out.println("1");
            System.out.println(x.getSkuSimpleVo());
            x.setSkuSimpleVo(new GoodsSkuSimpleVo(iGoodsService.getSku(x.getSkuId())));
            System.out.println("2");
            System.out.println(x.getSkuSimpleVo());
            return x;
        }).collect(Collectors.toList());
        PageInfo<FootPrintPo> footPoPage = PageInfo.of(footPrintPos);
        PageInfo<VoObject> retObj = new PageInfo<>(footprints);
        retObj.setPageNum(footPoPage.getPageNum());
        retObj.setPageSize(footPoPage.getPageSize());
        retObj.setTotal(footPoPage.getTotal());
        retObj.setPages(footPoPage.getPages());
        return new ReturnObject<>(retObj);
    }

}
