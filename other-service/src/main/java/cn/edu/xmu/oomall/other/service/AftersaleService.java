package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.oomall.dto.AftersaleDto;
import cn.edu.xmu.oomall.dto.ExchangeOrderDto;
import cn.edu.xmu.oomall.service.IDubboOrderService;
import cn.edu.xmu.oomall.service.IDubboPaymentService;
import cn.edu.xmu.oomall.other.dao.AddressDao;
import cn.edu.xmu.oomall.other.dao.AftersaleDao;
import cn.edu.xmu.oomall.other.model.bo.AftersaleBo;
import cn.edu.xmu.oomall.other.model.po.AftersalePo;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.*;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author XQChen
 * @version 创建时间：2020/12/10 上午10:21
 */
@Service
public class AftersaleService {
    private static final Logger logger = LoggerFactory.getLogger(AftersaleService.class);

    @Autowired
    private AftersaleDao aftersaleDao;

    @Autowired
    private AddressDao addressDao;

    @DubboReference(version = "0.0.1-SNAPSHOT", check = false)
    private IDubboOrderService iDubboOrderService;

    @DubboReference(version = "0.0.1-SNAPSHOT", check = false)
    private IDubboPaymentService iDubboPaymentService;

    public List<AftersaleStateVo> getAftersaleAllStates() {
        logger.debug("getAfterSaleAllStates");

        return Arrays.stream(AftersaleBo.State.values()).map(AftersaleStateVo::new).collect(Collectors.toList());
    }

