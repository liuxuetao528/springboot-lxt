package com.oracle.springboot.lxt.service;

import com.oracle.springboot.lxt.po.Shop;

import java.util.List;

public interface ShopService{


    int deleteByPrimaryKey(Long id);

    int insert(Shop record);

    int insertSelective(Shop record);

    Shop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);

    List<Shop> getshoplist(Shop shop, int min, int max);

    int updatestatus(Long id);
}
