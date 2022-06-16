-- 初始化表
drop table if exists `user`, `role`, `role_permission`, `permission`, `public_key`, `article`, `anthology`;

-- 角色
create table `role`
(
	`id`           int unsigned auto_increment,
	`name`         varchar(32) not null,
	`gmt_created`  datetime,
	`gmt_modified` datetime,
	primary key (`id`)
) engine InnoDB
  default charset = utf8mb4;

-- 权限
create table `permission`
(
	`id`           int unsigned auto_increment,
	`name`         varchar(32) not null,
	`gmt_created`  datetime,
	`gmt_modified` datetime,
	primary key (`id`)
) engine InnoDB
  default charset = utf8mb4;

-- 角色对权限表
create table `role_permission`
(
	`role_id`       int unsigned,
	`permission_id` int unsigned,
	primary key (`role_id`, `permission_id`),
	foreign key (`role_id`) references `role` (`id`) on delete cascade on update cascade,
	foreign key (`permission_id`) references `permission` (`id`) on delete cascade on update cascade
) engine InnoDB
  default charset = utf8mb4;

-- 用户
create table `user`
(
	`id`           int unsigned auto_increment,
	`username`     varchar(16)   not null,
	`password`     varchar(64)   not null,
	`nickname`     varchar(32)   not null,
	`avatar`       varchar(1024) not null,
	`email`        varchar(64)   not null,
	`role_id`      int unsigned  not null,
	`gmt_created`  datetime,
	`gmt_modified` datetime,
	primary key (`id`),
	foreign key (`role_id`) references `role` (`id`) on delete cascade on update cascade
) engine InnoDB
  default charset = utf8mb4;

-- 公钥信息
create table `public_key`
(
	`id`           int unsigned auto_increment,
	`line`         int          not null,
	`user_id`      int unsigned not null,
	`gmt_created`  datetime,
	`gmt_modified` datetime,
	primary key (`id`),
	foreign key (`user_id`) references `user` (`id`)
) engine InnoDB
  default charset = utf8mb4;

-- 文集
create table `anthology`
(
	`id`               bigint        not null,
	`name`             varchar(64)   not null,
	`cover`            varchar(1024) not null,
	`repo_path`        varchar(2048) not null,
	`latest_commit_id` varchar(40)   not null,
	`gmt_created`      datetime,
	`gmt_modified`     datetime,
	primary key (`id`)
) engine InnoDB
  default charset = utf8mb4;

-- 文章索引信息
create table `article`
(
	`id`           bigint        not null,
	`file_path`    varchar(2048) not null,
	`anthology_id` bigint        not null,
	`gmt_created`  datetime,
	`gmt_modified` datetime,
	primary key (`id`),
	foreign key (`anthology_id`) references `anthology` (`id`) on delete cascade on update cascade
) engine InnoDB
  default charset = utf8mb4;

-- 初始化一些数据
insert into `role` (`name`)
values ('ROLE_ADMIN'), -- id为1，管理员
	   ('ROLE_MEMBER'); -- id为2，团队成员

insert into `permission`(`name`)
values ('edit_user'), -- id为1，增加或者编辑用户
	   ('browse_article'); -- id为2，浏览文章

insert into `role_permission`
values (1, 1),
	   (1, 2),
	   (2, 2);