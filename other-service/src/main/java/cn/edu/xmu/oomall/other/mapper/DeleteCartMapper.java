package cn.edu.xmu.oomall.other.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author JiangXIao
 **/
public interface DeleteCartMapper {
     int deleteCart(@Param("customerId") Long customerId, @Param("skuIdList")List<Long> skuIdList);
}
