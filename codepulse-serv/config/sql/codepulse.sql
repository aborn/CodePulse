DROP TABLE IF EXISTS `cp_user_coding_daily`;
CREATE TABLE `cp_user_coding_daily`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `uid`         varchar(128)  NOT NULL DEFAULT '' COMMENT '用户UID',
    `token`       varchar(128)  NOT NULL DEFAULT '' COMMENT '用户token',
    `day`         varchar(16)   NOT NULL DEFAULT '' COMMENT '数据日期,格式为：yyyyMMdd，如：20230210',
    `code_info`   varchar(2048) NOT NULL DEFAULT '' COMMENT '每日数据，bitset的字符串存储',
    `create_by`   varchar(255)  NOT NULL DEFAULT '' COMMENT '创建者',
    `update_by`   varchar(255)  NOT NULL DEFAULT '' COMMENT '更新者',
    `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_token_day` (`token`, `day`),
    KEY           `idx_uid`(`uid`),
    KEY           `idx_token`(`token`),
    KEY           `idx_create_time_index` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='日编程记录表';
