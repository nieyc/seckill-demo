package com.github.nyc.seckill.good.domain;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable{
	
	private static final long serialVersionUID = 1L;

    /**
    * 
    */
     private String orderId;
    /**
    * 
    */
     private int userId;
    /**
    * 
    */
     private int goodId;
    /**
    * 
    */
    private Date createTime;
    /**
    * 
    */
     private int orderStatus;
    /**
    * 
    */
    private Date payTime;

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }






    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }





    public int getGoodId() {
        return this.goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }






    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }




    public int getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }






    public Date getPayTime() {
        return this.payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

}
