package cn.edu.xmu.oomall.other.impl;

/**
 * @author XQChen
 * @version 创建时间：2020/12/16 下午8:42
 */
public interface IAftersaleService {
    /**     *
     *  检查用户是否拥有该售后
     *
     * * @param afterSaleId 售后id
     * * @param customerId  用户id
     * * @return
     * */
    Boolean isCustomerOwnAftersale(Long customerId, Long afterSaleId);

    /**
     * 检查商店是否拥有该售后
     * @param shopId        商店id
     * @param afterSaleId   售后id
     * @return
     */
    Boolean isShopOwnAftersale(Long shopId, Long afterSaleId);
    /**
     * 查看售后是否处于可支付状态
     * @param id    售后id
     * @return
     */
    Boolean aftersaleCanBePaid(Long id);
    /**
     * 获取售后的顾客id
     * @param id 售后id
     * @return
     */
    Long getCustomerIdByAftersaleId(Long id);}