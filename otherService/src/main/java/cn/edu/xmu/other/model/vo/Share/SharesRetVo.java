package cn.edu.xmu.other.model.vo.Share;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午6:01
 */
@Data
public class SharesRetVo {
    private Long id;
    private Long sharerId;
    private Long goodSpuId;
    private Integer quantity;
    private LocalDateTime gmtCreate;
}
