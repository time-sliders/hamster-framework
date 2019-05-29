create database zhw default character set utf8 collate utf8_general_ci;

create table zhw.user_info
(
  id           BIGINT UNSIGNED primary key auto_increment comment 'id',
  seq_no       varchar(32) DEFAULT null comment '序列号',
  name         varchar(32) DEFAULT null comment '用户名',
  gmt_create   DATETIME    DEFAULT null comment '创建时间',
  gmt_modified DATETIME    DEFAULT null comment '修改时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户信息';


INSERT INTO zhw.user_info (seq_no, name, gmt_create)
VALUES ('100001', '张伟', now());
INSERT INTO zhw.user_info (seq_no, name, gmt_create)
VALUES ('100002', '黄珊珊', now());