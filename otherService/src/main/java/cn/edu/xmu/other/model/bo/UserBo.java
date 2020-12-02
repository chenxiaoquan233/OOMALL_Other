package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.CustomerPo;
import cn.edu.xmu.other.model.vo.User.UserRetVo;
import cn.edu.xmu.other.model.vo.User.UserStateRetVo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XQChen
 * @version 创建时间：2020/11/23 下午6:39
 */
@Data
public class UserBo implements VoObject {
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
        FEMAIL(1, "女性"),
        SECRET(2, "保密");

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

    private Long id;
    private String userName;
    private String realName;
    private String password;
    private String mobile;
    private String email;
    private Gender gender = Gender.SECRET;
    private LocalDateTime birthday;
    private State state = State.NORM;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public UserBo() {}

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
    }

    public UserStateRetVo createUserStateVo() {
        UserStateRetVo userStateRetVo = new UserStateRetVo();
        userStateRetVo.setCode(this.state.code);
        userStateRetVo.setName(this.userName);
        return userStateRetVo;
    }

    public CustomerPo createUserPo() {
        CustomerPo customerPo = new CustomerPo();
        customerPo.setId(this.id);
        customerPo.setUserName(this.userName);
        customerPo.setRealName(this.realName);
        customerPo.setPassword(this.password);
        customerPo.setMobile(this.mobile);
        customerPo.setEmail(this.email);
        customerPo.setGender((byte) gender.code);
        customerPo.setBirthday(this.birthday);
        customerPo.setState((byte) state.code);
        customerPo.setGmtCreate(this.gmtCreate);
        customerPo.setGmtModified(this.gmtModified);
        return customerPo;
    }

    @Override
    public Object createVo() {
        UserRetVo userRetVo= new UserRetVo();
        userRetVo.setId(this.id);
        userRetVo.setUserName(this.userName);
        userRetVo.setRealName(this.realName);
        userRetVo.setMobile(this.mobile);
        userRetVo.setEmail(this.email);
        userRetVo.setGender(this.gender.code);
        userRetVo.setBirthday(this.birthday);
        userRetVo.setGmtCreate(this.gmtCreate);
        userRetVo.setGmtModified(this.gmtModified);
        return userRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
