/*
SQLyog Community v12.2.5 (64 bit)
MySQL - 10.1.16-MariaDB : Database - 3patti_database
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`3patti_database` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `3patti_database`;

/*Table structure for table `gt_game_card_rank` */

DROP TABLE IF EXISTS `gt_game_card_rank`;

CREATE TABLE `gt_game_card_rank` (
  `id` int(11) NOT NULL,
  `rank_card` varchar(20) DEFAULT NULL,
  `ranking` varchar(5) DEFAULT NULL,
  `game_varient` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `gt_game_card_rank` */

/*Table structure for table `gt_game_config` */

DROP TABLE IF EXISTS `gt_game_config`;

CREATE TABLE `gt_game_config` (
  `game_config_id` int(11) NOT NULL AUTO_INCREMENT,
  `game_variant` varchar(20) NOT NULL,
  `min_players` int(11) NOT NULL,
  `max_players` int(11) NOT NULL,
  `min_room` int(11) DEFAULT '2',
  `max_room` int(11) DEFAULT '5',
  `status` varchar(20) NOT NULL,
  `modified_time` datetime DEFAULT NULL,
  `chip_type` varchar(10) DEFAULT 'Dummy',
  PRIMARY KEY (`game_config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `gt_game_config` */

insert  into `gt_game_config`(`game_config_id`,`game_variant`,`min_players`,`max_players`,`min_room`,`max_room`,`status`,`modified_time`,`chip_type`) values 
(1,'simple',2,7,2,10,'created','2016-10-13 10:34:47','Dummy'),
(2,'muflis',2,7,3,5,'created','2016-10-13 10:36:50','Dummy'),
(3,'ak47',2,7,4,5,'created','2016-10-13 10:38:10','Dummy');

/*Table structure for table `gt_game_prop` */

DROP TABLE IF EXISTS `gt_game_prop`;

CREATE TABLE `gt_game_prop` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `game_config_id` int(11) NOT NULL,
  `prop_name` varchar(20) DEFAULT NULL,
  `prop_value` varchar(10) DEFAULT NULL,
  `prop_type` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `gt_game_prop` */

insert  into `gt_game_prop`(`id`,`game_config_id`,`prop_name`,`prop_value`,`prop_type`) values 
(1,1,'turn_time','30','time'),
(2,1,'disconnection_time','100','time'),
(3,1,'deck','-1','Card'),
(4,1,'min_bet','10','Rs.'),
(5,1,'min_players','2','number');

/*Table structure for table `gt_game_table` */

DROP TABLE IF EXISTS `gt_game_table`;

CREATE TABLE `gt_game_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `game_config_id` int(11) NOT NULL,
  `table_name` varchar(25) DEFAULT NULL,
  `table_status` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=latin1;

/*Data for the table `gt_game_table` */

insert  into `gt_game_table`(`id`,`game_config_id`,`table_name`,`table_status`) values 
(33,1,'simple#7#33','created'),
(34,1,'simple#7#34','created'),
(35,2,'muflis#7#35','created'),
(36,2,'muflis#7#36','created'),
(37,2,'muflis#7#37','created'),
(38,3,'ak47#7#38','created'),
(39,3,'ak47#7#39','created'),
(40,3,'ak47#7#40','created'),
(41,3,'ak47#7#41','created');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
