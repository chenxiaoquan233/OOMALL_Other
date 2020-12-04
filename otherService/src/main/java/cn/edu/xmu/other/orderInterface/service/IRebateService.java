package cn.edu.xmu.other.orderInterface.service;

public interface IRebateService {
    /**
     * 使用返点
     * @param customerId
     * @param num 预期使用量
     * @return 实际使用量
     */
    Integer useRebate(Long customerId, Integer num);
}
