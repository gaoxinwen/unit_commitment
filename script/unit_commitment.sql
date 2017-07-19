/*
Navicat MySQL Data Transfer

Source Server         : MySQLDATA
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : unit_commitment

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2017-02-17 10:00:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for unit
-- ----------------------------
DROP TABLE IF EXISTS `unit`;
CREATE TABLE `unit` (
  `id` int(11) NOT NULL,
  `P_min` double NOT NULL,
  `P_max` double NOT NULL,
  `Min_up_time` double NOT NULL,
  `Min_down_time` double NOT NULL,
  `Hot_start_cost` double NOT NULL,
  `Cold_start_cost` double NOT NULL,
  `Cold_start_hour` int(11) NOT NULL,
  `Initial_status` int(11) NOT NULL,
  `a` double NOT NULL,
  `b` double NOT NULL,
  `c` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of unit
-- ----------------------------
INSERT INTO `unit` VALUES ('1', '150', '455', '8', '8', '4500', '9000', '5', '8', '1000', '16.19', '0.00048');
INSERT INTO `unit` VALUES ('2', '150', '455', '8', '8', '5000', '10000', '5', '8', '970', '17.26', '0.00031');
INSERT INTO `unit` VALUES ('3', '20', '130', '5', '5', '550', '1100', '4', '-5', '700', '16.6', '0.002');
INSERT INTO `unit` VALUES ('4', '20', '130', '5', '5', '560', '1120', '4', '-5', '680', '16.5', '0.00211');
INSERT INTO `unit` VALUES ('5', '25', '162', '6', '6', '900', '1800', '4', '-6', '450', '19.7', '0.00398');
INSERT INTO `unit` VALUES ('6', '20', '80', '3', '3', '170', '340', '2', '-3', '370', '22.26', '0.00712');
INSERT INTO `unit` VALUES ('7', '25', '85', '3', '3', '260', '520', '2', '-3', '480', '27.74', '0.00079');
INSERT INTO `unit` VALUES ('8', '10', '55', '1', '1', '30', '60', '0', '-1', '660', '25.92', '0.00413');
INSERT INTO `unit` VALUES ('9', '10', '55', '1', '1', '30', '60', '0', '-1', '665', '27.27', '0.00222');
INSERT INTO `unit` VALUES ('10', '10', '55', '1', '1', '30', '60', '0', '-1', '670', '27.79', '0.00173');
