package cn.edu.xmu.oomall.other.model.vo.User;

import cn.edu.xmu.oomall.other.model.bo.UserBo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author XQChen
 * @version 创建时间：2020/12/7 上午11:48
 */
@Data
@ApiModel
public class UserResetPasswordVo {
    @NotBlank
    private String userName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String mobile;

    public UserBo createUserBo() {
        UserBo userBo = new UserBo();
        userBo.setUserName(this.userName);
        userBo.setEmail(this.email);
        userBo.setMobile(this.mobile);
        return userBo;
    }
}
