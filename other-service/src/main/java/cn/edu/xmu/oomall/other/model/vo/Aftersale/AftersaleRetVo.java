package cn.edu.xmu.oomall.other.model.vo.Aftersale;

import lombok.Data;

import java.time.LocalDateTime;
/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午14:51
 */
@Data
public class AftersaleRetVo {
    private Long id;
    private Long orderItemId;
    private Long customerId;
    private Long shopId;
    private String serviceSn;
    private Integer type;
    private String reason;
    private String conclusion;
    private Long refund;
    private Integer quantity;
    private Long regionId;
    private String detail;
    private String consignee;
    private String mobile;
    private String customerLogSn;
    private String shopLogSn;
    private Byte state;
    private Byte beDeleted;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
