package cn.edu.xmu.oomall.other.service.OrderInterface;

import cn.edu.xmu.oomall.other.dao.ShareDao;
import cn.edu.xmu.oomall.other.dto.OrderItemDTO;
import cn.edu.xmu.oomall.other.impl.IShareService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jx
 * @version 创建时间：2020/12/5 下午3:10
 */
@DubboService(registry = {"provider2"})
public class IShareServiceImpl implements IShareService {

    @Autowired
    private ShareDao shareDao;

    @Override
    public void sendShareMessage(OrderItemDTO orderItem) {
        return;
    }

    @Override
    public Long getBeSharedId(Long customerId, Long skuId) {
        return shareDao.getFirstBeShared(customerId, skuId).getId();
    }
}
