package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午15:16
 */
@Data
public class AftersaleOrderBo implements VoObject {
    /**
     * 售后类型
     */
    public enum Type {
        EXCHANGE(0, "换货"),
        MAINTAIN(1,"维修"),
        RETURN(2, "退货");

        private static final Map<Integer, AftersaleOrderBo.Type> typeMap;

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

        public static AftersaleOrderBo.Type getTypeByCode(Integer code) { return typeMap.get(code); }

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
        CANCELED(7,"取消"),
        OVER(8,"已结束");

        private static final Map<Integer, AftersaleOrderBo.State> stateMap;

        static {
            stateMap = new HashMap();
            Arrays.stream(AftersaleOrderBo.State.values()).forEach(enumitem -> stateMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static AftersaleOrderBo.State getTypeByCode(Integer code) { return stateMap.get(code); }

        public Integer getCode() { return code; }

        public String getDescription() { return description; }
    }
    private Long id;

    private Long orderId;

    private Long orderSn;

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
}
