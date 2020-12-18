package cn.edu.xmu.oomall.other.model.vo.User;

import cn.edu.xmu.oomall.other.model.bo.UserBo;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author XQChen
 * @version 创建时间：2020/11/24 上午7:52
 */
@Data
public class UserRetVo {
    private Long id;
    private Integer state;
    private String userName;
    private String name;
    private String mobile;
    private String email;
    private Integer gender;
    private LocalDate birthday;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}