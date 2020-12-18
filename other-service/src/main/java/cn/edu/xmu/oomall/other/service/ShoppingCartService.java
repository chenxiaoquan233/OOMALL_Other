package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.goods.client.IActivityService;
import cn.edu.xmu.goods.client.dubbo.CouponActivityDTO;
import cn.edu.xmu.goods.client.dubbo.PriceDTO;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
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
import java.util.Map;
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

    @DubboReference(version = "0.0.1-SNAPSHOT", check = false)
    IActivityService iActivityService;




    public ResponseCode clearCart(Long userId){
        return shoppingCartDao.clearCart(userId);
    }

    public ResponseCode deleteCart(Long userId,Long cartId){
        Long judge=shoppingCartDao.judge(userId,cartId);
        if(judge.equals(-1L))
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(judge.equals(-2L))
            return ResponseCode.RESOURCE_ID_OUTSCOPE;
        return shoppingCartDao.deleteCart(userId,cartId);
    }

    public ReturnObject<PageInfo<VoObject>> getCarts(Long userId, Integer page, Integer pageSize){
        List<ShoppingCartPo> cartPos=shoppingCartDao.getCartByUserId(userId,page,pageSize);
        List<Long> skuIds=cartPos.stream().map(ShoppingCartPo::getGoodsSkuId).collect(Collectors.toList());
        Map<Long, List<CouponActivityDTO>> coupon=iActivityService.getSkuCouponActivity(skuIds);
        List<VoObject> ret = cartPos.stream().map(ShoppingCartBo::new)
                .map(x-> {
                    PriceDTO priceDTO=iGoodsService.getPrice(x.getGoodsSkuId());
                    if(priceDTO==null) return x;
                    x.setSkuName(priceDTO.getName());
                    //x.setPrice(priceDTO.getPrePrice());
                    x.setCouponActivity(coupon.get(x.getGoodsSkuId()).stream().collect(Collectors.toList()));
                    return x;})
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
        PriceDTO priceDTO=iGoodsService.getPrice(goodsSkuId);
        if(priceDTO==null||priceDTO.getPrePrice()<=0)
            return null;
        ShoppingCartPo po=shoppingCartDao.addCart(userId,goodsSkuId,quantity,priceDTO.getPrePrice());
        if(po==null)
            return null;
        ShoppingCartBo bo=new ShoppingCartBo(po);
        bo.setSkuName(priceDTO.getName());
        bo.setCouponActivity(iActivityService.getSkuCouponActivity(goodsSkuId).stream().collect(Collectors.toList()));
        return bo.createVo();
    }

    public ResponseCode modifyCart(Long userId, Long cartId,Long goodsSkuId, Integer quantity) {
        System.out.println("1");
        Long judge=shoppingCartDao.judge(userId,cartId);
        if(judge.equals(-1L))
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(judge.equals(-2L))
            return ResponseCode.RESOURCE_ID_OUTSCOPE;
        System.out.println("2");

        SkuDTO oldSku=iGoodsService.getSku(judge);
        SkuDTO newSku=iGoodsService.getSku(goodsSkuId);
        if(newSku==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        if(!oldSku.getGoodsSpuId().equals(newSku.getGoodsSpuId()))
            return ResponseCode.FIELD_NOTVALID;
        System.out.println("3");
        System.out.println("4");
        return shoppingCartDao.modifyCart(cartId,userId,goodsSkuId,quantity,newSku.getPrice());
    }
}
