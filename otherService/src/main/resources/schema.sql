-- MySQL dump 10.13  Distrib 5.7.21, for Win64 (x86_64)
--
-- Host: localhost    Database: oomall_other
-- ------------------------------------------------------
-- Server version	5.7.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `customer_id` bigint(20) DEFAULT NULL,
                           `region_id` bigint(20) DEFAULT NULL,
                           `detail` varchar(500) DEFAULT NULL,
                           `consignee` varchar(64) DEFAULT NULL,
                           `mobile` varchar(128) DEFAULT NULL,
                           `be_default` tinyint(4) DEFAULT NULL,
                           `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `gmt_modified` datetime DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `advertisement`
--

DROP TABLE IF EXISTS `advertisement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `advertisement` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `seg_id` bigint(20) DEFAULT NULL,
                                 `link` varchar(255) DEFAULT NULL,
                                 `content` varchar(500) DEFAULT NULL,
                                 `image_url` varchar(255) DEFAULT NULL,
                                 `state` tinyint(4) DEFAULT NULL,
                                 `weight` int(11) DEFAULT NULL,
                                 `begin_date` datetime DEFAULT NULL,
                                 `end_date` datetime DEFAULT NULL,
                                 `repeat` tinyint(4) DEFAULT NULL,
                                 `message` varchar(500) DEFAULT NULL,
                                 `be_default` tinyint(4) DEFAULT NULL,
                                 `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `gmt_modified` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `aftersale_service`
--

DROP TABLE IF EXISTS `aftersale_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aftersale_service` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `order_item_id` bigint(20) DEFAULT NULL,
                                     `customer_id` bigint(20) DEFAULT NULL,
                                     `shop_id` bigint(20) DEFAULT NULL,
                                     `service_sn` varchar(128) DEFAULT NULL,
                                     `type` tinyint(4) DEFAULT NULL,
                                     `reason` varchar(500) DEFAULT NULL,
                                     `conclusion` varchar(500) DEFAULT NULL,
                                     `refund` bigint(10) DEFAULT NULL,
                                     `quantity` int(11) DEFAULT NULL,
                                     `region_id` bigint(20) DEFAULT NULL,
                                     `detail` varchar(500) DEFAULT NULL,
                                     `consignee` varchar(64) DEFAULT NULL,
                                     `mobile` varchar(128) DEFAULT NULL,
                                     `customer_log_sn` varchar(128) DEFAULT NULL,
                                     `shop_log_sn` varchar(128) DEFAULT NULL,
                                     `state` tinyint(4) DEFAULT NULL,
                                     `be_deleted` tinyint(4) DEFAULT NULL,
                                     `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     `gmt_modified` datetime DEFAULT NULL,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `be_share`
--

DROP TABLE IF EXISTS `be_share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `be_share` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `goods_spu_id` bigint(20) DEFAULT NULL,
                            `sharer_id` bigint(20) DEFAULT NULL,
                            `share_id` bigint(20) DEFAULT NULL,
                            `customer_id` bigint(20) DEFAULT NULL,
                            `order_item_id` bigint(20) DEFAULT NULL,
                            `rebate` int(11) DEFAULT NULL,
                            `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified` datetime DEFAULT NULL,
                            `share_activity_id` bigint(20) DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49147 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `user_name` varchar(32) DEFAULT NULL,
                            `password` varchar(128) DEFAULT NULL,
                            `real_name` varchar(32) DEFAULT NULL,
                            `gender` tinyint(4) DEFAULT NULL,
                            `birthday` datetime DEFAULT NULL,
                            `point` int(11) DEFAULT NULL,
                            `state` tinyint(4) DEFAULT NULL,
                            `email` varchar(128) DEFAULT NULL,
                            `mobile` varchar(128) DEFAULT NULL,
                            `be_deleted` tinyint(4) DEFAULT NULL,
                            `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `gmt_modified` datetime DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17330 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `favourite_goods`
--

DROP TABLE IF EXISTS `favourite_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `favourite_goods` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `customer_id` bigint(20) DEFAULT NULL,
                                   `goods_spu_id` bigint(20) DEFAULT NULL,
                                   `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `gmt_modified` datetime DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=589807 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `foot_print`
--

DROP TABLE IF EXISTS `foot_print`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `foot_print` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `customer_id` bigint(20) DEFAULT NULL,
                              `goods_spu_id` bigint(20) DEFAULT NULL,
                              `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              `gmt_modified` datetime DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32768 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `region` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `pid` bigint(20) DEFAULT NULL,
                          `name` varchar(64) DEFAULT NULL,
                          `postal_code` bigint(20) DEFAULT NULL,
                          `state` tinyint(4) DEFAULT NULL,
                          `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `gmt_modified` datetime DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `share`
--

DROP TABLE IF EXISTS `share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `share` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `sharer_id` bigint(20) DEFAULT NULL,
                         `goods_spu_id` bigint(20) DEFAULT NULL,
                         `quantity` int(11) DEFAULT NULL,
                         `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         `gmt_modified` datetime DEFAULT NULL,
                         `share_activity_id` bigint(20) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49147 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `share_activity`
--

DROP TABLE IF EXISTS `share_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `share_activity` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `shop_id` bigint(20) DEFAULT NULL,
                                  `goods_spu_id` bigint(20) DEFAULT NULL,
                                  `begin_time` datetime DEFAULT NULL,
                                  `end_time` datetime DEFAULT NULL,
                                  `strategy` varchar(500) DEFAULT NULL,
                                  `be_deleted` tinyint(4) DEFAULT NULL,
                                  `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `gmt_modified` datetime DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shopping_cart`
--

DROP TABLE IF EXISTS `shopping_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shopping_cart` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `customer_id` bigint(20) DEFAULT NULL,
                                 `goods_sku_id` bigint(20) DEFAULT NULL,
                                 `quantity` int(11) DEFAULT NULL,
                                 `price` bigint(10) DEFAULT NULL,
                                 `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `gmt_modified` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `time_segment`
--

DROP TABLE IF EXISTS `time_segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `time_segment` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                `begin_time` datetime DEFAULT NULL,
                                `end_time` datetime DEFAULT NULL,
                                `type` tinyint(4) DEFAULT NULL,
                                `gmt_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `gmt_modified` datetime DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-11-24 19:14:34