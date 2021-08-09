--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = awag_auth_schema, pg_catalog;

ALTER TABLE ONLY awag_auth_schema.users DROP CONSTRAINT users_pkey;
ALTER TABLE ONLY awag_auth_schema.users_groups DROP CONSTRAINT users_groups_pkey;
ALTER TABLE ONLY awag_auth_schema.groups DROP CONSTRAINT groups_pkey;
DROP TABLE awag_auth_schema.users_groups;
DROP TABLE awag_auth_schema.users;
DROP TABLE awag_auth_schema.groups;

DROP SCHEMA awag_auth_schema;
--
-- Name: awag_auth_schema; Type: SCHEMA; Schema: -; Owner: awag
--

CREATE SCHEMA awag_auth_schema;


ALTER SCHEMA awag_auth_schema OWNER TO awag;


SET search_path = awag_auth_schema, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: groups; Type: TABLE; Schema: awag_auth_schema; Owner: awag; Tablespace:
--

CREATE TABLE groups (
    group_name character varying(20) NOT NULL
);


ALTER TABLE awag_auth_schema.groups OWNER TO awag;

--
-- Name: users; Type: TABLE; Schema: awag_auth_schema; Owner: awag; Tablespace:
--

CREATE TABLE users (
    user_name character varying(50) NOT NULL,
    password character varying(128) NOT NULL
);


ALTER TABLE awag_auth_schema.users OWNER TO awag;

--
-- Name: users_groups; Type: TABLE; Schema: awag_auth_schema; Owner: awag; Tablespace:
--

CREATE TABLE users_groups (
    user_name character varying(50) NOT NULL,
    group_name character varying(50) NOT NULL
);


ALTER TABLE awag_auth_schema.users_groups OWNER TO awag;

--
-- Data for Name: groups; Type: TABLE DATA; Schema: awag_auth_schema; Owner: awag
--

COPY groups (group_name) FROM stdin;
admin
assessmentuser
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: awag_auth_schema; Owner: awag
--
COPY users (user_name, password) FROM stdin;
admin	d82494f05d6917ba02f7aaa29689ccb444bb73f20380876cb05d1f37537b7892
\.


--
-- Data for Name: users_groups; Type: TABLE DATA; Schema: awag_auth_schema; Owner: awag
--

COPY users_groups (user_name, group_name) FROM stdin;
admin	admin
admin	assessmentuser
\.


--
-- Name: groups_pkey; Type: CONSTRAINT; Schema: awag_auth_schema; Owner: awag; Tablespace:
--

ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (group_name);


--
-- Name: users_groups_pkey; Type: CONSTRAINT; Schema: awag_auth_schema; Owner: awag; Tablespace:
--

ALTER TABLE ONLY users_groups
    ADD CONSTRAINT users_groups_pkey PRIMARY KEY (user_name, group_name);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: awag_auth_schema; Owner: awag; Tablespace:
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_name);


--
-- PostgreSQL database dump complete
--

ALTER DATABASE awauth SET search_path=awag_auth_schema,pg_catalog; 
GRANT ALL ON SCHEMA awag_auth_schema TO awag;

