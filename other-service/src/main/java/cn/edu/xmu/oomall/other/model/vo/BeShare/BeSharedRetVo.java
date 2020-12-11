package cn.edu.xmu.oomall.other.model.vo.BeShare;

import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/11 下午7:56
 * 4
 */
@Data
public class BeSharedRetVo {
    private Long id;
    private GoodsSkuSimpleVo sku;
    private Long sharerId;
    private Long customId;
    private Long orderId;
    private Integer rebate;
    private LocalDateTime gmtCreate;
}
