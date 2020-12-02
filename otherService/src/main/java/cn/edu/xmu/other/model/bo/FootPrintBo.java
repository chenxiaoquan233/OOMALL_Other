package cn.edu.xmu.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.other.model.po.FootPrintPo;
import cn.edu.xmu.other.model.vo.FootPrintVo;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hardworking-qf
 * @version 创建时间：2020/12/2 下午3:48
 */
@Data
public class FootPrintBo implements VoObject {
    private Long id;
    private Long customerId;
    private Long goodSkuId;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public FootPrintBo(){}

    public FootPrintBo(FootPrintPo footPrintPo){
        this.id=footPrintPo.getId();
        this.customerId=footPrintPo.getCustomerId();
        this.goodSkuId=footPrintPo.getGoodsSpuId();
        this.gmtCreate=footPrintPo.getGmtCreate();
        this.gmtModified=footPrintPo.getGmtModified();
    }
    @Override
    public Object createVo() {
        FootPrintVo footPrintVo = new FootPrintVo();
        footPrintVo.setGoodSkuID(this.goodSkuId);
        return footPrintVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
