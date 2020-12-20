package cn.edu.xmu.oomall.other.model.vo.TimeSegment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author XQChen
 * @version 创建时间：2020/12/9 下午8:47
 */
@Data
public class TimeSegmentVo {
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
