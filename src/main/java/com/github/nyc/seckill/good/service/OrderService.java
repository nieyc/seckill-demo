package com.github.nyc.seckill.good.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.github.nyc.seckill.good.dao.OrderDao;
import com.github.nyc.seckill.good.domain.Order;

@Service
public class OrderService {
	 @Resource
	 private OrderDao orderDao;
	 
		public List<Order> getOrderList() {
	       return  orderDao.getOrderList();
	    }

	    public  Order getOrderById(String id) {
	        return  orderDao.getOrderById(id);
	    }

	    public void addOrder(Order mallOrder) {
	    	orderDao.addOrder(mallOrder);
	    }
	    public void updateOrder(Order mallOrder) {
	    	orderDao.updateOrderById(mallOrder);
	    }

	    public void deleteOrderById(String id) {
	    	orderDao.deleteOrderById(id);
	    }

	    public int  getMallOrderCount() {
	        return  orderDao.getOrderCount();
	    }
	 
	 
	 
}
