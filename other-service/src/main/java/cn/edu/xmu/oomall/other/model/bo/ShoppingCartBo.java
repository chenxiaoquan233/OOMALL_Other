package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.ShoppingCartPo;
import cn.edu.xmu.oomall.other.model.vo.ShoppingCart.ShoppingCartRetVo;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author Jx
 * @version 创建时间：2020/12/2 上午8:23
 */

@Data
public class ShoppingCartBo implements VoObject {
    private Long id;
    private Long customerId;
    private Long goodsSkuId;
    private Integer quantity;
    private Long price;
    private List<Object> couponActivity;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public ShoppingCartBo(ShoppingCartPo shoppingCartPo){
        this.id=shoppingCartPo.getId();
        this.gmtCreate=shoppingCartPo.getGmtCreate();
        this.gmtModified=shoppingCartPo.getGmtModified();
        this.customerId=shoppingCartPo.getCustomerId();
        this.goodsSkuId=shoppingCartPo.getGoodsSkuId();
        this.quantity=shoppingCartPo.getQuantity();
        this.price=shoppingCartPo.getPrice();
    }

    @Override
    public Object createVo() {
        ShoppingCartRetVo shoppingCartRetVo=new ShoppingCartRetVo();
        shoppingCartRetVo.setId(this.id);
        shoppingCartRetVo.setGoodsSkuId(this.goodsSkuId);
        shoppingCartRetVo.setQuantity(this.quantity);
        shoppingCartRetVo.setPrice(this.price);
        shoppingCartRetVo.setAddTime(this.gmtCreate);
        shoppingCartRetVo.setCouponActivity(this.couponActivity);
        return shoppingCartRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
