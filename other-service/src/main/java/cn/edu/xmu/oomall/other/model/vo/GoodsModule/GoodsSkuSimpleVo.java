package cn.edu.xmu.oomall.other.model.vo.GoodsModule;

import cn.xmu.edu.goods.client.dubbo.SkuDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Jx
 * @version 创建时间：2020/12/9
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
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
