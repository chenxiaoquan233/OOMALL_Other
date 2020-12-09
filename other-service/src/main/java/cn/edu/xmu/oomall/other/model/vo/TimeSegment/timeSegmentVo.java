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
public class timeSegmentVo {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime beginTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime endTime;
}
