package cn.edu.xmu.oomall.other.model.vo.TimeSegment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author XQChen
 * @version 创建时间：2020/12/9 下午8:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSegmentVo {
    @NotNull
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime beginTime;

    @NotNull
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;
}
