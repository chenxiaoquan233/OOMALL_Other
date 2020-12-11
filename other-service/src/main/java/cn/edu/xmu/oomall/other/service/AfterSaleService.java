package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.dto.OrderDTO;
import cn.edu.xmu.oomall.impl.IOrderService;
import cn.edu.xmu.oomall.other.dao.AftersaleDao;
import cn.edu.xmu.oomall.other.model.bo.AftersaleBo;
import cn.edu.xmu.oomall.other.model.po.AftersalePo;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.AftersaleRetVo;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.AftersaleStateVo;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.AftersaleVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XQChen
 * @version 创建时间：2020/12/10 上午10:21
 */
@Service
public class AfterSaleService {
    private static final Logger logger = LoggerFactory.getLogger(AfterSaleService.class);

    @Autowired
    private AftersaleDao aftersaleDao;

    @DubboReference
    private IOrderService iOrderService;

    public List<AftersaleStateVo> getAfterSaleAllStates() {
        logger.debug("getAfterSaleAllStates");

        return Arrays.stream(AftersaleBo.State.values()).map(AftersaleStateVo::new).collect(Collectors.toList());
    }

    public AftersaleRetVo createAfterSale(AftersaleVo vo, Long orderItemId) {
        logger.debug("createAfterSale");

        AftersaleBo aftersaleBo = vo.createBo();
        aftersaleBo.setOrderItemId(orderItemId);
        aftersaleBo.setRefund(0L);

        OrderDTO orderDTO = iOrderService.getOrderIdByOrderItemId(orderItemId);

        AftersalePo aftersalePo = aftersaleDao.insertAftersale(aftersaleBo.createPo());





        return null;
    }

    public List<AftersaleRetVo> getAllAftersales(
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
        aftersaleDao.getAllAftersales(userId, spuId, skuId, orderItemId, beginTime, endTime, page, pageSize, type, state);

        return null;
    }
}
