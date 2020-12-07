package cn.edu.xmu.oomall.other.model.vo.User;

import cn.edu.xmu.oomall.other.model.bo.UserBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author XQChen
 * @version 创建时间：2020/11/29 下午11:23
 */
@Data
@ApiModel
public class UserModifyVo {
    @ApiModelProperty(name = "真实姓名", value = "陈xx")
    private String realName;

    @Pattern(regexp = "[01]", message = "性别错误")
    @ApiModelProperty(name = "性别", value = "1")
    private String gender;

    @Past
    @ApiModelProperty(name = "生日", value = "2020-01-01")
    private LocalDate birthday;

    public UserBo createBo() {
        UserBo userBo = new UserBo();

        userBo.setRealName(this.realName);
        userBo.setGender(UserBo.Gender.getTypeByCode(Integer.valueOf(this.gender)));
        userBo.setBirthday(this.birthday);

        return userBo;
    }
}
