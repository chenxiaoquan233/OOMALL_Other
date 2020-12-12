package cn.edu.xmu.oomall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/11 下午3:22
 * 4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeSharedDTO {
    private Long orderItemId;
    private Long skuId;
    private Long beSharedId;
    private Long customId;
}
