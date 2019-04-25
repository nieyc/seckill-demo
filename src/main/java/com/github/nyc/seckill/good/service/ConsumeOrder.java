package com.github.nyc.seckill.good.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.github.nyc.seckill.good.domain.Order;


@Component
public class ConsumeOrder implements CommandLineRunner {

	private Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private GoodService goodService;
	
	private volatile boolean isRunning=true;
	
	private AtomicInteger counter=new AtomicInteger(0);
	
	/**100 个线程同时消费队列中的数据，控制住数据库并发 ，这个值应该根据数据库压力来确定，一般数据库抗住3000应该没问题，集群环境下合理分配每个节点的并发量**/
	ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 100, 10, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(200), new ThreadPoolExecutor.DiscardOldestPolicy());
	
	@Autowired
	private QueueService queueService;
	 
	@Override
	public void run(String... args) throws Exception {
		 logger.info("启动初始化加载");
		 while(isRunning) {
			 while (queueService.checkQueueIsEmplty()==false) {
						executor.execute(new Runnable() {
							@Override
							public void run() {
								Order order=queueService.get();
								logger.debug("threadName:"+Thread.currentThread().getName());
								if(order!=null) {
								   seckillGood(order.getGoodId(), order.getUserId());
								   counter.incrementAndGet();
								   logger.debug("counter:"+counter);
								}else {
									logger.debug("队列中已经没有数据，程序进入等待状态。。。");
								}
							}
						});
				}
			 Thread.sleep(1);//降低cpu消耗
		 }
	}
	
	
	public void seckillGood(int goodId,int userId) {
		logger.debug("goodId:"+goodId+"----------userId"+userId);
		goodService.seckillGoodByZk(goodId);
		//goodService.seckillGoodByDbLock(goodId);
		//goodService.seckillGoodByOptimisticLock(goodId);
	}
	
	@Deprecated
	public boolean isEmptyGood() {
		if(counter.get()>=1000) {
			return true;
		}else {
			return false;
		}
		
	}
	
	@Deprecated
	public void stop() {
	   isRunning = false;
	   System.out.println("商品抢购完毕，退出程序");
     
	}
}
