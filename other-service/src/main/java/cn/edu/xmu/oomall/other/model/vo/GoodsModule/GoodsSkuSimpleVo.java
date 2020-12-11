package cn.edu.xmu.oomall.other.model.vo.GoodsModule;

import cn.xmu.edu.goods.client.dubbo.SkuDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Jx
 * @version 创建时间：2020/12/9
 */

@Data
public class GoodsSkuSimpleVo  {
    private Long id;
    private String name;
    private String skuSn;
    private String imageUrl;
    private Integer inventory;
    private Long originalPrice;
    private Long price;
    private Byte disable;

    public GoodsSkuSimpleVo(SkuDTO sku){
        id=sku.getId();
        name=sku.getName();
        skuSn=sku.getSkuSn();
        imageUrl=sku.getImageUrl();
        inventory=sku.getInventory();
        originalPrice=sku.getOriginalPrice();
        price=sku.getPrice();
        disable=sku.getDisable();
    }
}
