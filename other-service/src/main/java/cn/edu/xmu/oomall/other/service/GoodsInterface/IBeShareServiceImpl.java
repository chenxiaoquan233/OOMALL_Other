package cn.edu.xmu.oomall.other.service.GoodsInterface;

import cn.edu.xmu.oomall.impl.IBeShareService;
import cn.edu.xmu.oomall.other.dao.ShareDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/12 下午8:28
 * 4
 */
public class IBeShareServiceImpl implements IBeShareService {
    @Autowired
    ShareDao shareDao;
    @Override
    public boolean createBeShare(Long customerId, Long shareId, Long skuId) {
        return false;
    }
}
