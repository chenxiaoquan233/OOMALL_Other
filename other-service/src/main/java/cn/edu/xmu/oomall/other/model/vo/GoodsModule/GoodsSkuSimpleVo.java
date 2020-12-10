package cn.edu.xmu.oomall.other.model.vo.GoodsModule;

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
    private Integer originalPrice;
    private Integer price;
    private Boolean disable;

    public GoodsSkuSimpleVo(Long id){
        this.id=id;
        name="打桩";
        skuSn="0";
        imageUrl="打桩.jpg";
        inventory=10;
        originalPrice=100;
        price=100;
        disable=false;
    }
}
