package cn.edu.xmu.other.model.vo.User;

import cn.edu.xmu.other.model.bo.UserBo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @NotBlank(message = "必须输入手机号")
    @Pattern(regexp = "[+]?[0-9*#]+", message = "手机号码格式不正确")
    @ApiModelProperty(name = "手机号", value = "12300000000")
    private String mobile;

    @NotBlank(message = "必须输入邮箱")
    @Email(message = "Email 格式不正确")
    @ApiModelProperty(name = "邮箱", value = "testuser@test.com")
    private String email;

    @NotBlank(message = "必须输入用户名")
    @ApiModelProperty(name = "用户名", value = "testuser")
    private String userName;

    @NotBlank(message = "必须输入密码")
    @ApiModelProperty(name = "密码", value = "123456")
    private String password;

    @NotBlank(message = "必须输入真实姓名")
    @ApiModelProperty(name = "真实姓名", value = "陈xx")
    private String realName;

    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "[012]", message = "性别错误")
    @ApiModelProperty(name = "性别", value = "1")
    private String gender;

    @NotNull(message = "生日不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
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
