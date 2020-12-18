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
import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
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

//    public Long getPrice(Long skuId){
//        return  skuId*100;
//    }
//    public SkuDTO getSku(Long id){
//        SkuDTO sku=new SkuDTO();
//        sku.setId(id);
//        sku.setName("打桩");
//        sku.setSkuSn("0");
//        sku.setImageUrl("打桩.jpg");
//        sku.setInventory(10);
//        sku.setOriginalPrice(100L);
//        sku.setPrice(100L);
//        sku.setDisable((byte)0);
//        return sku;
//    }

    @Autowired
    private FavouriteGoodsPoMapper favouriteGoodsPoMapper;



    public List<FavouriteGoodsPo> getFavoritesByUserId(Long userId, Integer page, Integer pageSize){
        FavouriteGoodsPoExample favouriteGoodsPoExample=new FavouriteGoodsPoExample();
        FavouriteGoodsPoExample.Criteria criteria=favouriteGoodsPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        List<FavouriteGoodsPo> favoritesPos = null;
        PageHelper.startPage(page,pageSize,true,true,null);
        try {
            favoritesPos = favouriteGoodsPoMapper.selectByExample(favouriteGoodsPoExample);
        }catch (DataAccessException e){
            logger.error("findFavorites: DataAccessException:" + e.getMessage());
            return null;
        }
        return favoritesPos;
    }

    public FavouriteGoodsPo addFavorites(Long userId, Long skuId) {
        /*判断是否已经有此收藏*/
        FavouriteGoodsPoExample favouriteGoodsPoExample=new FavouriteGoodsPoExample();
        FavouriteGoodsPoExample.Criteria criteria=favouriteGoodsPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        criteria.andGoodsSkuIdEqualTo(skuId);
        List<FavouriteGoodsPo> favoritesPos = favouriteGoodsPoMapper.selectByExample(favouriteGoodsPoExample);
        /*若有，返回*/
        if(favoritesPos.size()>0)
            return favoritesPos.get(0);
        /*若没有，插入*/
        FavouriteGoodsPo record=new FavouriteGoodsPo();
        record.setCustomerId(userId);
        record.setGoodsSkuId(skuId);
        record.setGmtCreate(LocalDateTime.now());
        favouriteGoodsPoMapper.insertSelective(record);
        return record;
    }

    public ResponseCode deletefavorites(Long userId, Long id){
        FavouriteGoodsPo favouriteGoodsPo=favouriteGoodsPoMapper.selectByPrimaryKey(id);
        /*资源不存在*/
        if(favouriteGoodsPo==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        /*资源id非自己对象*/
        if(!favouriteGoodsPo.getCustomerId().equals(userId))
            return ResponseCode.RESOURCE_ID_OUTSCOPE;
        /*开始删除*/
        if(favouriteGoodsPoMapper.deleteByPrimaryKey(id)==1)
            return ResponseCode.OK;
        else return ResponseCode.INTERNAL_SERVER_ERR;
//        FavouriteGoodsPoExample favouriteGoodsPoExample=new FavouriteGoodsPoExample();
//        FavouriteGoodsPoExample.Criteria criteria=favouriteGoodsPoExample.createCriteria();
//        criteria.andCustomerIdEqualTo(userId);
//        criteria.andIdEqualTo(id);
//        int ret=favouriteGoodsPoMapper.deleteByExample(favouriteGoodsPoExample);
//        if(ret!=1)
//            return ResponseCode.AUTH_ID_NOTEXIST;
//        else return ResponseCode.OK;

    }
}
