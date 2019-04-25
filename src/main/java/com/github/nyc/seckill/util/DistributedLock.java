package com.github.nyc.seckill.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

public class DistributedLock {
	
	    public static Logger log = LoggerFactory.getLogger(DistributedLock.class);
	    private InterProcessMutex interProcessMutex;  //可重入排它锁
	    private String lockName;  //竞争资源标志
	    private String root = "/distributed/lock/";//根节点
	    private static CuratorFramework curatorFramework;
	    private static String ZK_URL = "172.16.2.149:2181,172.16.2.71:2181,172.16.2.72:2181";
	   //private static String ZK_URL ="172.16.2.141:2181";
	    static{
	        curatorFramework= CuratorFrameworkFactory.newClient(ZK_URL,new ExponentialBackoffRetry(1000,3));
	        curatorFramework.start();
	        log.info("zk.curator start success");
	    }
	 
	    /**
	           * 实例化
	     * @param lockName
	     */
	    public DistributedLock(String lockName){
	        try {
	            this.lockName = lockName;
	            interProcessMutex = new InterProcessMutex(curatorFramework, root + lockName);
	        }catch (Exception e){
	            log.error("initial InterProcessMutex exception="+e);
	        }
	    }
	 
	    /**
	               * 获取锁
	     */
	    public void acquireLock(){
	        int flag = 0;
	        try {
	            //重试2次，每次最大等待2s，也就是最大等待4s
	            while (!interProcessMutex.acquire(2, TimeUnit.SECONDS)){
	                flag++;
	                if(flag>1){  //重试两次
	                    break;
	                }
	            }
	        } catch (Exception e) {
	           log.error("distributed lock acquire exception="+e);
	        }
	         if(flag>1){
	              log.info("Thread:"+Thread.currentThread().getId()+" acquire distributed lock  busy");
	         }else{
	             log.info("Thread:"+Thread.currentThread().getId()+" acquire distributed lock  success");
	         }
	    }
	    

	 
	    /**
	            * 释放锁
	     */
	    public void releaseLock(){
	        try {
	        if(interProcessMutex != null && interProcessMutex.isAcquiredInThisProcess()){
	            interProcessMutex.release();
	            curatorFramework.delete().inBackground().forPath(root+lockName);
	            log.info("Thread:"+Thread.currentThread().getId()+" release distributed lock  success");
	        }
	        }catch (Exception e){
	            log.info("Thread:"+Thread.currentThread().getId()+" release distributed lock  exception="+e);
	        }
	    }
	    
	    
	    /**
	     * 
	     * <p>经测试可以满足分布式锁 </p>  
	     * @return
	     * @author nieyc 
	     * @date 2019年4月25日
	     */
	    public boolean lock(){
	    	boolean flag=false;
	    	try {
	    		flag= interProcessMutex.acquire(5, TimeUnit.SECONDS);
			} catch (Exception e) {
				 log.error("distributed lock acquire exception="+e);
			}
	    	return flag;
	    }
	    
	    

}
