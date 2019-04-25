/*
Navicat MySQL Data Transfer

Source Server         : 公司142机器
Source Server Version : 50624
Source Host           : 172.16.2.142:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2019-04-25 15:21:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mall_good
-- ----------------------------
DROP TABLE IF EXISTS `mall_good`;
CREATE TABLE `mall_good` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `good_name` varchar(255) DEFAULT NULL,
  `good_price` decimal(10,2) DEFAULT NULL,
  `good_num` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mall_good
-- ----------------------------
INSERT INTO `mall_good` VALUES ('1', 'thinkPad', '8000.00', '200', '0');



SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mall_order
-- ----------------------------
DROP TABLE IF EXISTS `mall_order`;
CREATE TABLE `mall_order` (
  `order_id` varchar(255) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `good_id` int(11) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_status` int(11) DEFAULT NULL,
  `pay_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mall_order
-- ----------------------------