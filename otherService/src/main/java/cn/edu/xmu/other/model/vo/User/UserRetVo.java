package cn.edu.xmu.other.model.vo.User;

import cn.edu.xmu.other.model.bo.UserBo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author XQChen
 * @version 创建时间：2020/11/24 上午7:52
 */
@Data
public class UserRetVo {
    private Long id;
    private String userName;
    private String realName;
    private String mobile;
    private String email;
    private Integer gender;
    private LocalDateTime birthday;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
