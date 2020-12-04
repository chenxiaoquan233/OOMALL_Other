package cn.edu.xmu.oomall.other.model.vo.Address;

import lombok.Data;
import org.apache.tomcat.jni.Address;

import java.time.LocalDateTime;
/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午18:57
 */
@Data
public class AddressRetVo {
    private Long id;
    private Long regionId;
    private String detail;
    private String consignee;
    private String mobile;
    private Boolean beDefault;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;


}
