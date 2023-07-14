-- 初始化一些数据
insert into "role" ("name", "show_name", "gmt_created", "gmt_modified")
values ('ROLE_PRESERVE_ADMIN', '预留管理员', now(), now()), -- id为1，预留管理员
	   ('ROLE_ADMIN', '管理员', now(), now()),              -- id为2，管理员
	   ('ROLE_MEMBER', '团队成员', now(), now()); -- id为3，团队成员

insert into "permission"("name", "show_name", "gmt_created", "gmt_modified")
values ('edit_user', '编辑用户', now(), now()),                -- id为1，增加或者编辑用户
	   ('edit_anthology', '编辑文集', now(), now()),           -- id为2，增加或者修改或者删除文集
	   ('alter_system_setting', '修改系统设置', now(), now()), -- id为3，修改系统设置
	   ('browse_article', '浏览内部文章', now(), now()); -- id为4，浏览内部文章

insert into "setting" ("receive_update_email", "gmt_created", "gmt_modified")
values (true, now(), now()); -- id为1

insert into "role_permission"
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
insert into "user" ("username", "password", "nickname", "email", "role_id", "setting_id", "gmt_created", "gmt_modified")
values ('admin', '$2a$10$DnVDUKyYw77O5VTbQsi7XOktMOGUajGwq1xkoDn2BM6fKvMCtZNtu', 'Administrator', 'example@example.com', 1, 1, now(), now());

-- 初始化系统设置
insert into "system_setting"
values ('organization_name', '组织名'),
	   ('allow_public', 'true'),
	   ('login_background_image', null),
	   ('main_background_image', null);