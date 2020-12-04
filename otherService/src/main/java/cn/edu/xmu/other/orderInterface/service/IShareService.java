package cn.edu.xmu.other.orderInterface.service;

public interface IShareService {
    /**
     * 发送分享成功的消息
     * 其他模块提供接口还是发消息队列，待决定
     * @param orderItem
     */
    //void sendShareMessage(OrderItem orderItem);

    /**
     * 获取点击记录
     * @param customerId
     * @param skuId
     * @return
     */
    Long getBeSharedId(Long customerId, Long skuId);
}
