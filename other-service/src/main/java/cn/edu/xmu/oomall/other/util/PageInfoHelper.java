package cn.edu.xmu.oomall.other.util;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author XQChen
 * @version 创建时间：2020/12/15 下午4:51
 */
public class PageInfoHelper {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class RetObject {
        Integer page;
        Integer pageSize;
        Long total;
        Integer pages;
        List list;
    }

    public static RetObject process(PageInfo pageInfo) {
        return new RetObject(
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.getTotal(),
                pageInfo.getPages(),
                pageInfo.getList());
    }
}
