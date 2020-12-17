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
    public Integer firstOrAvg;
    public Rule[] rule;
    boolean validate(){
        if(rule==null)return false;
        for(int i=0;i<rule.length;i++){
            if(rule[i].num==null||rule[i].rate==null)return false;
            if(i!=0){

                if(rule[i].num<=rule[i-1].num)return false;
            }
            if(rule[i].num<0)return false;
            if(rule[i].rate<0)return false;
        }
        return true;
    }
}
