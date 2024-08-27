-- 初始化索引

-- 用户表
create index on "user" ("username");
create index on "user" ("email");

-- 公钥
create index on "public_key" ("content");
create index on "public_key" ("user_id");

-- 收藏
create index on "star" ("user_id");
create index on "star" ("anthology_id");

-- 文章
create index on "article" ("file_path");
create index on "article" ("anthology_id");

-- 文集
create index on "anthology" ("name");