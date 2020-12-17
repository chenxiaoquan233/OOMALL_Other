package cn.edu.xmu.oomall.other.service;


import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.FavoriteDao;
import cn.edu.xmu.oomall.other.model.bo.FavoriteBo;
import cn.edu.xmu.oomall.other.model.po.FavouriteGoodsPo;
import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
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
 * @version 创建时间：2020/12/3 上午8:57
 */
@Service
public class FavoriteService {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);

    @Autowired
    private FavoriteDao favoriteDao;

    @DubboReference(version = "0.0.1-SNAPSHOT", check = false)
    public IGoodsService iGoodsService;

    public ReturnObject<PageInfo<VoObject>> getFavorites(Long userId, int page, int pageSize) {
        List<FavouriteGoodsPo> favoritesPos=favoriteDao.getFavoritesByUserId(userId,page,pageSize);
        List<VoObject> ret = favoritesPos.stream().map(FavoriteBo::new).
                map((x)->{x.setSkuSimpleVo(new GoodsSkuSimpleVo(iGoodsService.getSku(x.getGoodsSkuId())));return x;}).
                collect(Collectors.toList());
        PageInfo<FavouriteGoodsPo> favoritesPoPage = PageInfo.of(favoritesPos);
        PageInfo<VoObject> favoritesPage = new PageInfo<>(ret);
        favoritesPage.setPages(favoritesPoPage.getPages());
        favoritesPage.setPageNum(favoritesPoPage.getPageNum());
        favoritesPage.setPageSize(favoritesPoPage.getPageSize());
        favoritesPage.setTotal(favoritesPoPage.getTotal());
        return  new ReturnObject<>(favoritesPage);
    }

    public Object addFavorites(Long userId, Long skuId) {
        GoodsSkuSimpleVo sku=new GoodsSkuSimpleVo(iGoodsService.getSku(skuId));
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
