package com.github.nyc.seckill.good.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.nyc.seckill.good.domain.Order;

@Mapper
public interface OrderDao {
	public List<Order> getOrderList();

    public Order getOrderById(String id);

    public void addOrder(Order order);

    public void updateOrderById(Order order);

    public void deleteOrderById(String id);

    public int  getOrderCount();
}
