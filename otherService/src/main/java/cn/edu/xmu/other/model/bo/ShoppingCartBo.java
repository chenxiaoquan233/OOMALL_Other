package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.ShoppingCartPo;
import cn.edu.xmu.other.model.vo.ShoppingCart.ShoppingCartRetVo;
import lombok.Data;
import java.time.LocalDateTime;


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
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public ShoppingCartBo(ShoppingCartPo shoppingCartPo){
        this.id=shoppingCartPo.getId();
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
        return shoppingCartRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
