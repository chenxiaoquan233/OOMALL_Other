package dubbotest.controller;

import cn.edu.xmu.oomall.impl.ITimeSegmentService;
import cn.edu.xmu.oomall.other.dto.CustomerDTO;
import cn.edu.xmu.oomall.other.impl.ICustomerService;
import cn.edu.xmu.oomall.other.impl.IRebateService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XQChen
 * @version 创建时间：2020/12/9 上午8:58
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class testController {

    @DubboReference
    private ICustomerService customerService;

    @DubboReference
    private IRebateService rebateService;

    @DubboReference
    private ITimeSegmentService timeSegmentService;

    @GetMapping("customer")
    public Object customer() {
        CustomerDTO customer = customerService.getCustomer(20000L);
        return customer;
    }

    @GetMapping("rebate")
    public Object rebate() {
        rebateService.useRebate(20000L, 20);
        CustomerDTO customerDTO = customerService.getCustomer(20000L);
        return customerDTO;
    }

    @GetMapping("flash")
    public Object flash() {
        return timeSegmentService.getFlashSaleSegmentById(1L);
    }
}
