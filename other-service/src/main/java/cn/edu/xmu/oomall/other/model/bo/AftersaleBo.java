package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.dto.AftersaleDto;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.AftersaleSkuRetVo;
import cn.edu.xmu.oomall.service.IDubboOrderService;
import cn.edu.xmu.oomall.other.model.po.AftersalePo;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.AftersaleRetVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.dubbo.config.annotation.DubboReference;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午15:16
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleBo implements VoObject {
    /**
     * 售后类型
     */
    public enum Type {
        EXCHANGE(0, "换货"),
        MAINTAIN(1,"维修"),
        RETURN(2, "退货");

        private static final Map<Integer, AftersaleBo.Type> typeMap;

        static {
            typeMap = new HashMap();
            Arrays.stream(Type.values()).forEach(enumitem -> typeMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        Type(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static AftersaleBo.Type getTypeByCode(Integer code) { return typeMap.get(code); }

        public Integer getCode() { return code; }

        public String getDescription() { return description; }
    }

    /***
     * 售后单状态
     */
    public enum State {
        WAIT_ADMIN_AUDIT(0, "待管理员审核"),
        WAIT_CUSTOMER_DELIVER(1, "待买家发货"),
        CUSTOMER_DELIVERED(2, "买家已发货"),
        WAIT_SHOP_REFUND(3, "待店家退款"),
        WAIT_SHOP_DELIVER(4,"待店家发货"),
        SHOP_DELIVERED(5,"店家已发货"),
        AUDIT_NOT_PASS(6,"审核不通过"),
        CANCELED(7,"已取消"),
        OVER(8,"已结束");

        private static final Map<Integer, AftersaleBo.State> stateMap;

        static {
            stateMap = new HashMap();
            Arrays.stream(AftersaleBo.State.values()).forEach(enumitem -> stateMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static AftersaleBo.State getTypeByCode(Integer code) { return stateMap.get(code); }

        public Integer getCode() { return code; }

        public String getDescription() { return description; }
    }
    private Long id;
    private Long orderId;
    private String orderSn;
    private Long orderItemId;
    private Long skuId;
    private String skuName;
    private Long customerId;
    private Long shopId;
    private String serviceSn;
    private Type type = Type.RETURN;
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
    private State state;
    private Byte beDeleted;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;


    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public AftersaleBo(AftersalePo po) {
        this.customerId = po.getCustomerId();
        this.beDeleted = po.getBeDeleted();
        this.conclusion = po.getConclusion();
        this.type = AftersaleBo.Type.getTypeByCode(po.getType().intValue());
        this.shopId = po.getShopId();
        this.state = AftersaleBo.State.getTypeByCode(po.getState().intValue());
        this.consignee = po.getConsignee();
        this.customerLogSn = po.getCustomerLogSn();
        this.detail = po.getDetail();
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
        this.mobile = po.getMobile();
        this.orderItemId = po.getOrderItemId();
        this.quantity = po.getQuantity();
        this.reason = po.getReason();
        this.refund = po.getRefund();
        this.regionId = po.getRegionId();
        this.serviceSn = po.getServiceSn();
        this.shopLogSn = po.getShopLogSn();
        this.id = po.getId();
    }

    public AftersaleBo setDTO(AftersaleDto aftersaleDTO) {
        System.out.println("DTO:" + aftersaleDTO);

        this.orderId = aftersaleDTO.getOrderId();
        this.orderSn = aftersaleDTO.getOrderSn();
        this.skuId = aftersaleDTO.getSkuId();
        this.skuName = aftersaleDTO.getSkuName();

        return this;
    }

    public AftersalePo createPo() {
        AftersalePo po = new AftersalePo();
        po.setId(this.id);
        po.setOrderItemId(this.orderItemId);
        po.setBeDeleted(this.beDeleted);
        po.setConclusion(this.conclusion);
        po.setConsignee(this.consignee);
        po.setCustomerId(this.customerId);
        po.setDetail(this.detail);
        po.setCustomerLogSn(this.customerLogSn);
        po.setGmtCreate(this.gmtCreate);
        po.setGmtModified(this.gmtModified);
        po.setMobile(this.mobile);
        po.setQuantity(this.quantity);
        po.setReason(this.reason);
        po.setRefund(this.refund);
        po.setRegionId(this.regionId);
        po.setServiceSn(this.serviceSn);
        po.setShopId(this.shopId);
        po.setShopLogSn(this.shopLogSn);
        po.setState(this.state.getCode().byteValue());
        po.setType(this.type.getCode().byteValue());

        return po;
    }

    public AftersaleRetVo createRetVo() {
        AftersaleRetVo vo = new AftersaleRetVo();
        vo.setConsignee(this.consignee);
        vo.setCustomerId(this.customerId);
        vo.setCustomerLogSn(this.customerLogSn);
        vo.setDetail(this.detail);
        vo.setId(this.id);
        vo.setMobile(this.mobile);
        vo.setOrderItemId(this.orderItemId);
        vo.setQuantity(this.quantity);
        vo.setReason(this.reason);
        vo.setRefund(this.refund);
        vo.setRegionId(this.regionId);
        vo.setServiceSn(this.serviceSn);
        vo.setShopId(this.shopId);
        vo.setShopLogSn(this.shopLogSn);
        vo.setState(this.state.getCode().byteValue());
        vo.setType(this.type.getCode().byteValue());

        return vo;
    }

    public AftersaleSkuRetVo createSkuRetVo() {
        AftersaleSkuRetVo vo = new AftersaleSkuRetVo();
        vo.setSkuId(this.skuId);
        vo.setSkuName(this.skuName);
        vo.setConsignee(this.consignee);
        vo.setCustomerId(this.customerId);
        vo.setCustomerLogSn(this.customerLogSn);
        vo.setDetail(this.detail);
        vo.setId(this.id);
        vo.setMobile(this.mobile);
        vo.setOrderItemId(this.orderItemId);
        vo.setQuantity(this.quantity);
        vo.setReason(this.reason);
        vo.setRefund(this.refund);
        vo.setRegionId(this.regionId);
        vo.setServiceSn(this.serviceSn);
        vo.setShopId(this.shopId);
        vo.setShopLogSn(this.shopLogSn);
        vo.setState(this.state.getCode().byteValue());
        vo.setType(this.type.getCode().byteValue());

        return vo;
    }
}
