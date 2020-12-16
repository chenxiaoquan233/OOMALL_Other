package cn.edu.xmu.oomall.other.model.vo.Aftersale;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午14:51
 */
@Data
@ToString
public class AftersaleSkuRetVo {
    private Long id;
    private Long orderId;
    private Long orderItemId;
    private Long skuId;
    private String skuName;
    private Long customerId;
    private Long shopId;
    private String serviceSn;
    private Byte type;
    private String reason;
    private Long refund;
    private Integer quantity;
    private Long regionId;
    private String detail;
    private String consignee;
    private String mobile;
    private String customerLogSn;
    private String shopLogSn;
    private Byte state;
}
