package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.other.model.bo.AftersaleBo;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.AftersaleRetVo;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.AftersaleStateVo;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.AftersaleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public List<AftersaleStateVo> getAfterSaleAllStates() {
        logger.debug("getAfterSaleAllStates");

        return Arrays.stream(AftersaleBo.State.values()).map(AftersaleStateVo::new).collect(Collectors.toList());
    }

    public AftersaleRetVo createAfterSale(AftersaleVo vo, Long orderItemId) {
        logger.debug("createAfterSale");

        AftersaleBo aftersaleBo = vo.createBo();
        aftersaleBo.setOrderItemId(orderItemId);



        return null;
    }
}
