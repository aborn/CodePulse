DROP TABLE IF EXISTS `cp_user_coding_daily`;
CREATE TABLE `cp_user_coding_daily`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `token`       varchar(128)  NOT NULL DEFAULT '' COMMENT '用户token',
    `day`         varchar(16)   NOT NULL DEFAULT '' COMMENT '数据日期,格式为：yyyyMMdd，如：20230210',
    `code_time`   int           NOT NULL DEFAULT '0' COMMENT '每日的编程时间，单位秒S',
    `code_info`   varchar(2048) NOT NULL DEFAULT '' COMMENT '每日数据，bitset的字符串存储',
    `create_by`   varchar(255)  NOT NULL DEFAULT '' COMMENT '创建者',
    `update_by`   varchar(255)  NOT NULL DEFAULT '' COMMENT '更新者',
    `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_token_day` (`token`, `day`),
    KEY           `idx_token` (`token`),
    KEY           `idx_create_time_index` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='日编程记录表';


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
