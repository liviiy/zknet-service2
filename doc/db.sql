CREATE TABLE `zknet_exchange` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ex_no` varchar(64) DEFAULT NULL COMMENT 'exchange no',
  `api_key` varchar(255) NOT NULL DEFAULT '',
  `address` varchar(1024) NOT NULL DEFAULT '',
  `name` varchar(255) DEFAULT NULL COMMENT 'name',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_ex_no` (`ex_no`) USING BTREE,
  UNIQUE KEY `idx_a_k` (`api_key`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT 'the exchange';

CREATE TABLE `zknet_event` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `ex_no` varchar(64) DEFAULT NULL COMMENT 'exchange number',
   `type` varchar(10) NOT NULL COMMENT 'interaction mode',
   `mode` varchar(16) NOT NULL COMMENT 'operation mode',
   `tx_status` varchar(16) NOT NULL COMMENT 'transaction status',
   `tx_sign` text NOT NULL COMMENT 'transaction signature',
   `tx_hash` varchar(70) NOT NULL COMMENT 'transaction hash',
   `confirm_hash` varchar(70) DEFAULT NULL COMMENT 'confirm tx_hash',
   `error` text COMMENT 'fail info',
   `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   KEY `idx_t_h` (`tx_hash`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT 'event';

CREATE TABLE `zknet_business_contract` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `contract_address` varchar(128) NOT NULL COMMENT 'contract address',
   `platform` varchar(32) DEFAULT NULL COMMENT 'chain',
   `chain_id` bigint(50) DEFAULT NULL COMMENT 'chain id',
   `owner` varchar(255) DEFAULT NULL COMMENT 'the contract owner',
   `ex_no` varchar(64) DEFAULT NULL COMMENT 'exchange no',
   `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `zknet_contract_log_event_handle_progress` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `topic` varchar(128) NOT NULL COMMENT 'the topic to be listened',
    `platform` varchar(32) DEFAULT NULL COMMENT 'chain',
    `complete_block_no` bigint(20) DEFAULT '0' COMMENT 'latest handled block no',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_t_p` (`topic`,`platform`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='the contract log event handle progress';

CREATE TABLE `zknet_contract_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tx_hash` varchar(128) NOT NULL COMMENT 'transaction hash',
  `contract_address` varchar(70) NOT NULL COMMENT 'the contract address where the event happened',
  `block_no` bigint(20) DEFAULT '0' COMMENT 'current block no',
  `data` varchar(256) DEFAULT NULL COMMENT 'the event data',
  `topic` varchar(70) DEFAULT NULL COMMENT 'topic',
  `from` varchar(70) DEFAULT NULL COMMENT 'from',
  `to` varchar(70) DEFAULT NULL COMMENT 'to',
  `token_addr` varchar(70) DEFAULT NULL COMMENT 'token address',
  `platform` varchar(32) DEFAULT NULL COMMENT 'chain',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='the contract event log';

CREATE TABLE `zknet_ex_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ex_no` varchar(64) NOT NULL COMMENT 'exchange no',
  `user_addr` varchar(70) NOT NULL COMMENT 'order user address',
  `order_no` varchar(70) NOT NULL COMMENT 'order no',
  `order_type` varchar(10) NOT NULL COMMENT 'order type:market price、fixed price',
  `position_id` int(10) DEFAULT '0' COMMENT 'position id',
  `timestamp` int(10) DEFAULT '0' COMMENT 'order timestamp',
  `token_buy` varchar(10) NOT NULL COMMENT 'token name',
  `amount_buy` bigint(20) DEFAULT '0' COMMENT 'amount buy',
  `token_sell` varchar(10) NOT NULL COMMENT 'token name',
  `amount_sell` bigint(20) DEFAULT '0' COMMENT 'amount sell',
  `fee` bigint(20) DEFAULT '0' COMMENT 'fee',
  `token_fee` varchar(10) NOT NULL COMMENT 'the token name to pay the fee',
  `extend` varchar(256) DEFAULT '' COMMENT 'extend json data',
  `status` varchar(10) NOT NULL COMMENT 'order status:FINISHED、UNFINISHED',
  `sign` text NOT NULL COMMENT 'the sign tx',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_e_n_o_n` (`ex_no`,`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='the order info';

CREATE TABLE `zknet_ex_settlement` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `ex_no` varchar(64) NOT NULL COMMENT 'exchange no',
   `st_no` varchar(70) NOT NULL COMMENT 'settlement no',
   `order_a_no` varchar(70) NOT NULL COMMENT 'order A no',
   `order_b_no` varchar(70) NOT NULL COMMENT 'order B no',
   `order_a_sold` bigint(20) DEFAULT '0' COMMENT 'order A sold amount',
   `order_b_sold` bigint(20) DEFAULT '0' COMMENT 'order B sold amount',
   `order_a_fee` bigint(20) DEFAULT '0' COMMENT 'order A fee amount',
   `order_b_fee` bigint(20) DEFAULT '0' COMMENT 'order B fee amount',
   `tx_hash` varchar(70) DEFAULT NULL COMMENT 'the transaction hash',
   `status` varchar(10) NOT NULL COMMENT 'settlement stats:PENDING、SUCCESS、FAIL',
   `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   UNIQUE KEY `idx_e_n_st_n` (`ex_no`,`st_no`) USING BTREE,
   UNIQUE KEY `idx_t_h` (`tx_hash`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='the settlement info';

CREATE TABLE `zknet_l2_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(16) NOT NULL COMMENT 'token name',
  `full_name` varchar(64) DEFAULT NULL COMMENT 'full name',
  `contract_address` varchar(128) NOT NULL COMMENT 'token contract address',
  `type` varchar(16) NOT NULL COMMENT 'token type',
  `decimal` int(10) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
