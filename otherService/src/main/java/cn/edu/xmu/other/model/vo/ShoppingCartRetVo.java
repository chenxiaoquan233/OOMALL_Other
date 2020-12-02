package cn.edu.xmu.other.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Jx
 * @version 创建时间：2020/12/2 上午8:46
 */

@Data
public class ShoppingCartRetVo {
    private Long id;
    private Long customerId;
    private Long goodsSkuId;
    private Integer quantity;
    private Long price;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
