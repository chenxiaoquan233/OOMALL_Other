package cn.edu.xmu.oomall.other.model.vo.Address;

import lombok.Data;

import java.time.LocalDateTime;
/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午18:56
 */
@Data
public class RegionRetVo {
    private Long id;

    private Long pid;

    private String name;

    private String postalCode;

    private Integer state;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
