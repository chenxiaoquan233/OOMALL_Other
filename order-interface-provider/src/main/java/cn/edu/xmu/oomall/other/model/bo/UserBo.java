package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.dto.CustomerDTO;
import cn.edu.xmu.oomall.other.model.po.CustomerPo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XQChen
 * @version 创建时间：2020/12/10 下午7:27
 */
@Data
@NoArgsConstructor
public class UserBo{
    /***
     * 买家状态
     */
    public enum State {
        BACK(0, "后台"),
        NORM(4, "正常"),
        FORBID(6, "封禁");

        private static final Map<Integer, State> stateMap;

        static {
            stateMap = new HashMap();
            Arrays.stream(State.values()).forEach(enumitem -> stateMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static UserBo.State getTypeByCode(Integer code) { return stateMap.get(code); }

        public Integer getCode() { return code; }

        public String getDescription() { return description; }
    }

    public enum Gender {
        MALE(0, "男性"),
        FEMAIL(1, "女性");

        private static final Map<Integer, Gender> stateMap;

        static {
            stateMap = new HashMap();
            Arrays.stream(Gender.values()).forEach(enumitem -> stateMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        Gender(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static UserBo.Gender getTypeByCode(Integer code) { return stateMap.get(code); }

        public Integer getCode() { return code; }

        public String getDescription() { return description; }
    }

    public enum Deleted {
        NOT_DELETED(0, "未删除"),
        DELETED(1, "已删除");

        private static final Map<Integer, Deleted> stateMap;

        static {
            stateMap = new HashMap();
            Arrays.stream(Deleted.values()).forEach(enumitem -> stateMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        Deleted(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static UserBo.Deleted getTypeByCode(Integer code) { return stateMap.get(code); }

        public Integer getCode() { return code; }

        public String getDescription() { return description; }
    }

    private Long id;
    private String userName;
    private String realName;
    private String password;
    private String mobile;
    private String email;
    private Integer point;
    private Gender gender;
    private Deleted deleted = Deleted.NOT_DELETED;
    private LocalDate birthday;
    private State state = State.NORM;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public UserBo(CustomerPo customerPo) {
        this.id = customerPo.getId();
        this.userName = customerPo.getUserName();
        this.realName = customerPo.getRealName();
        this.password = customerPo.getPassword();
        this.mobile = customerPo.getMobile();
        this.email = customerPo.getEmail();
        this.gender = UserBo.Gender.getTypeByCode(customerPo.getGender().intValue());
        this.birthday = customerPo.getBirthday();
        this.state = UserBo.State.getTypeByCode(customerPo.getState().intValue());
        this.gmtCreate = customerPo.getGmtCreate();
        this.gmtModified = customerPo.getGmtModified();
        this.deleted = UserBo.Deleted.getTypeByCode(customerPo.getBeDeleted().intValue());
        this.point = customerPo.getPoint();
    }

    public CustomerDTO createCustomer() {
        CustomerDTO customer = new CustomerDTO();
        customer.setId(this.id);
        customer.setRealName(this.realName);
        customer.setUserName(this.userName);

        return customer;
    }
}

