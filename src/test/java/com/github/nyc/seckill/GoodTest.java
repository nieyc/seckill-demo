package com.github.nyc.seckill;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.github.nyc.seckill.good.service.GoodService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodTest {
	@Autowired
	GoodService goodService;
	
	@Test
	public void TestUpdateScore() throws InterruptedException {
		ThreadPoolExecutor executor1 = new ThreadPoolExecutor(8, 100, 10, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(200), new ThreadPoolExecutor.DiscardOldestPolicy());

		for (int i = 0; i < 200; i++) {
			executor1.execute(new Runnable() {
				@Override
				public void run() {
					goodService.seckillGoodByZk(1);
				}
			});
		}

		executor1.shutdown();
		Thread.sleep(1000000);
	}
	
	

}
