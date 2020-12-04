package cn.edu.xmu.oomall.other.model.vo.Advertisement;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午5:39
 */
@Data
public class AdvertiseRetVo {
    private Long id;
    private String link;
    private String imagePath;
    private String content;
    private Long segId;
    private String state;
    private Integer weight;
    private Boolean default_;
    private LocalDate beginDate;
    private LocalDate endDate;
    private Boolean repeat;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
