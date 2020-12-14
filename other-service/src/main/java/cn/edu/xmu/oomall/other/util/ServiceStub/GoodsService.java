package cn.edu.xmu.oomall.other.util.ServiceStub;

import cn.xmu.edu.goods.client.IGoodsService;
import cn.xmu.edu.goods.client.dubbo.OrderItemDTO;
import cn.xmu.edu.goods.client.dubbo.ShopDTO;
import cn.xmu.edu.goods.client.dubbo.SkuDTO;
import cn.xmu.edu.goods.client.dubbo.SpuDTO;

import java.util.List;
import java.util.Map;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/14 下午7:49
 * 4
 */
public class GoodsService implements IGoodsService {
    @Override
    public Long getPrice(Long skuId) {
        return null;
    }

    @Override
    public Map<ShopDTO, List<OrderItemDTO>> classifySku(List<OrderItemDTO> orderItemDTOS) {
        return null;
    }

    @Override
    public SkuDTO getSku(Long skuId) {
        return null;
    }

    @Override
    public SpuDTO getSimpleSpuById(Long spuId) {
        return null;
    }

    @Override
    public ShopDTO getShopBySKUId(Long skuId) {
        return new ShopDTO(1L);
    }
}
