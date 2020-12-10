package cn.edu.xmu.oomall.other.model.vo.Aftersale;

import cn.edu.xmu.oomall.other.model.bo.AftersaleBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午15:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleStateVo {
    private Integer code;
    private String name;

    public AftersaleStateVo(AftersaleBo.State s) {
        this.code = s.getCode();
        this.name = s.getDescription();
    }
}
