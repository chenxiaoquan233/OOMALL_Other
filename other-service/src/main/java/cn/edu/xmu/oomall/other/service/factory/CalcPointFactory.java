package cn.edu.xmu.oomall.other.service.factory;

import cn.edu.xmu.ooad.util.JacksonUtil;
import org.springframework.stereotype.Component;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/13 下午3:27
 * 4
 */
@Component
public class CalcPointFactory {
    public CalcPoint getInstance(String strategyStr){
        ShareActivityStrategy strategy=JacksonUtil.toObj(strategyStr,ShareActivityStrategy.class);
        return new CalcPoint(strategy);
    }
    static public boolean validateStrategy(String strategyStr){
        ShareActivityStrategy strategy;
        try{
            strategy=JacksonUtil.toObj(strategyStr,ShareActivityStrategy.class);
        }
        catch (Exception ex){
            return false;
        }
        return strategy.validate();
    }
}
