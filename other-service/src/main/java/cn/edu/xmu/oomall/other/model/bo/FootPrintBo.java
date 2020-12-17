package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.oomall.other.model.po.FootPrintPo;
import cn.edu.xmu.oomall.other.model.vo.FootPrint.FootPrintRetVo;
import cn.edu.xmu.oomall.other.model.vo.FootPrint.FootPrintVo;
import cn.edu.xmu.oomall.other.model.vo.GoodsModule.GoodsSkuSimpleVo;
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
    private Long skuId;
    private GoodsSkuSimpleVo skuSimpleVo;
    private LocalDateTime gmtCreate;
    public FootPrintBo(){}
    public FootPrintBo(FootPrintPo po)
    {
        this.setId(po.getId());
        this.setSkuId(po.getGoodsSkuId());
        this.setCustomerId(po.getCustomerId());
        this.setGmtCreate(po.getGmtCreate());
    }
    @Override
    public Object createVo() {
        FootPrintRetVo footPrintRetVo = new FootPrintRetVo();
        footPrintRetVo.setId(this.getId());
        footPrintRetVo.setGoodsRetVo(this.getSkuSimpleVo());
        footPrintRetVo.setGmtCreate("2020-12-17 12:13:14");
        return footPrintRetVo;

    }

    @Override
    public Object createSimpleVo() {
        return null;
    }


}
