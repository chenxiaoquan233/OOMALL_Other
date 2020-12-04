package cn.edu.xmu.oomall.other.model.vo.User;

import cn.edu.xmu.oomall.other.model.bo.UserBo;
//
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author XQChen
 * @version 创建时间：2020/11/24 上午7:54
 */
@ApiModel
@Data
public class UserSignUpVo {
    @NotBlank
    @Pattern(regexp = "[+]?[0-9*#]*")
    @ApiModelProperty(name = "手机号", value = "12300000000")
    private String mobile;

    @NotBlank
    @Email
    @ApiModelProperty(name = "邮箱", value = "testuser@test.com")
    private String email;

    @NotBlank
    @ApiModelProperty(name = "用户名", value = "testuser")
    private String userName;

    @NotBlank
    @ApiModelProperty(name = "密码", value = "123456")
    private String password;

    @NotBlank
    @ApiModelProperty(name = "真实姓名", value = "陈xx")
    private String realName;

    @NotBlank
    @Pattern(regexp = "[012]")
    @ApiModelProperty(name = "性别", value = "1")
    private String gender;

    @NotNull
    @Past
    @ApiModelProperty(name = "生日", value = "2020-01-01")
    private LocalDate birthday;

    public UserBo createBo() {
        UserBo userBo = new UserBo();
        userBo.setMobile(this.mobile);
        userBo.setEmail(this.email);
        userBo.setUserName(this.userName);
        userBo.setPassword(this.password);
        userBo.setRealName(this.realName);
        userBo.setGender(UserBo.Gender.getTypeByCode(Integer.valueOf(this.gender)));
        userBo.setBirthday(LocalDateTime.of(birthday, LocalTime.of(0, 0)));

        return userBo;
    }
}
