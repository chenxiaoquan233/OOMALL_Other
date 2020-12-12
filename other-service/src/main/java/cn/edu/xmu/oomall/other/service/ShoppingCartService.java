package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.ShoppingCartDao;
import cn.edu.xmu.oomall.other.dao.UserDao;
import cn.edu.xmu.oomall.other.model.bo.ShoppingCartBo;
import cn.edu.xmu.oomall.other.model.po.ShoppingCartPo;
import cn.edu.xmu.oomall.other.model.vo.ShoppingCart.ShoppingCartRetVo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jx
 * @version 创建时间：2020/12/2 上午8:49
 */
@Service
public class ShoppingCartService {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    public ResponseCode clearCart(Long userId){
        return shoppingCartDao.clearCart(userId);
    }

    public ResponseCode deleteCart(Long userId,Long cartId){
        return shoppingCartDao.deleteCart(userId,cartId);
    }

    public ReturnObject<PageInfo<VoObject>> getCarts(Long userId, Integer page, Integer pageSize){
        return shoppingCartDao.getCartByUserId(userId,page,pageSize);
    }

    public Object addCart(Long userId, Long goodsSkuId, Integer quantity){
        ShoppingCartPo po=shoppingCartDao.addCart(userId,goodsSkuId,quantity);
        if(po==null)
            return null;
        ShoppingCartBo bo=new ShoppingCartBo(po);
        bo.setCouponActivity(shoppingCartDao.getCouponActicity(goodsSkuId));
        return new ReturnObject<>(bo);
    }

    public ResponseCode modifyCart(Long userId, Long cartId,Long goodsSkuId, Integer quantity) {
        return shoppingCartDao.modifyCart(cartId,userId,goodsSkuId,quantity);
    }
}
