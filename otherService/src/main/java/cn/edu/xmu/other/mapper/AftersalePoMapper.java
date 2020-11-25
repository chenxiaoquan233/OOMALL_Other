package cn.edu.xmu.other.mapper;

import cn.edu.xmu.other.model.po.AftersalePo;
import cn.edu.xmu.other.model.po.AftersalePoExample;
import java.util.List;

public interface AftersalePoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    int insert(AftersalePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    int insertSelective(AftersalePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    List<AftersalePo> selectByExample(AftersalePoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    AftersalePo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(AftersalePo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table aftersale_service
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(AftersalePo record);
}