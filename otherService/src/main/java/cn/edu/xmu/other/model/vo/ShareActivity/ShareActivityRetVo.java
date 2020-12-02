package cn.edu.xmu.other.model.vo.ShareActivity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午6:04
 */
@Data
public class ShareActivityRetVo {
    private Long id;
    private Long shopId;
    private Long goodSpuId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer state;
}
