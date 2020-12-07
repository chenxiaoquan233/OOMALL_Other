package cn.edu.xmu.oomall.other.service.OrderInterface;

import cn.edu.xmu.oomall.other.dao.ShareDao;
import cn.edu.xmu.oomall.other.dto.OrderItem;
import cn.edu.xmu.oomall.other.service.ICustomerService;
import cn.edu.xmu.oomall.other.service.IShareService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jx
 * @version 创建时间：2020/12/5 下午3:10
 */
public class ShareService implements IShareService {

    @Autowired
    private ShareDao shareDao;
    @Override
    public void sendShareMessage(OrderItem orderItem) {

    }

    @Override
    public Long getBeSharedId(Long customerId, Long skuId) {
        return null;
    }
}
