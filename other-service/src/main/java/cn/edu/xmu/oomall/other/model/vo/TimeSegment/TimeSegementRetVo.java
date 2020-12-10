package cn.edu.xmu.oomall.other.model.vo.TimeSegment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author XQChen
 * @version 创建时间：2020/12/9 下午8:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSegementRetVo {
    private Integer id;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
