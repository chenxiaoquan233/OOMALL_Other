package cn.edu.xmu.oomall.other.model.bo;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.FavouriteGoodsPo;
import cn.edu.xmu.oomall.other.model.vo.Favorite.FavoritesRetVo;
import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Jx
 * @version 创建时间：2020/12/3 上午9:17
 */

@Data
public class FavoriteBo implements VoObject {
    private Long id;
    private Long customerId;
    private Long goodsSkuId;
    private GoodsSkuSimpleVo skuSimpleVo;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public FavoriteBo(FavouriteGoodsPo favouriteGoodsPo){
        this.id=favouriteGoodsPo.getId();
        this.customerId=favouriteGoodsPo.getCustomerId();
        this.goodsSkuId=favouriteGoodsPo.getGoodsSkuId();
        this.gmtCreate=favouriteGoodsPo.getGmtCreate();
        this.gmtModified=favouriteGoodsPo.getGmtModified();
    }

    @Override
    public Object createVo() {
        FavoritesRetVo favoritesRetVo=new FavoritesRetVo();
        favoritesRetVo.setId(this.id);
        favoritesRetVo.setGoodsSku(skuSimpleVo);
        favoritesRetVo.setGmtCreate(this.gmtCreate);
        return favoritesRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

}
