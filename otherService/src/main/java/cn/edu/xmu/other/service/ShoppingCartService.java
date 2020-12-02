package cn.edu.xmu.other.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.ShoppingCartDao;
import cn.edu.xmu.other.dao.UserDao;
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
}
