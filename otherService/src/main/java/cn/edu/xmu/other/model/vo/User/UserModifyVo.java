package cn.edu.xmu.other.model.vo.User;

import cn.edu.xmu.other.model.bo.UserBo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
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

    @Pattern(regexp = "[012]?", message = "性别错误")
    @ApiModelProperty(name = "性别", value = "1")
    private String gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "生日", value = "2020-01-01")
    private LocalDate birthday;

    public UserBo createBo() {
        UserBo userBo = new UserBo();

        userBo.setRealName(this.realName);
        userBo.setGender(UserBo.Gender.getTypeByCode(Integer.valueOf(this.gender)));
        userBo.setBirthday(LocalDateTime.of(this.birthday, LocalTime.of(0, 0)));

        return userBo;
    }
}
