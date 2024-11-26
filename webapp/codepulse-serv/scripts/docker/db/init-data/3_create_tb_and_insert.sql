-- MySQL dump 10.13  Distrib 8.0.32, for macos13 (arm64)
--
-- Host: 127.0.0.1    Database: codepulse
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

USE `codepulse`;

--
-- Table structure for table `cp_user_coding_daily`
--
DROP TABLE IF EXISTS `cp_user_coding_daily`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cp_user_coding_daily`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `token`       varchar(128)  NOT NULL DEFAULT '' COMMENT '用户token',
    `day`         varchar(16)   NOT NULL DEFAULT '' COMMENT '数据日期,格式为：yyyyMMdd，如：20230210',
    `code_info`   varchar(2048) NOT NULL DEFAULT '' COMMENT '每日数据，bitset的字符串存储',
    `create_by`   varchar(255)  NOT NULL DEFAULT '' COMMENT '创建者',
    `update_by`   varchar(255)  NOT NULL DEFAULT '' COMMENT '更新者',
    `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `code_time`   int           NOT NULL DEFAULT '0' COMMENT '每日的编程时间，单位秒S',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_token_day` (`token`,`day`),
    KEY           `idx_token` (`token`),
    KEY           `idx_create_time_index` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='日编程记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cp_user_coding_daily`
--

LOCK
TABLES `cp_user_coding_daily` WRITE;
/*!40000 ALTER TABLE `cp_user_coding_daily` DISABLE KEYS */;
INSERT INTO `cp_user_coding_daily`
VALUES (1, '0xa2325852', '2023-02-10',
        ',,,,,,,,,,,,,,,,,,,,65536,,,,,,,,,,3461016313634226176,,,-9223319260296642560,65,1152921513733652480,1738684262730694656,34359738368',
        'auto', 'auto', '2023-02-10 18:06:31', '2023-02-11 11:21:42', 660),
       (7, '0xa2325852', '2023-02-11',
        ',,,,,,,,,,,,,,,,,,,,,,,,274877906944,,,4553139223271571456,4611685727979931646,8766017806321,,-4222124650562432,1138578175270899',
        'auto', 'auto', '2023-02-11 13:07:38', '2023-02-13 11:00:54', 4200),
       (8, '0xa2325852', '2023-02-13',
        ',,,,,,,,,,,,,,,,,,,,2199023255552,3458448704876314624,16042423549222664,172032,,,-31560386037678080,29695,,-108121575428980736,-5478597136020930625,-1743033260668092420,2348767909607437695,108086391342104578,,1649267474432,6597090738176,,2048',
        'auto', 'auto', '2023-02-13 11:01:13', '2023-02-14 10:19:50', 7770),
       (9, '0xa2325852', '2023-02-14', ',,,,,,,,,,,,,,,,,,,8388608', 'auto', 'auto', '2023-02-14 10:20:00',
        '2023-02-15 17:03:36', 30),
       (10, '0xa2325852', '2023-02-15', ',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,17179869184', 'auto', 'auto',
        '2023-02-15 17:03:38', '2023-02-15 23:56:01', 30),
       (11, '0xa2325852', '2023-02-16', ',,,,,,,,,,,,,,,,,,134217728,,,,3072,,,,,,134217728', 'auto', 'auto',
        '2023-02-16 10:34:41', '2023-02-16 15:43:44', 120);
/*!40000 ALTER TABLE `cp_user_coding_daily` ENABLE KEYS */;
UNLOCK
TABLES;

DROP TABLE IF EXISTS `cp_user_info`;
CREATE TABLE `cp_user_info`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `token`       varchar(128) NOT NULL DEFAULT '' COMMENT '用户token',
    `openid`      varchar(256) NOT NULL DEFAULT '' COMMENT '第三方登录的openid，如github登录的id',
    `third_type`  int(11) NOT NULL DEFAULT '0' COMMENT '第三方登录类型，0-表示github',
    `avatar`      varchar(512) NOT NULL DEFAULT '' COMMENT '用户头像',
    `uid`         varchar(256) NOT NULL DEFAULT '' COMMENT '用户uid，如：aborn',
    `name`        varchar(256) NOT NULL DEFAULT '' COMMENT '用户名，如：Aborn Jiang',
    `team`        varchar(128) NOT NULL DEFAULT '' COMMENT '团队标识的t_key',
    `corp`        varchar(128) NOT NULL DEFAULT '' COMMENT '公司标识的t_key',
    `create_by`   varchar(255) NOT NULL DEFAULT '' COMMENT '创建者',
    `update_by`   varchar(255) NOT NULL DEFAULT '' COMMENT '更新者',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_openid_third_type` (`openid`,`third_type`),
    UNIQUE KEY `uniq_token` (`token`),
    /** KEY           `idx_token` (`token`),*/
    KEY           `idx_create_time_index` (`create_time`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

DROP TABLE IF EXISTS `cp_team_info`;
CREATE TABLE `cp_team_info`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `t_key`       varchar(128) NOT NULL DEFAULT '' COMMENT 'team的唯一标识',
    `name`        varchar(256) NOT NULL DEFAULT '' COMMENT '团队名',
    `owner_token` varchar(128) NOT NULL DEFAULT '' COMMENT '所有者，token',
    `type`        int(11) NOT NULL DEFAULT '0' COMMENT '0-公司，1-团队',
    `create_by`   varchar(255) NOT NULL DEFAULT '' COMMENT '创建者',
    `update_by`   varchar(255) NOT NULL DEFAULT '' COMMENT '更新者',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_tk` (`t_key`),
    KEY           `idx_create_time_index` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='团队信息表';

DROP TABLE IF EXISTS `cp_trending`;
CREATE TABLE `cp_trending`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `day`         varchar(16)   NOT NULL DEFAULT '' COMMENT '数据日期,格式为：yyyyMMdd，如：20230210',
    `t_key`       varchar(128)  NOT NULL DEFAULT '' COMMENT 'team的唯一标识',
    `trend_info`  varchar(2048) NOT NULL DEFAULT '' COMMENT '每日趋势，bitset的字符串存储',
    `create_by`   varchar(255)  NOT NULL DEFAULT '' COMMENT '创建者',
    `update_by`   varchar(255)  NOT NULL DEFAULT '' COMMENT '更新者',
    `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_day_tkey` (`day`, `t_key`),
    KEY           `idx_tkey` (`t_key`),
    KEY           `idx_create_time_index` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='编程趋势表';

--
-- Dumping routines for database 'codepulse'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-03 16:02:23
