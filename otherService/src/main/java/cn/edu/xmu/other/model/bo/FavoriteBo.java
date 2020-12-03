package cn.edu.xmu.other.model.bo;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.FavouriteGoodsPo;
import cn.edu.xmu.other.model.vo.Favorite.FavoritesRetVo;
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
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public FavoriteBo(FavouriteGoodsPo favouriteGoodsPo){
        this.id=favouriteGoodsPo.getId();
        this.customerId=favouriteGoodsPo.getCustomerId();
        this.goodsSkuId=favouriteGoodsPo.getGoodsSpuId();
        this.gmtCreate=favouriteGoodsPo.getGmtCreate();
        this.gmtModified=favouriteGoodsPo.getGmtModified();
    }

    @Override
    public Object createVo() {
        FavoritesRetVo favoritesRetVo=new FavoritesRetVo();
        favoritesRetVo.setId(this.id);
        favoritesRetVo.setGoodsSpuId(this.goodsSkuId);
        favoritesRetVo.setGmtCreate(this.gmtCreate);
        return favoritesRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
