package cn.edu.xmu.other.model.vo.FootPrint;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午4:53
 */
@Data
public class FootPrintRetVo {
    private Long id;
    private Long goodsSpuId;
    private LocalDateTime gmtCreate;
}
