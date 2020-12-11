package cn.edu.xmu.oomall.impl;

/**
 * @author XQChen
 * @version 创建时间：2020/12/11 上午11:28
 */
public interface IRefundService {

    /**
     * 进行退款并生成退款单
     * @param userId
     * @param afterSaleId
     * @param shopId
     * @param orderItemId
     * @param quantity
     */
    void createRefund(Long userId, Long afterSaleId, Long shopId, Long orderItemId, Integer quantity);
}
