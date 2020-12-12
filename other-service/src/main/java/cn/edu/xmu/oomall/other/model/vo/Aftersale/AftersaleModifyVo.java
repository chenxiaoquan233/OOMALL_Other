package cn.edu.xmu.oomall.other.model.vo.Aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * @author XQChen
 * @version 创建时间：2020/12/12 上午10:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleModifyVo {

    @Min(value = 1)
    private Integer quantity;

    private String reason;

    @Min(value = 1)
    private Long regionId;

    private String detail;

    private String consignee;

    @Pattern(regexp = "[+]?[0-9*#]*")
    private String mobile;
}
