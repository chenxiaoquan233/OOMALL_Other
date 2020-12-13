package cn.edu.xmu.oomall.other.service.factory;

import lombok.Data;

import java.util.List;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/13 下午3:28
 * 4
 */
@Data
public class ShareActivityStrategy {
    Integer[] num;
    Integer[] rate;
    boolean validate(){
        if(num==null||rate==null)return false;
        if(num.length!=rate.length)return false;
        if(num[0]!=0)return false;
        for(int i=0;i<num.length;i++){
            if(i!=0){
                if(num[i]<=num[i-1])return false;
                if(rate[i]<0)return false;
            }
        }
        return true;
    }
}