    public Object createAftersale(AftersaleVo vo, Long orderItemId, Long userId) {
        if(!addressDao.hasRegion(vo.getRegionId())) return ResponseCode.RESOURCE_ID_NOTEXIST;

        AftersaleBo aftersaleBo = vo.createBo();
        aftersaleBo.setCustomerId(userId);
        aftersaleBo.setOrderItemId(orderItemId);
        aftersaleBo.setServiceSn(UUID.randomUUID().toString());
        aftersaleBo.setState(AftersaleBo.State.WAIT_ADMIN_AUDIT);
        aftersaleBo.setBeDeleted((byte) 0);

        AftersaleDto aftersaleDTO = null;

        //TODO dubbo
        //if(orderItemId.equals(1L))
        //    aftersaleDTO = new AftersaleDto(1L, "tset", 2L, "ttt", 10L, 20);
        aftersaleDTO = iDubboOrderService.getAfterSaleByOrderItemId(orderItemId);

        if(aftersaleDTO == null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;

        aftersaleBo.setOrderId(aftersaleDTO.getOrderId());
        aftersaleBo.setOrderSn(aftersaleDTO.getOrderSn());
        aftersaleBo.setSkuId(aftersaleDTO.getSkuId());
        aftersaleBo.setSkuName(aftersaleDTO.getSkuName());
        aftersaleBo.setShopId(aftersaleDTO.getShopId());

        AftersalePo aftersalePo = aftersaleDao.insertAftersale(aftersaleBo.createPo());

        aftersaleBo.setId(aftersalePo.getId());

        return aftersaleBo.createSkuRetVo();
    }

    public PageInfo<AftersaleRetVo> getAllAftersales(
            Long userId,
            Long shopId,
            LocalDateTime beginTime,
            LocalDateTime endTime,
            Integer page,
            Integer pageSize,
            Integer type,
            Integer state) {

        PageInfo<AftersalePo> aftersalePos = aftersaleDao.getAllAftersales(userId, shopId, beginTime, endTime, page, pageSize, type, state);
        PageInfo<AftersaleRetVo> aftersaleVos = new PageInfo<>(aftersalePos.getList().stream().map(AftersaleBo::new).peek(x -> x.setDTO(iDubboOrderService.getAfterSaleByOrderItemId(x.getOrderItemId()))).map(AftersaleBo::createRetVo).collect(Collectors.toList()));

        aftersaleVos.setTotal(aftersalePos.getTotal());
        aftersaleVos.setPageSize(aftersalePos.getPageSize());
        aftersaleVos.setPageNum(aftersalePos.getPageNum());
        aftersaleVos.setPages(aftersalePos.getPages());

        return aftersaleVos;
    }

    public Object getAftersaleById(Long userId, Long aftersaleId) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(aftersaleId);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getCustomerId().equals(userId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;

        AftersaleBo bo = new AftersaleBo(aftersalePo);
        AftersaleDto dto = iDubboOrderService.getAfterSaleByOrderItemId(aftersalePo.getOrderItemId());
        bo.setDTO(dto);
        logger.debug("bo:" + bo);
        AftersaleSkuRetVo vo = bo.createSkuRetVo();
        return vo;
    }

    public ResponseCode modifyAftersaleById(Long userId, Long aftersaleId, AftersaleModifyVo vo) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(aftersaleId);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getCustomerId().equals(userId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        if(!aftersalePo.getState().equals((byte) 0)) return ResponseCode.AFTERSALE_STATENOTALLOW;

        if(!(vo.getConsignee() == null) && !vo.getConsignee().isBlank()) aftersalePo.setConsignee(vo.getConsignee());
        if(!(vo.getDetail() == null) && !vo.getDetail().isBlank()) aftersalePo.setDetail(vo.getDetail());
        if(!(vo.getMobile() == null) && !vo.getMobile().isBlank()) aftersalePo.setMobile(vo.getMobile());
        if(vo.getQuantity() != null) aftersalePo.setQuantity(vo.getQuantity());
        if(!(vo.getReason() == null) && !vo.getReason().isBlank()) aftersalePo.setQuantity(vo.getQuantity());
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

    public void test() {
        logger.debug("before dubbo");
        AftersaleDto aftersaleDto = iDubboOrderService.getAfterSaleByOrderItemId(1L);
        logger.debug(aftersaleDto.toString());
        logger.debug("after dubbo");
    }

    public ResponseCode adminReceive(Long aftersaleId, Long shopId, AftersaleReceiveVo vo) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(aftersaleId);
        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getShopId().equals(shopId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        if(!aftersalePo.getState().equals((byte) 2)) return ResponseCode.AFTERSALE_STATENOTALLOW;
        logger.info("asocnasiucbasi");
        logger.info("po:" + vo.getConfirm().equals(true));

        aftersalePo.setConclusion(vo.getConclusion());
        if(vo.getConfirm().equals(true)) {
            if(aftersalePo.getType().equals(AftersaleBo.Type.RETURN.getCode().byteValue())) {
                logger.info("sjikbiausbcoasivuyabi");
                logger.info(aftersaleId.toString());
                logger.info(aftersalePo.getOrderItemId().toString());
                logger.info(aftersalePo.getQuantity().toString());
                //TODO dubbo
                iDubboPaymentService.createRefund(aftersaleId, aftersalePo.getOrderItemId(), aftersalePo.getQuantity());
                aftersalePo.setState((byte) 3);
                logger.debug("po here:" + aftersalePo);
            } else if(aftersalePo.getType().equals(AftersaleBo.Type.EXCHANGE.getCode().byteValue())) {
                //TODO dubbo
                iDubboOrderService.createExchangeOrder(
                        new ExchangeOrderDto(
                                aftersalePo.getCustomerId(),
                                aftersalePo.getShopId(),
                                aftersalePo.getQuantity(),
                                aftersalePo.getOrderItemId(),
                                aftersalePo.getMobile(),
                                aftersalePo.getConsignee(),
                                aftersalePo.getRegionId(),
                                aftersalePo.getDetail()));
                aftersalePo.setState((byte) 4);
            }
        }
        else {
            aftersalePo.setState((byte) 1);
        }

        aftersaleDao.updateAftersale(aftersalePo);

        return ResponseCode.OK;
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

        AftersaleBo bo = new AftersaleBo(aftersalePo);
        AftersaleDto dto = iDubboOrderService.getAfterSaleByOrderItemId(aftersalePo.getOrderItemId());
        bo.setDTO(dto);
        logger.debug("bo:" + bo);
        AftersaleSkuRetVo vo = bo.createSkuRetVo();
        logger.debug("sduoivbdivbid" + vo);
        return vo;
    }

    public ResponseCode confirmAftersaleEnd(Long userId, Long id) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(id);

        if(aftersalePo == null) return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!aftersalePo.getCustomerId().equals(userId)) return ResponseCode.RESOURCE_ID_OUTSCOPE;
        if(!aftersalePo.getState().equals((byte) 5) && !aftersalePo.getState().equals((byte) 3)) return ResponseCode.AFTERSALE_STATENOTALLOW;

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
        if(aftersalePo.getBeDeleted().equals((byte) 1)) return ResponseCode.RESOURCE_ID_NOTEXIST;
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
