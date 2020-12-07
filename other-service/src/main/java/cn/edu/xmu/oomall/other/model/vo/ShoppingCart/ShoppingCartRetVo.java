package cn.edu.xmu.oomall.other.model.vo.ShoppingCart;

import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jx
 * @version 创建时间：2020/12/2 上午8:46
 */

@Data
public class ShoppingCartRetVo {
    private Long id;
    private Long goodsSkuId;
    private String skuName; //dispatch goods module
    private String spuName; //dispatch goods module
    private Integer quantity;
    private LocalDateTime addTime;
    private Long price;     //? history or now
    private List<VoObject> couponActivity; //dispatch goods module
}
