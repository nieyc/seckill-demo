package com.github.nyc.seckill.good.service;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.stereotype.Service;
import com.github.nyc.seckill.good.domain.Order;

@Service
public class QueueService {
	
	private static ConcurrentLinkedQueue<Order> queue = new ConcurrentLinkedQueue<>();
	
	
	
	public void addOrder(Order order) {
		queue.add(order);
	}
	
	public Order get() {
	       return queue.poll();
	}
	
	public boolean checkQueueIsEmplty() {
		return queue.isEmpty();
	}
	
	public Object[] getQueue() {
		return queue.toArray();
	}

}
