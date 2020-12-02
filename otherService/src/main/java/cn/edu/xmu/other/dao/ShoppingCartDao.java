package cn.edu.xmu.other.dao;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.mapper.CustomerPoMapper;
import cn.edu.xmu.other.mapper.ShoppingCartPoMapper;
import cn.edu.xmu.other.model.bo.ShoppingCartBo;
import cn.edu.xmu.other.model.po.ShoppingCartPo;
import cn.edu.xmu.other.model.po.ShoppingCartPoExample;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
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

    public ResponseCode deleteCart(Long cartId){
        shoppingCartPoMapper.deleteByPrimaryKey(cartId);
        return ResponseCode.OK;
    }

    public ReturnObject<PageInfo<VoObject>> getCartByUserId(Long userId, Integer page, Integer pageSize){
        ShoppingCartPoExample shoppingCartPoExample=new ShoppingCartPoExample();
        ShoppingCartPoExample.Criteria criteria=shoppingCartPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        PageHelper.startPage(page==null?1:page, pageSize==null?10:page);
        List<ShoppingCartPo> cartPos = null;
        try {
            cartPos = shoppingCartPoMapper.selectByExample(shoppingCartPoExample);
        }catch (DataAccessException e){
            logger.error("findCarts: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        List<VoObject> ret = cartPos.stream().map(x->new ShoppingCartBo(x)).collect(Collectors.toList());
        PageInfo<ShoppingCartPo> cartPoPage = PageInfo.of(cartPos);
        PageInfo<VoObject> cartPage = new PageInfo<>(ret);
        cartPage.setPages(cartPoPage.getPages());
        cartPage.setPageNum(cartPoPage.getPageNum());
        cartPage.setPageSize(cartPoPage.getPageSize());
        cartPage.setTotal(cartPoPage.getTotal());
        return new ReturnObject<>(cartPage);
    }

    public ReturnObject addCart(Long userId, Long goodsSkuId,Integer quantity,Long price){
        return null;
    }

    public ReturnObject modifyCart(Long id, Long goodsSkuId,Integer quantity,Long price){
        return null;
    }
}
