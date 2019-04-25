package com.github.nyc.seckill.good.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.nyc.seckill.good.domain.Good;

@Mapper
public interface GoodDao {
	
	public List<Good> getGoodList();

    public Good getGoodById(int id);
    
    public Good getGoodByIdByLock(int id);

    public void addGood(Good good);

    public void updateGoodById(Good good);
    
    public int updateGoodByVersion(Good good);

    public int  getGoodCount();


}
