package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.dto.OrderDTO;
import cn.edu.xmu.oomall.impl.IDubboOrderService;
import cn.edu.xmu.oomall.other.dao.AftersaleDao;
import cn.edu.xmu.oomall.other.model.bo.AftersaleBo;
import cn.edu.xmu.oomall.other.model.po.AftersalePo;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private IDubboOrderService iDubboOrderService;

    public List<AftersaleStateVo> getAfterSaleAllStates() {
        logger.debug("getAfterSaleAllStates");

        return Arrays.stream(AftersaleBo.State.values()).map(AftersaleStateVo::new).collect(Collectors.toList());
    }

    public AftersaleRetVo createAfterSale(AftersaleVo vo, Long orderItemId) {
        logger.debug("createAfterSale");

        AftersaleBo aftersaleBo = vo.createBo();
        aftersaleBo.setOrderItemId(orderItemId);
        aftersaleBo.setRefund(0L);

        //OrderDTO orderDTO = iDubboOrderService.getOrderIdByOrderItemId(orderItemId);

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

    public Object getAftersaleById(Long userId, Long aftersaleId) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(aftersaleId);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getCustomerId().equals(userId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        return new AftersaleBo(aftersalePo).createRetVo();
    }

    public ResponseCode modifyAftersaleById(Long userId, Long aftersaleId, AftersaleModifyVo vo) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(aftersaleId);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getCustomerId().equals(userId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        if(!aftersalePo.getState().equals((byte) 0)) return ResponseCode.AFTERSALE_STATENOTALLOW;

        if(!vo.getConsignee().isBlank()) aftersalePo.setConsignee(vo.getConsignee());
        if(!vo.getDetail().isBlank()) aftersalePo.setDetail(vo.getDetail());
        if(!vo.getMobile().isBlank()) aftersalePo.setMobile(vo.getMobile());
        if(vo.getQuantity() != null) aftersalePo.setQuantity(vo.getQuantity());
        if(!vo.getReason().isBlank()) aftersalePo.setQuantity(vo.getQuantity());
        if(vo.getRegionId() != null) aftersalePo.setRegionId(vo.getRegionId());

        aftersaleDao.updateAftersale(aftersalePo);
        return ResponseCode.OK;
    }

    public ResponseCode adminDeliver(Long aftersaleId, Long shopId, String logSn) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(aftersaleId);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getShopId().equals(shopId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        aftersalePo.setState((byte) 5);

        aftersalePo.setShopLogSn(logSn);
        aftersaleDao.updateAftersale(aftersalePo);
        return ResponseCode.OK;
    }

    public ResponseCode adminReceive(Long aftersaleId, Long shopId, AftersaleReceiveVo vo) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(aftersaleId);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getShopId().equals(shopId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        if(!aftersalePo.getState().equals((byte) 2)) return ResponseCode.AFTERSALE_STATENOTALLOW;

        //TODO API存疑
        //TODO 若退款则调用退款,换货生成新的订单
        return null;
    }

    public ResponseCode adminConfirm(Long aftersaleId, Long shopId, AftersaleConfirmVo vo) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(aftersaleId);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getShopId().equals(shopId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        if(!aftersalePo.getState().equals((byte) 0)) return ResponseCode.AFTERSALE_STATENOTALLOW;

        if(vo.getConfirm()) {
            aftersalePo.setState((byte) 1);
            if(aftersalePo.getType().equals((byte) 2)) aftersalePo.setRefund(vo.getPrice());
        } else {
            aftersalePo.setState((byte) 6);
        }

        aftersaleDao.updateAftersale(aftersalePo);
        return ResponseCode.OK;
    }

    public Object adminGetAftersaleById(Long shopId, Long id) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(id);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getShopId().equals(shopId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;

        return new AftersaleBo(aftersalePo).createRetVo();
    }

    public ResponseCode confirmAftersaleEnd(Long userId, Long id) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(id);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getCustomerId().equals(userId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        if(!aftersalePo.getState().equals((byte) 5)) return ResponseCode.AFTERSALE_STATENOTALLOW;

        aftersalePo.setState((byte) 8);
        aftersaleDao.updateAftersale(aftersalePo);
        return ResponseCode.OK;
    }

    public ResponseCode addWayBillNumber(Long userId, Long id, String logSn) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(id);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getCustomerId().equals(userId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        if(!aftersalePo.getState().equals((byte) 1)) return ResponseCode.AFTERSALE_STATENOTALLOW;

        aftersalePo.setCustomerLogSn(logSn);
        aftersaleDao.updateAftersale(aftersalePo);
        return ResponseCode.OK;
    }

    public ResponseCode deleteAftersaleById(Long userId, Long id) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(id);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getCustomerId().equals(userId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        if(aftersalePo.getState().equals((byte) 0) || aftersalePo.getState().equals((byte) 1)) {
            aftersalePo.setState((byte) 7);
            aftersaleDao.updateAftersale(aftersalePo);

            return ResponseCode.OK;
        }

        if(aftersalePo.getState().equals((byte) 6) || aftersalePo.getState().equals((byte) 7) || aftersalePo.getState().equals((byte) 8)) {
            aftersalePo.setBeDeleted((byte) 1);
            aftersaleDao.updateAftersale(aftersalePo);

            return ResponseCode.OK;
        }

        return ResponseCode.AFTERSALE_STATENOTALLOW;
    }
}
