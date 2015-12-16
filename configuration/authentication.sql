DROP TABLE IF EXISTS users_groups;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS groups;

CREATE TABLE users (
	user_name character varying(50) NOT NULL,
	password character varying(128) NOT NULL,
	PRIMARY KEY(user_name)
);

ALTER TABLE public.users OWNER TO postgres;

CREATE TABLE groups (
	 group_name character varying(20) NOT NULL,
	 PRIMARY KEY(group_name)
);

ALTER TABLE public.groups OWNER TO postgres;

CREATE TABLE users_groups (
	user_name character varying(50) NOT NULL,
	group_name character varying(20) NOT NULL,
	PRIMARY KEY(user_name, group_name)
);

ALTER TABLE public.users_groups OWNER TO postgres;

INSERT INTO groups(group_name) VALUES('admin');

INSERT INTO users(user_name, password) VALUES('admin', 'd82494f05d6917ba02f7aaa29689ccb444bb73f20380876cb05d1f37537b7892');

INSERT INTO users_groups(user_name, group_name) VALUES('admin','admin');