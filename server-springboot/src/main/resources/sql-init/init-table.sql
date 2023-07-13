-- 初始化表
drop table if exists "user", "setting", "role", "role_permission", "permission", "public_key", "article", "anthology", "star", "system_setting";

-- 角色
create table "role"
(
	"id"           serial primary key,
	"name"         varchar(32) unique not null,
	"show_name"    varchar(32) unique not null,
	"gmt_created"  timestamp,
	"gmt_modified" timestamp
);

-- 权限
create table "permission"
(
	"id"           serial primary key,
	"name"         varchar(32) unique not null,
	"show_name"    varchar(32) unique not null,
	"gmt_created"  timestamp,
	"gmt_modified" timestamp
);

-- 角色对权限表
create table "role_permission"
(
	"role_id"       int,
	"permission_id" int,
	primary key ("role_id", "permission_id"),
	foreign key ("role_id") references "role" ("id") on delete cascade on update cascade,
	foreign key ("permission_id") references "permission" ("id") on delete cascade on update cascade
);

-- 用户偏好设置
create table "setting"
(
	"id"                   serial primary key,
	"receive_update_email" boolean,
	"receive_new_email"    boolean,
	"gmt_created"          timestamp,
	"gmt_modified"         timestamp
);

-- 用户
create table "user"
(
	"id"           serial primary key,
	"username"     varchar(16) not null unique,
	"password"     varchar(64) not null,
	"nickname"     varchar(32) not null,
	"avatar"       varchar(1024),
	"email"        varchar(64) not null unique,
	"setting_id"   int         not null,
	"role_id"      int         not null,
	"gmt_created"  timestamp,
	"gmt_modified" timestamp,
	foreign key ("setting_id") references "setting" ("id") on delete cascade on update cascade,
	foreign key ("role_id") references "role" ("id") on delete cascade on update cascade
);

-- 公钥信息
create table "public_key"
(
	"id"           serial primary key,
	"line"         int not null,
	"user_id"      int not null,
	"gmt_created"  timestamp,
	"gmt_modified" timestamp,
	foreign key ("user_id") references "user" ("id")
);

-- 文集
create table "anthology"
(
	"id"               bigint primary key,
	"name"             varchar(64) unique not null,
	"show_name"        varchar(64)        not null,
	"cover"            varchar(1024),
	"repo_path"        varchar(2048)      not null,
	"latest_commit_id" varchar(40),
	"gmt_created"      timestamp,
	"gmt_modified"     timestamp
);

-- 文章索引信息
create table "article"
(
	"id"           bigint primary key,
	"file_path"    varchar(2048) not null,
	"anthology_id" bigint        not null,
	"gmt_created"  timestamp,
	"gmt_modified" timestamp,
	foreign key ("anthology_id") references "anthology" ("id") on delete cascade on update cascade
);

-- 星星（收藏）
create table "star"
(
	"id"           bigint primary key,
	"user_id"      int    not null,
	"anthology_id" bigint not null,
	"gmt_created"  timestamp,
	"gmt_modified" timestamp,
	unique ("user_id", "anthology_id"),
	foreign key ("user_id") references "user" ("id") on delete cascade on update cascade,
	foreign key ("anthology_id") references "anthology" ("id") on delete cascade on update cascade
);

-- 系统全局设置
create table "system_setting"
(
	"key"   varchar(16) primary key,
	"value" varchar(128) not null
);