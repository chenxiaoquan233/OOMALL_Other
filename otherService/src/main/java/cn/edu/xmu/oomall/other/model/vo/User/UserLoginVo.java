package cn.edu.xmu.oomall.other.model.vo.User;

import cn.edu.xmu.oomall.other.model.bo.UserBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @author XQChen
 * @version 创建时间：2020/11/26 上午10:39
 */
@Data
@ApiModel
public class UserLoginVo {
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(name = "用户名", value = "testuser")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(name = "密码", value = "123456")
    private String password;

    public UserBo createBo() {
        UserBo userBo = new UserBo();
        userBo.setUserName(this.userName);
        userBo.setPassword(this.password);
        UserLoginVo vo = new UserLoginVo();
        return userBo;
    }
}
