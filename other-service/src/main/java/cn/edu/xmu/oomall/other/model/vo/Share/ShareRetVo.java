package cn.edu.xmu.oomall.other.model.vo.Share;

import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午6:01
 */
@Data
public class ShareRetVo {
    private Long id;
    private Long sharerId;
    private GoodsSkuSimpleVo sku;
    private Integer quantity;
    private LocalDateTime gmtCreate;
}
