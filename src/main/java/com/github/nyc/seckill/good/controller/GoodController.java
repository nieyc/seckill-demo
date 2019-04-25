package com.github.nyc.seckill.good.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.nyc.seckill.good.domain.Good;
import com.github.nyc.seckill.good.domain.Order;
import com.github.nyc.seckill.good.service.GoodService;
import com.github.nyc.seckill.good.service.QueueService;
import com.github.nyc.seckill.util.RedisUtil;


@RestController
@RequestMapping("/good")
public class GoodController {
	
	@Autowired
	private QueueService queueService;
	
	@Autowired
	private GoodService goodService;
	
	  @Autowired
	  RedisUtil redisUtil;
	
	
	@RequestMapping("/accept")
    public Object acceptService(String goodId,String userId) {
		Good good=(Good)redisUtil.hget("good", goodId);
		JSONObject json=new JSONObject();
		if(good!=null) {
			//如果商品已经秒杀完毕，则直接返回客户端，请求不在进入队列
			if(good.getGoodNum()==0) {	
				json.put("retCode", "0");
				json.put("retMsg", "game over");
				return json;
			}
		}
		Order order=new Order();
		order.setUserId(Integer.valueOf(userId));
		order.setGoodId(Integer.valueOf(goodId));
		queueService.addOrder(order);
		json.put("retCode", "0");
		json.put("good", good);
		json.put("retMsg", "success");
		return json;
    }
	
	
	@RequestMapping("/accept1")
    public Object acceptService1(String goodId,String userId) {
		//如果商品已经秒杀完毕，则直接返回客户端，请求不在进入队列
		Order order=new Order();
		order.setUserId(Integer.valueOf(userId));
		order.setGoodId(Integer.valueOf(goodId));
		queueService.addOrder(order);
		return null;
    }
	
	
	@RequestMapping(value="/getGoodById",method=RequestMethod.POST)
    public Object getGoodById(String goodId) {
		Good good=null;
		if(redisUtil.hget("good", goodId)==null) {
			 good=goodService.getGoodById(Integer.valueOf(goodId));
			 redisUtil.hset("good", goodId, good);
		}else {
			good=(Good)redisUtil.hget("good",goodId);
		}
		return good;
    }
	
	
	@RequestMapping("/test")
    public Object test(String goodId) {
		goodService.seckillGoodByZk(1);
		return null;
    }
	
	
	

}
