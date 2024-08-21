-- 初始化表
drop table if exists "role", "permission", "role_permission", "user", "setting", "login_record", "public_key", "anthology", "article", "star", "system_setting";

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
	primary key ("role_id", "permission_id")
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
	"role_id"      int         not null,
	"gmt_created"  timestamp,
	"gmt_modified" timestamp
);

-- 用户偏好设置
create table "setting"
(
	"user_id"              int primary key,
	"receive_update_email" boolean not null,
	"receive_new_email"    boolean not null,
	"gmt_created"          timestamp,
	"gmt_modified"         timestamp
);

-- 用户的登录记录
create table "login_record"
(
	"user_id"      int primary key,
	"ip"           varchar(16),
	"location"     varchar(16),
	"gmt_created"  timestamp,
	"gmt_modified" timestamp
);

-- 公钥信息
create table "public_key"
(
	"id"           serial primary key,
	"content"      varchar(1024) not null,
	"user_id"      int           not null,
	"gmt_created"  timestamp,
	"gmt_modified" timestamp
);

-- 文集
create table "anthology"
(
	"id"            bigint primary key,
	"name"          varchar(64) unique not null,
	"show_name"     varchar(64)        not null,
	"cover"         varchar(1024),
	"repo_path"     varchar(2048)      not null,
	"latest_commit" varchar(48),
	"status"        varchar(12)        not null,
	"gmt_created"   timestamp,
	"gmt_modified"  timestamp
);

-- 文章索引信息
create table "article"
(
	"id"           bigint primary key,
	"file_path"    varchar(2048) not null,
	"anthology_id" bigint        not null,
	"gmt_created"  timestamp,
	"gmt_modified" timestamp
);

-- 星星（收藏）
create table "star"
(
	"id"           bigint primary key,
	"user_id"      int    not null,
	"anthology_id" bigint not null,
	"gmt_created"  timestamp,
	"gmt_modified" timestamp,
	unique ("user_id", "anthology_id")
);

-- 系统全局设置
create table "system_setting"
(
	"key"   varchar(32) primary key,
	"value" varchar(128)
);