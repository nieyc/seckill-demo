package com.github.nyc.seckill.good.domain;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
* 描述：商品表模型
* @author nieyc
* @date 2019-04-19 15:12:14
*/

public class Good implements Serializable {

private static final long serialVersionUID = 1L;

        /**
        * 
        */
         private int id;
        /**
        * 
        */
         private String goodName;
        /**
        * 
        */
        private BigDecimal goodPrice;
        /**
        * 
        */
         private int goodNum;
         
       private int version;


        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public String getGoodName() {
            return this.goodName;
        }

        public void setGoodName(String goodName) {
            this.goodName = goodName;
        }


        public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public BigDecimal getGoodPrice() {
            return this.goodPrice;
        }

        public void setGoodPrice(BigDecimal goodPrice) {
            this.goodPrice = goodPrice;
        }

        public int getGoodNum() {
            return this.goodNum;
        }

        public void setGoodNum(int goodNum) {
            this.goodNum = goodNum;
        }





}