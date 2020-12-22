package cn.edu.xmu.oomall.other.model.vo.Aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author XQChen
 * @version 创建时间：2020/12/12 下午3:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleReceiveVo {
    @NotNull
    private Boolean confirm;

    private String conclusion;
}
