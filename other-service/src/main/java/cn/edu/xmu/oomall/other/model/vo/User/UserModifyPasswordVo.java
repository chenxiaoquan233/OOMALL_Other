package cn.edu.xmu.oomall.other.model.vo.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author XQChen
 * @version 创建时间：2020/12/8 下午9:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyPasswordVo {
    @NotBlank
    private String captcha;

    @NotBlank
    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W){6}")
    private String newPassword;
}
