package com.oracle.springboot.lxt.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.oracle.springboot.lxt.mapper.ShopMapper;
import com.oracle.springboot.lxt.po.Shop;
import com.oracle.springboot.lxt.service.ShopService;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService{

    @Resource
    private ShopMapper shopMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return shopMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Shop record) {
        return shopMapper.insert(record);
    }

    @Override
    public int insertSelective(Shop record) {
        return shopMapper.insertSelective(record);
    }

    @Override
    public Shop selectByPrimaryKey(Long id) {
        return shopMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Shop record) {
        return shopMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Shop record) {
        return shopMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Shop> getshoplist(Shop shop, int min, int max) {
        return shopMapper.getshoplist(shop,min,max);
    }

    @Override
    public int updatestatus(Long id) {
        return shopMapper.updatestatus(id);
    }

}
