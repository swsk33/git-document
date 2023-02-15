-- 初始化表
drop table if exists `user`, `setting`, `role`, `role_permission`, `permission`, `public_key`, `article`, `anthology`, `star`;

-- 角色
create table `role`
(
	`id`           int unsigned auto_increment,
	`name`         varchar(32) unique not null,
	`show_name`    varchar(32) unique not null,
	`gmt_created`  datetime,
	`gmt_modified` datetime,
	primary key (`id`)
) engine InnoDB
  default charset = utf8mb4;

-- 权限
create table `permission`
(
	`id`           int unsigned auto_increment,
	`name`         varchar(32) unique not null,
	`show_name`    varchar(32) unique not null,
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

-- 用户偏好设置
create table `setting`
(
	`id`                   int unsigned auto_increment,
	`receive_update_email` bool,
	`gmt_created`          datetime,
	`gmt_modified`         datetime,
	primary key (`id`)
) engine InnoDB
  default charset = utf8mb4;

-- 用户
create table `user`
(
	`id`           int unsigned auto_increment,
	`username`     varchar(16)   not null unique,
	`password`     varchar(64)   not null,
	`nickname`     varchar(32)   not null,
	`avatar`       varchar(1024) not null,
	`email`        varchar(64)   not null unique,
	`setting_id`   int unsigned  not null,
	`role_id`      int unsigned  not null,
	`gmt_created`  datetime,
	`gmt_modified` datetime,
	primary key (`id`),
	foreign key (`setting_id`) references `setting` (`id`) on delete cascade on update cascade,
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
	`id`               bigint             not null,
	`name`             varchar(64) unique not null,
	`show_name`        varchar(64)        not null,
	`cover`            varchar(1024)      not null,
	`repo_path`        varchar(2048)      not null,
	`latest_commit_id` varchar(40),
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

-- 星星（收藏）
create table `star`
(
	`id`           bigint       not null,
	`user_id`      int unsigned not null,
	`anthology_id` bigint       not null,
	`gmt_created`  datetime,
	`gmt_modified` datetime,
	primary key (`id`),
	foreign key (`user_id`) references `user` (`id`) on delete cascade on update cascade,
	foreign key (`anthology_id`) references `anthology` (`id`) on delete cascade on update cascade
) engine InnoDB
  default charset = utf8mb4;


-- 初始化一些数据
insert into `role` (`name`, `show_name`, `gmt_created`, `gmt_modified`)
values ('ROLE_PRESERVE_ADMIN', '预留管理员', now(), now()), -- id为1，预留管理员
	   ('ROLE_ADMIN', '管理员', now(), now()),              -- id为2，管理员
	   ('ROLE_MEMBER', '团队成员', now(), now()); -- id为3，团队成员

insert into `permission`(`name`, `show_name`, `gmt_created`, `gmt_modified`)
values ('edit_user', '编辑用户', now(), now()),                -- id为1，增加或者编辑用户
	   ('edit_anthology', '编辑文集', now(), now()),           -- id为2，增加或者修改或者删除文集
	   ('alter_system_setting', '修改系统设置', now(), now()), -- id为3，修改系统设置
	   ('browse_article', '浏览内部文章', now(), now()); -- id为4，浏览内部文章

insert into `setting` (`receive_update_email`, `gmt_created`, `gmt_modified`)
values (true, now(), now()); -- id为1

insert into `role_permission`
values (1, 1),
	   (1, 2),
	   (1, 3),
	   (1, 4),
	   (2, 1),
	   (2, 2),
	   (2, 3),
	   (2, 4),
	   (3, 4);

-- 初始管理员账户，用户名：admin，密码：789101112
insert into `user` (`username`, `password`, `nickname`, `avatar`, `email`, `role_id`, `setting_id`, `gmt_created`, `gmt_modified`)
	value ('admin', '$2a$10$DnVDUKyYw77O5VTbQsi7XOktMOGUajGwq1xkoDn2BM6fKvMCtZNtu', 'Administrator', '/static/avatar/default/1.png', 'example@example.com', 1, 1, now(), now());