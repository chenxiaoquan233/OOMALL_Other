package cn.edu.xmu.oomall.other.controller.Share.ServiceStub;

import cn.edu.xmu.oomall.dto.AftersaleDto;
import cn.edu.xmu.oomall.dto.EffectiveShareDto;
import cn.edu.xmu.oomall.dto.ExchangeOrderDto;
import cn.edu.xmu.oomall.dto.OrderItemDto;
import cn.edu.xmu.oomall.service.IDubboOrderService;

import java.util.List;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/14 下午7:53
 * 4
 */
public class OrderService implements IDubboOrderService {
    @Override
    public AftersaleDto getAfterSaleByOrderItemId(Long orderItemId) {
        return null;
    }

    @Override
    public Boolean isShopOwnOrder(Long shopId, Long orderId) {
        return null;
    }

    @Override
    public Boolean isCustomerOwnOrder(Long customerId, Long orderId) {
        return null;
    }

    @Override
    public Boolean isCustomerOwnOrderItem(Long customerId, Long orderItemId) {
        return null;
    }

    @Override
    public OrderItemDto getOrderItem(Long orderItemId) {
        return null;
    }

    @Override
    public Boolean orderCanBePaid(Long id) {
        return null;
    }

    @Override
    public void checkOrderPaid(Long id, Long amount) {

    }

    @Override
    public Long getOrderPresaleDeposit(Long id) {
        return null;
    }

    @Override
    public List<EffectiveShareDto> getEffectiveShareRecord() {
        return null;
    }

    @Override
    public Integer createExchangeOrder(ExchangeOrderDto dto) {
        return null;
    }
}
