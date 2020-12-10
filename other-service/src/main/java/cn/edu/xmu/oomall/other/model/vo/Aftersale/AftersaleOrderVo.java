package cn.edu.xmu.oomall.other.model.vo.Aftersale;

import lombok.Data;
/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午15:13
 */
@Data
public class AftersaleOrderVo {
    private Long id;
    private Long orderId;
    private String orderSn;
    private Long orderItemId;
    private Long skuId;
    private String skuName;
    private Long customerId;
    private Long shopId;
    private String serviceSn;
    private Integer type;
    private String reason;
    private Long refund;
    private Integer quantity;
    private Long regionId;
    private String detail;
    private String consignee;
    private String mobile;
    private String customerLogSn;
    private String shopLogSn;
    private Integer state;

}
