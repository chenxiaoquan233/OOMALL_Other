package cn.edu.xmu.oomall.other.service;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.FavoriteDao;
import cn.edu.xmu.oomall.other.model.bo.FavoriteBo;
import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jx
 * @version 创建时间：2020/12/3 上午8:57
 */
@Service
public class FavoriteService {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);

    @Autowired
    private FavoriteDao favoriteDao;

    public ReturnObject<PageInfo<VoObject>> getFavorites(Long userId, int page, int pageSize) {
        return favoriteDao.getFavoritesByUserId(userId,page,pageSize);
    }

    public Object addFavorites(Long userId, Long skuId) {
        GoodsSkuSimpleVo sku=new GoodsSkuSimpleVo(favoriteDao.iGoodsService.getSku(skuId));
        if(sku==null||sku.getId()==null){
            return null;
        }
        FavoriteBo favoriteBo=new FavoriteBo(favoriteDao.addFavorites(userId, skuId));
        favoriteBo.setSkuSimpleVo(sku);
        return Common.getRetObject(new ReturnObject<>(favoriteBo));
    }

    public ResponseCode deleteFavorites(Long userId,Long id){
        return favoriteDao.deletefavorites(userId,id);
    }
}
