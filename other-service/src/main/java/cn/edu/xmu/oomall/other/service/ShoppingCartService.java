package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.ShoppingCartDao;
import cn.edu.xmu.oomall.other.dao.UserDao;
import cn.edu.xmu.oomall.other.model.bo.ShoppingCartBo;
import cn.edu.xmu.oomall.other.model.po.ShoppingCartPo;
import cn.edu.xmu.oomall.other.model.vo.ShoppingCart.ShoppingCartRetVo;
import cn.edu.xmu.goods.client.IGoodsService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jx
 * @version 创建时间：2020/12/2 上午8:49
 */
@Service
public class ShoppingCartService {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @DubboReference(version = "0.0.1-SNAPSHOT", check = false)
    IGoodsService iGoodsService;

//    public Long getPrice(Long skuId){
//        return skuId*10;
//    }
//
    public List<Object> getCouponActicity(Long goodsSkuId) {
        return null;
    }


    public ResponseCode clearCart(Long userId){
        return shoppingCartDao.clearCart(userId);
    }

    public ResponseCode deleteCart(Long userId,Long cartId){
        return shoppingCartDao.deleteCart(userId,cartId);
    }

    public ReturnObject<PageInfo<VoObject>> getCarts(Long userId, Integer page, Integer pageSize){
        List<ShoppingCartPo> cartPos=shoppingCartDao.getCartByUserId(userId,page,pageSize);
        List<VoObject> ret = cartPos.stream().map(ShoppingCartBo::new)
                .map(x->{x.setCouponActivity(getCouponActicity(x.getGoodsSkuId()));return x;})
                .collect(Collectors.toList());
        PageInfo<ShoppingCartPo> cartPoPage = PageInfo.of(cartPos);
        PageInfo<VoObject> cartPage = new PageInfo<>(ret);
        cartPage.setPages(cartPoPage.getPages());
        cartPage.setPageNum(cartPoPage.getPageNum());
        cartPage.setPageSize(cartPoPage.getPageSize());
        cartPage.setTotal(cartPoPage.getTotal());
        return new ReturnObject<>(cartPage);
    }

    public Object addCart(Long userId, Long goodsSkuId, Integer quantity){
        Long price=iGoodsService.getPrice(goodsSkuId);
        if(price<=0)
            return null;
        ShoppingCartPo po=shoppingCartDao.addCart(userId,goodsSkuId,quantity,price);
        if(po==null)
            return null;
        ShoppingCartBo bo=new ShoppingCartBo(po);
        bo.setCouponActivity(getCouponActicity(goodsSkuId));
        return new ReturnObject<>(bo);
    }

    public ResponseCode modifyCart(Long userId, Long cartId,Long goodsSkuId, Integer quantity) {
        Long price=iGoodsService.getPrice(goodsSkuId);
        if(price<=0)
            return null;
        return shoppingCartDao.modifyCart(cartId,userId,goodsSkuId,quantity,price);
    }
}
