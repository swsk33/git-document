-- 初始化外键约束

alter table "user"
	add foreign key ("role_id") references "role" ("id")
		on delete cascade on update cascade;

alter table "setting"
	add foreign key ("user_id") references "user" ("id")
		on delete cascade on update cascade;

alter table "login_record"
	add foreign key ("user_id") references "user" ("id")
		on delete cascade on update cascade;

alter table "public_key"
	add foreign key ("user_id") references "user" ("id")
		on delete cascade on update cascade;

alter table "role_permission"
	add foreign key ("role_id") references "role" ("id")
		on delete cascade on update cascade;

alter table "role_permission"
	add foreign key ("permission_id") references "permission" ("id")
		on delete cascade on update cascade;

alter table "article"
	add foreign key ("anthology_id") references "anthology" ("id")
		on delete cascade on update cascade;

alter table "star"
	add foreign key ("user_id") references "user" ("id")
		on delete cascade on update cascade;

alter table "star"
	add foreign key ("anthology_id") references "anthology" ("id")
		on delete cascade on update cascade;