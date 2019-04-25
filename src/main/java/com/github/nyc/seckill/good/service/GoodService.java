package com.github.nyc.seckill.good.service;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.nyc.seckill.good.dao.GoodDao;
import com.github.nyc.seckill.good.dao.OrderDao;
import com.github.nyc.seckill.good.domain.Good;
import com.github.nyc.seckill.good.domain.Order;
import com.github.nyc.seckill.util.DistributedLock;
import com.github.nyc.seckill.util.RedisUtil;
import com.github.nyc.seckill.util.Snowflake;

@Service
public class GoodService {
	
	 @Resource
	 private GoodDao goodDao;
	 
	 @Resource
	 private OrderDao orderDao;
	 
	 @Resource
	 private Snowflake snowflake;
	 
	  @Autowired
	  RedisUtil redisUtil;
	  
	  
	 private static final int TIMEOUT = 10*1000;//超时时间 2s
	 
	 DistributedLock dlock=new DistributedLock("zktest");
	 
	 private Logger logger = LogManager.getLogger(getClass());
	 
	 
		public List<Good> getGoodList() {
	       return  goodDao.getGoodList();
	    }

	    public  Good getGoodById(int id) {
	        return  goodDao.getGoodById(id);
	    }

	    public void addGood(Good good) {
	    	goodDao.addGood(good);
	    }

	    public void updateGood(Good good) {
	    	goodDao.updateGoodById(good);
	    }

	    public int  getGoodCount() {
	        return  goodDao.getGoodCount();
	    }
	    
	    
	    /**
	       zk实现的分布式锁
	     * <p> </p>  
	     * @param id
	     * @author nieyc 
	     * @date 2019年4月19日
	     */
	    public void seckillGoodByZk(int id) {
			   if( dlock.lock()) {
				   Good good=goodDao.getGoodById(id);
			    	if(good.getGoodNum()>0) {
			    		goodDao.updateGoodById(good);
			    	}else {
			    		logger.debug("good is less than 0");
			    	}
			    	dlock.releaseLock();
			   }else {
				   logger.debug("获取锁失败");
			   }
		}
	    
	    
	    /**
	     * <p>数据库悲观锁实现分布式锁，悲观锁高并发下会影响数据库性能，但是能保证先从队列取出的用户先抢到商品 </p>  
	     * @param id
	     * @author nieyc 
	     * @date 2019年4月22日
	     */
	    @Transactional(rollbackFor = RuntimeException.class)
	    public void seckillGoodByDbLock(int id) {
    		Good good=goodDao.getGoodByIdByLock(id);
	    	if(good.getGoodNum()>0) {
	    		 goodDao.updateGoodById(good);
	    		 Order order=new Order();
				 order.setOrderId(Long.toString(snowflake.nextId()));
				 order.setGoodId(id);
				 order.setOrderStatus(0);
				 order.setUserId(99);
				 order.setCreateTime(new Date());
				 orderDao.addOrder(order);
	    	}
	    	 redisUtil.hset("good", String.valueOf(id), good);
         }
	    
	    
	    /**
	     * 
	     * <p>数据库乐观锁实现，不能保证按照队列中取到的先后顺序抢到商品 </p>  
	     * @param id
	     * @author nieyc 
	     * @date 2019年4月22日
	     */
	    @Transactional(rollbackFor = RuntimeException.class)
	    public void seckillGoodByOptimisticLock(int id) {
	    	int i=0;
	    	Good good=goodDao.getGoodById(id);
	    	if(good.getGoodNum()>0) {
	    		i=goodDao.updateGoodByVersion(good);
	    		 if(i==0) {
	    			 logger.debug("更新记录失败。。。。。。。。。。。。。。。。。。。。。。。。。。。");
				 }else {
					 Order order=new Order();
					 order.setOrderId(Long.toString(snowflake.nextId()));
					 order.setGoodId(id);
					 order.setOrderStatus(0);
					 order.setUserId(99);
					 order.setCreateTime(new Date());
					 orderDao.addOrder(order);
					 redisUtil.hset("good", String.valueOf(id), good);
				 }
	    	}
	    }
	    

}
