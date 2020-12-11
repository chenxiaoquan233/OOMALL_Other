package cn.edu.xmu.oomall.impl;

import cn.edu.xmu.oomall.dto.OrderDTO;

/**
 * @author XQChen
 * @version 创建时间：2020/12/11 上午11:11
 */
public interface IOrderService {

    /**
     * 获取orderitem所在订单的orderid
     * @param orderItemId
     * @return
     */
    public OrderDTO getOrderIdByOrderItemId(Long orderItemId);
}
