package cn.edu.xmu.oomall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XQChen
 * @version 创建时间：2020/12/11 上午11:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    Long orderId;
    Long orderSn;
    Long skuId;
    String skuName;
    Long shopId;


}
