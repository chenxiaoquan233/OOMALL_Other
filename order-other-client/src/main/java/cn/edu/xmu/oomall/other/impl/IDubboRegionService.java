package cn.edu.xmu.oomall.other.impl;


public interface IDubboRegionService {
    /**
     * 获取该region的上级地区
     * @param regionId
     * @return
     */
    Long getSuperiorRegionId(Long regionId);
}
