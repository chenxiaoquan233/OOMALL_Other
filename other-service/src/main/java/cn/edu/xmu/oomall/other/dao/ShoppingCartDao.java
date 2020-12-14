package cn.edu.xmu.oomall.other.dao;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.CustomerPoMapper;
import cn.edu.xmu.oomall.other.mapper.ShoppingCartPoMapper;
import cn.edu.xmu.oomall.other.model.bo.ShoppingCartBo;
import cn.edu.xmu.oomall.other.model.po.FavouriteGoodsPo;
import cn.edu.xmu.oomall.other.model.po.ShoppingCartPo;
import cn.edu.xmu.oomall.other.model.po.ShoppingCartPoExample;
import cn.edu.xmu.goods.client.IGoodsService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @author Jx
 * @version 创建时间：2020/11/29
 */

@Repository
public class ShoppingCartDao {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartDao.class);

    @Autowired
    private ShoppingCartPoMapper shoppingCartPoMapper;


    public ResponseCode clearCart(Long userId){
        ShoppingCartPoExample shoppingCartPoExample=new ShoppingCartPoExample();
        ShoppingCartPoExample.Criteria criteria=shoppingCartPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        shoppingCartPoMapper.deleteByExample(shoppingCartPoExample);
        return ResponseCode.OK;
    }

    public ResponseCode deleteCart(Long userId,Long cartId){
        ShoppingCartPo po=shoppingCartPoMapper.selectByPrimaryKey(cartId);
        /*资源不存在*/
        if(po==null)
            return ResponseCode.RESOURCE_ID_NOTEXIST;
        /*资源id非自己对象*/
        if(po.getCustomerId()!=userId)
            return ResponseCode.RESOURCE_ID_OUTSCOPE;
        /*开始删除*/
        if(shoppingCartPoMapper.deleteByPrimaryKey(cartId)==1)
            return ResponseCode.OK;
        else return ResponseCode.INTERNAL_SERVER_ERR;
    }

    public void deleteCartByCustomerAndSku(Long userId,Long skuId){
        ShoppingCartPoExample example=new ShoppingCartPoExample();
        ShoppingCartPoExample.Criteria criteria=example.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        criteria.andGoodsSkuIdEqualTo(skuId);
        shoppingCartPoMapper.deleteByExample(example);
    }

    public List<ShoppingCartPo> getCartByUserId(Long userId, Integer page, Integer pageSize){
        ShoppingCartPoExample shoppingCartPoExample=new ShoppingCartPoExample();
        ShoppingCartPoExample.Criteria criteria=shoppingCartPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        List<ShoppingCartPo> cartPos = null;
        PageHelper.startPage(page,pageSize,true,true,null);
        try {
            cartPos = shoppingCartPoMapper.selectByExample(shoppingCartPoExample);
        }catch (DataAccessException e){
            logger.error("findCarts: DataAccessException:" + e.getMessage());
            return null;
        }
        return cartPos;
    }

    public ShoppingCartPo addCart(Long userId, Long goodsSkuId,Integer quantity,Long price){
        ShoppingCartPoExample example=new ShoppingCartPoExample();
        ShoppingCartPoExample.Criteria criteria=example.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        criteria.andGoodsSkuIdEqualTo(goodsSkuId);
        List<ShoppingCartPo> oldPos=shoppingCartPoMapper.selectByExample(example);
        if(oldPos!=null&&oldPos.size()>0){
            ShoppingCartPo po=oldPos.get(0);
            po.setQuantity(po.getQuantity()+quantity);
            po.setPrice(price);
            po.setGmtModified(LocalDateTime.now());
            shoppingCartPoMapper.updateByPrimaryKeySelective(po);
            return po;
        }
        ShoppingCartPo po=new ShoppingCartPo();
        po.setCustomerId(userId);
        po.setGoodsSkuId(goodsSkuId);
        po.setQuantity(quantity);
        po.setPrice(price);
        po.setGmtCreate(LocalDateTime.now());
        shoppingCartPoMapper.insertSelective(po);
        return po;
    }

    public ResponseCode modifyCart(Long cartId, Long userId, Long goodsSkuId,Integer quantity,Long price){
        ShoppingCartPo po=new ShoppingCartPo();
        po.setGoodsSkuId(goodsSkuId);
        po.setQuantity(quantity);
        po.setPrice(price);
        po.setGmtModified(LocalDateTime.now());
        ShoppingCartPoExample example=new ShoppingCartPoExample();
        ShoppingCartPoExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(cartId);
        criteria.andCustomerIdEqualTo(userId);
        int ret=shoppingCartPoMapper.updateByExampleSelective(po,example);
        if(ret==1)
         return ResponseCode.OK;
        else return ResponseCode.AUTH_ID_NOTEXIST;
    }
}
