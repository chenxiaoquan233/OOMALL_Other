package cn.edu.xmu.oomall.other.service.factory;

import java.util.Arrays;
import java.util.List;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/13 下午3:27
 * 4 num按件数，1代表一件商品 rate按万分之一计算 1代表1/10000
 * 5 example: {"num":[0,10,50,100],"rate":[0,50,100,130]}
 */
//TODO:修改格式以适应需求
public class CalcPoint {
    ShareActivityStrategy strategy;
    public CalcPoint(ShareActivityStrategy s){
        strategy=s;
    }
    public Integer getPoint(Long price,Integer plusQuantity,Integer oldQuantity){
        oldQuantity*=100;
        plusQuantity*=100;
        Integer newQuantity=(oldQuantity+plusQuantity);
        if(newQuantity<=strategy.rule[0].num)return 0;
        Integer[] num= (Integer[]) Arrays.stream(strategy.rule).map(rule -> rule.num).toArray();
        Integer[] rate=(Integer[]) Arrays.stream(strategy.rule).map(rule -> rule.rate).toArray();
        int cnt=0;
        for(cnt=0;cnt<num.length;cnt++){
            if(cnt+1== num.length||num[cnt+1]>=newQuantity)break;
        }
        int tempQuantity=newQuantity;
        int point=0;
        while(cnt>=0||tempQuantity!=oldQuantity){
            point+=(tempQuantity-Math.max(num[cnt],oldQuantity))*price*rate[cnt]/10000;
            tempQuantity=Math.max(num[cnt],oldQuantity);
            cnt--;
        }
        return point;
    }
}
