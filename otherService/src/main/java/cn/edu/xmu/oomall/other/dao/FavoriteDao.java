package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.FavouriteGoodsPoMapper;
import cn.edu.xmu.oomall.other.model.bo.FavoriteBo;
import cn.edu.xmu.oomall.other.model.bo.ShoppingCartBo;
import cn.edu.xmu.oomall.other.model.po.FavouriteGoodsPo;
import cn.edu.xmu.oomall.other.model.po.FavouriteGoodsPoExample;
import cn.edu.xmu.oomall.other.model.po.ShoppingCartPo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jx
 * @version 创建时间：2020/12/3 8:59
 */

@Repository
public class FavoriteDao {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteDao.class);

    @Autowired
    private FavouriteGoodsPoMapper favouriteGoodsPoMapper;

    public ReturnObject<PageInfo<VoObject>> getFavoritesByUserId(Long userId, Integer page, Integer pageSize){
        FavouriteGoodsPoExample favouriteGoodsPoExample=new FavouriteGoodsPoExample();
        FavouriteGoodsPoExample.Criteria criteria=favouriteGoodsPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        List<FavouriteGoodsPo> favoritesPos = null;
        PageHelper.startPage(page,pageSize,true,true,null);
        try {
            favoritesPos = favouriteGoodsPoMapper.selectByExample(favouriteGoodsPoExample);
        }catch (DataAccessException e){
            logger.error("findFavorites: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        List<VoObject> ret = favoritesPos.stream().map(FavoriteBo::new).collect(Collectors.toList());
        PageInfo<FavouriteGoodsPo> favoritesPoPage = PageInfo.of(favoritesPos);
        PageInfo<VoObject> favoritesPage = new PageInfo<>(ret);
        favoritesPage.setPages(favoritesPoPage.getPages());
        favoritesPage.setPageNum(favoritesPoPage.getPageNum());
        favoritesPage.setPageSize(favoritesPoPage.getPageSize());
        favoritesPage.setTotal(favoritesPoPage.getTotal());
        return new ReturnObject<>(favoritesPage);
    }

    public ReturnObject<VoObject> addFavorites(Long userId, Long skuId) {
        /*判断是否已经有此收藏*/
        FavouriteGoodsPoExample favouriteGoodsPoExample=new FavouriteGoodsPoExample();
        FavouriteGoodsPoExample.Criteria criteria=favouriteGoodsPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        criteria.andGoodsSkuIdEqualTo(skuId);
        List<FavouriteGoodsPo> favoritesPos = favouriteGoodsPoMapper.selectByExample(favouriteGoodsPoExample);
        /*若有，返回*/
        if(favoritesPos.size()>0)
            return new ReturnObject<>(new FavoriteBo(favoritesPos.get(0)));
        /*若没有，插入*/
        FavouriteGoodsPo record=new FavouriteGoodsPo();
        record.setCustomerId(userId);
        record.setGoodsSkuId(skuId);
        record.setGmtCreate(LocalDateTime.now());
        favouriteGoodsPoMapper.insertSelective(record);
        return new ReturnObject<>(new FavoriteBo(record));
    }

    public ResponseCode deletefavorites(Long userId, Long id){
        FavouriteGoodsPoExample favouriteGoodsPoExample=new FavouriteGoodsPoExample();
        FavouriteGoodsPoExample.Criteria criteria=favouriteGoodsPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        criteria.andIdEqualTo(id);
        favouriteGoodsPoMapper.deleteByExample(favouriteGoodsPoExample);
        return ResponseCode.OK;
    }
}
