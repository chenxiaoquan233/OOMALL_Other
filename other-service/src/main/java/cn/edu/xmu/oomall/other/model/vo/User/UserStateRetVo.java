package cn.edu.xmu.oomall.other.model.vo.User;

import cn.edu.xmu.oomall.other.model.bo.UserBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XQChen
 * @version 创建时间：2020/12/8 上午11:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStateRetVo {
    private Integer code;
    private String name;

    public UserStateRetVo(Integer code) {
        this.code = code;
        this.name = UserBo.State.getTypeByCode(code).getDescription();
    }
}
