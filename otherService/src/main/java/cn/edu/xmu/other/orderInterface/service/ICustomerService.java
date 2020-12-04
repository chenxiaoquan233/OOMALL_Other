package cn.edu.xmu.other.orderInterface.service;

import cn.edu.xmu.other.orderInterface.bo.OrderCustomer;

public interface ICustomerService {

    /**
     * 获取customer业务对象
     * @param customerId
     * @return
     */
    OrderCustomer getCustomer(Long customerId);
}
