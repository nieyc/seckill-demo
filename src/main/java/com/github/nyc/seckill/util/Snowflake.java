package com.github.nyc.seckill.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Snowflake {
	
	//自增标识
	private long sequence = 0L;
	//机器标识
	private final long workerId;
	//上次执行时间
	private long lastTimestamp = -1L;
	
	
	//时间戳基数
	private final static long TWEPOCH = 1000000000L;
	//毫秒内自增位数
	private final static long SEQUENCE_BITS = 14L;
	
	//自增最大值
	private final static long MAX_SEQUENCE = -1L ^ -1L << SEQUENCE_BITS;
	
	//机器标识位数
	private final static long WORKER_ID_BITS = 6L;
	//机器标识最大值
	public final static long MAX_WORKER_ID = -1L ^ -1L << WORKER_ID_BITS;
	
	//机器标识左移位数
	private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
	//时间左移位数
	private final static long TIME_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
	
	
	@Autowired
	public Snowflake(@Value("${snowflake.workerId}") long workerId) {
		if (workerId > MAX_WORKER_ID || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
		}
		this.workerId = workerId;
	}

	public synchronized long nextId() {
		long timestamp = this.timeGen();
		if (this.lastTimestamp == timestamp) {
			this.sequence = (this.sequence + 1) & MAX_SEQUENCE;
			if (this.sequence == 0) {
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else {
			this.sequence = MAX_SEQUENCE;
		}
		if (timestamp < this.lastTimestamp) {
			try {
				throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.lastTimestamp = timestamp;
		
		return ((timestamp - TWEPOCH << TIME_SHIFT)) | (this.workerId << WORKER_ID_SHIFT) | (this.sequence);
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}

	public static long getTime(long id) {
		return (id >> TIME_SHIFT)  + TWEPOCH;
	}
	
	public static long getWorkerId(long id) {
		return (id >> WORKER_ID_SHIFT) & MAX_WORKER_ID;
	}
	
	public static long getSequence(long id) {
		return id & MAX_SEQUENCE;
	}
	
	
  
}
