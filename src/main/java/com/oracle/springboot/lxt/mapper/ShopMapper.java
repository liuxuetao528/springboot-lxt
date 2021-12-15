package com.oracle.springboot.lxt.mapper;

import com.oracle.springboot.lxt.po.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Shop record);

    int insertSelective(Shop record);

    Shop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);

    List<Shop> getshoplist(@Param("shop") Shop shop, @Param(value = "min") int min, @Param(value = "max") int max);

    int updatestatus(Long id);
}