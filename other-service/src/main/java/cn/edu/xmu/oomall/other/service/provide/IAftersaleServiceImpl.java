package cn.edu.xmu.oomall.other.service.provide;

import cn.edu.xmu.oomall.other.controller.AftersaleController;
import cn.edu.xmu.oomall.other.dao.AftersaleDao;
import cn.edu.xmu.oomall.other.impl.IAftersaleService;
import cn.edu.xmu.oomall.other.model.bo.AftersaleBo;
import cn.edu.xmu.oomall.other.model.po.AftersalePo;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author XQChen
 * @version 创建时间：2020/12/16 下午8:44
 */
@DubboService(version = "0.0.1-SNAPSHOT")
public class IAftersaleServiceImpl implements IAftersaleService{
    private static final Logger logger = LoggerFactory.getLogger(AftersaleController.class);
    @Autowired
    private AftersaleDao aftersaleDao;

    @Override
    public Boolean isCustomerOwnAftersale(Long customerId, Long afterSaleId) {
        logger.debug("customerId:" + customerId + "aftersaleId: " + afterSaleId);
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(afterSaleId);
        if(aftersalePo == null) return null;
        return aftersalePo.getCustomerId().equals(customerId);
    }

    @Override
    public Boolean isShopOwnAftersale(Long shopId, Long afterSaleId) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(afterSaleId);
        if(aftersalePo == null) return null;
        return aftersalePo.getShopId().equals(shopId);
    }

    @Override
    public Boolean aftersaleCanBePaid(Long id) {
        AftersalePo aftersalePo = aftersaleDao.getAftersaleById(id);
        return aftersalePo.getType().equals(AftersaleBo.Type.MAINTAIN.getCode().byteValue()) && aftersalePo.getState().equals(AftersaleBo.State.WAIT_SHOP_DELIVER.getCode().byteValue());
    }

    @Override
    public Long getCustomerIdByAftersaleId(Long id) {
        return aftersaleDao.getAftersaleById(id).getCustomerId();
    }
}
