--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = awag_schema, pg_catalog;

ALTER TABLE ONLY awag_schema.parameter_score_factor_scored DROP CONSTRAINT parameter_score_factor_scored_mscoringfactorsscored_mid;
ALTER TABLE ONLY awag_schema.study_study_group DROP CONSTRAINT fk_study_study_group_study_mid;
ALTER TABLE ONLY awag_schema.study_study_group DROP CONSTRAINT fk_study_study_group_mgroups_mid;
ALTER TABLE ONLY awag_schema.study_group_animal DROP CONSTRAINT fk_study_group_animal_studygroup_mid;
ALTER TABLE ONLY awag_schema.study_group_animal DROP CONSTRAINT fk_study_group_animal_manimals_mid;
ALTER TABLE ONLY awag_schema.parameter_score DROP CONSTRAINT fk_parameter_score_mparameterscored_mid;
ALTER TABLE ONLY awag_schema.parameter_score_factor_scored DROP CONSTRAINT fk_parameter_score_factor_scored_parameterscore_mid;
ALTER TABLE ONLY awag_schema.factor_scored DROP CONSTRAINT fk_factor_scored_mscoringfactor_mid;
ALTER TABLE ONLY awag_schema.assessment_template_parameter_factor DROP CONSTRAINT fk_assessment_template_parameter_factor_parameter_id;
ALTER TABLE ONLY awag_schema.assessment_template_parameter_factor DROP CONSTRAINT fk_assessment_template_parameter_factor_factor_id;
ALTER TABLE ONLY awag_schema.assessment_template_parameter_factor DROP CONSTRAINT fk_assessment_template_parameter_factor_assessment_template_id;
ALTER TABLE ONLY awag_schema.assessment_template DROP CONSTRAINT fk_assessment_template_mscale_mid;
ALTER TABLE ONLY awag_schema.assessment_score_parameter_score DROP CONSTRAINT fk_assessment_score_parameter_score_mparametersscored_mid;
ALTER TABLE ONLY awag_schema.assessment_score_parameter_score DROP CONSTRAINT fk_assessment_score_parameter_score_assessmentscore_mid;
ALTER TABLE ONLY awag_schema.assessment DROP CONSTRAINT fk_assessment_mstudy_mid;
ALTER TABLE ONLY awag_schema.assessment DROP CONSTRAINT fk_assessment_mscore_mid;
ALTER TABLE ONLY awag_schema.assessment DROP CONSTRAINT fk_assessment_mreason_mid;
ALTER TABLE ONLY awag_schema.assessment DROP CONSTRAINT fk_assessment_mperformedby_mid;
ALTER TABLE ONLY awag_schema.assessment DROP CONSTRAINT fk_assessment_manimalhousing_mid;
ALTER TABLE ONLY awag_schema.assessment DROP CONSTRAINT fk_assessment_manimal_mid;
ALTER TABLE ONLY awag_schema.animal DROP CONSTRAINT fk_animal_mspecies_mid;
ALTER TABLE ONLY awag_schema.animal DROP CONSTRAINT fk_animal_msource_mid;
ALTER TABLE ONLY awag_schema.animal DROP CONSTRAINT fk_animal_msex_mid;
ALTER TABLE ONLY awag_schema.animal DROP CONSTRAINT fk_animal_mfather_mid;
ALTER TABLE ONLY awag_schema.animal DROP CONSTRAINT fk_animal_mdam_mid;
ALTER TABLE ONLY awag_schema.animal DROP CONSTRAINT fk_animal_massessmenttemplate_mid;
DROP INDEX awag_schema.users_lower_mname_unique;
DROP INDEX awag_schema.study_lower_mstudynumber_unique;
DROP INDEX awag_schema.study_group_lower_mstudygroupnumber_unique;
DROP INDEX awag_schema.species_lower_mname_unique;
DROP INDEX awag_schema.source_lower_mname_unique;
DROP INDEX awag_schema.scale_lower_mname_unique;
DROP INDEX awag_schema.reason_lower_mname_unique;
DROP INDEX awag_schema.parameter_lower_mname_unique;
DROP INDEX awag_schema.housing_lower_mname_unique;
DROP INDEX awag_schema.factor_lower_mname_unique;
DROP INDEX awag_schema.assessment_template_lower_mname_unique;
DROP INDEX awag_schema.assessment_reason_lower_mname_unique;
DROP INDEX awag_schema.animal_lower_manimalnumber_unique;
DROP INDEX awag_schema.animal_housing_lower_mname_unique;
ALTER TABLE ONLY awag_schema.users DROP CONSTRAINT users_pkey;
ALTER TABLE ONLY awag_schema.users DROP CONSTRAINT users_mname_key;
ALTER TABLE ONLY awag_schema.study_study_group DROP CONSTRAINT study_study_group_pkey;
ALTER TABLE ONLY awag_schema.study DROP CONSTRAINT study_pkey;
ALTER TABLE ONLY awag_schema.study DROP CONSTRAINT study_mstudynumber_key;
ALTER TABLE ONLY awag_schema.study_group DROP CONSTRAINT study_group_pkey;
ALTER TABLE ONLY awag_schema.study_group_animal DROP CONSTRAINT study_group_animal_pkey;
ALTER TABLE ONLY awag_schema.species DROP CONSTRAINT species_pkey;
ALTER TABLE ONLY awag_schema.species DROP CONSTRAINT species_mname_key;
ALTER TABLE ONLY awag_schema.source DROP CONSTRAINT source_pkey;
ALTER TABLE ONLY awag_schema.source DROP CONSTRAINT source_mname_key;
ALTER TABLE ONLY awag_schema.sex DROP CONSTRAINT sex_pkey;
ALTER TABLE ONLY awag_schema.sequence DROP CONSTRAINT sequence_pkey;
ALTER TABLE ONLY awag_schema.scale DROP CONSTRAINT scale_pkey;
ALTER TABLE ONLY awag_schema.parameter_score DROP CONSTRAINT parameter_score_pkey;
ALTER TABLE ONLY awag_schema.parameter_score_factor_scored DROP CONSTRAINT parameter_score_factor_scored_pkey;
ALTER TABLE ONLY awag_schema.parameter DROP CONSTRAINT parameter_pkey;
ALTER TABLE ONLY awag_schema.factor_scored DROP CONSTRAINT factor_scored_pkey;
ALTER TABLE ONLY awag_schema.factor DROP CONSTRAINT factor_pkey;
ALTER TABLE ONLY awag_schema.assessment_template DROP CONSTRAINT assessment_template_pkey;
ALTER TABLE ONLY awag_schema.assessment_template_parameter_factor DROP CONSTRAINT assessment_template_parameter_factor_pkey;
ALTER TABLE ONLY awag_schema.assessment_score DROP CONSTRAINT assessment_score_pkey;
ALTER TABLE ONLY awag_schema.assessment_score_parameter_score DROP CONSTRAINT assessment_score_parameter_score_pkey;
ALTER TABLE ONLY awag_schema.assessment_reason DROP CONSTRAINT assessment_reason_pkey;
ALTER TABLE ONLY awag_schema.assessment_reason DROP CONSTRAINT assessment_reason_mname_key;
ALTER TABLE ONLY awag_schema.assessment DROP CONSTRAINT assessment_pkey;
ALTER TABLE ONLY awag_schema.animal DROP CONSTRAINT animal_pkey;
ALTER TABLE ONLY awag_schema.animal DROP CONSTRAINT animal_manimalnumber_key;
ALTER TABLE ONLY awag_schema.animal_housing DROP CONSTRAINT animal_housing_pkey;
ALTER TABLE ONLY awag_schema.animal_housing DROP CONSTRAINT animal_housing_mname_key;
DROP TABLE awag_schema.users;
DROP TABLE awag_schema.study_study_group;
DROP TABLE awag_schema.study_group_animal;
DROP TABLE awag_schema.study_group;
DROP TABLE awag_schema.study;
DROP TABLE awag_schema.species;
DROP TABLE awag_schema.source;
DROP TABLE awag_schema.sex;
DROP TABLE awag_schema.sequence;
DROP TABLE awag_schema.scale;
DROP TABLE awag_schema.parameter_score_factor_scored;
DROP TABLE awag_schema.parameter_score;
DROP TABLE awag_schema.parameter;
DROP TABLE awag_schema.factor_scored;
DROP TABLE awag_schema.factor;
DROP TABLE awag_schema.assessment_template_parameter_factor;
DROP TABLE awag_schema.assessment_template;
DROP TABLE awag_schema.assessment_score_parameter_score;
DROP TABLE awag_schema.assessment_score;
DROP TABLE awag_schema.assessment_reason;
DROP TABLE awag_schema.assessment;
DROP TABLE awag_schema.animal_housing;
DROP TABLE awag_schema.animal;
DROP SCHEMA awag_schema;
--
-- Name: awag_schema; Type: SCHEMA; Schema: -; Owner: awag
--

CREATE SCHEMA awag_schema;


ALTER SCHEMA awag_schema OWNER TO awag;

--
-- Name: SCHEMA awag_schema; Type: COMMENT; Schema: -; Owner: awag
--

COMMENT ON SCHEMA awag_schema IS 'awag application schema';


SET search_path = awag_schema, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: animal; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE animal (
    mid bigint NOT NULL,
    manimalnumber character varying(255) NOT NULL,
    mdateofbirth character varying(255),
    misalive boolean,
    misassessed boolean,
    massessmenttemplate_mid bigint,
    mdam_mid bigint,
    mfather_mid bigint,
    msex_mid bigint,
    msource_mid bigint,
    mspecies_mid bigint,
    misdeleted boolean DEFAULT false NOT NULL
);


ALTER TABLE awag_schema.animal OWNER TO awag;

--
-- Name: animal_housing; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE animal_housing (
    mid bigint NOT NULL,
    mname character varying(255) NOT NULL
);


ALTER TABLE awag_schema.animal_housing OWNER TO awag;

--
-- Name: assessment; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE assessment (
    mid bigint NOT NULL,
    mdate character varying(255),
    miscomplete boolean,
    manimal_mid bigint,
    manimalhousing_mid bigint,
    mperformedby_mid bigint,
    mreason_mid bigint,
    mscore_mid bigint,
    mstudy_mid bigint
);


ALTER TABLE awag_schema.assessment OWNER TO awag;

--
-- Name: assessment_reason; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE assessment_reason (
    mid bigint NOT NULL,
    mname character varying(255) NOT NULL
);


ALTER TABLE awag_schema.assessment_reason OWNER TO awag;

--
-- Name: assessment_score; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE assessment_score (
    mid bigint NOT NULL
);


ALTER TABLE awag_schema.assessment_score OWNER TO awag;

--
-- Name: assessment_score_parameter_score; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE assessment_score_parameter_score (
    assessmentscore_mid bigint NOT NULL,
    mparametersscored_mid bigint NOT NULL
);


ALTER TABLE awag_schema.assessment_score_parameter_score OWNER TO awag;

--
-- Name: assessment_template; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE assessment_template (
    mid bigint NOT NULL,
    mname character varying(255),
    mscale_mid bigint
);


ALTER TABLE awag_schema.assessment_template OWNER TO awag;

--
-- Name: assessment_template_parameter_factor; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE assessment_template_parameter_factor (
    assessment_template_id bigint NOT NULL,
    parameter_id bigint NOT NULL,
    factor_id bigint NOT NULL
);


ALTER TABLE awag_schema.assessment_template_parameter_factor OWNER TO awag;

--
-- Name: factor; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE factor (
    mid bigint NOT NULL,
    mname character varying(255)
);


ALTER TABLE awag_schema.factor OWNER TO awag;

--
-- Name: factor_scored; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE factor_scored (
    mid bigint NOT NULL,
    misignored boolean,
    mscore integer,
    mscoringfactor_mid bigint
);


ALTER TABLE awag_schema.factor_scored OWNER TO awag;

--
-- Name: parameter; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE parameter (
    mid bigint NOT NULL,
    mname character varying(255)
);


ALTER TABLE awag_schema.parameter OWNER TO awag;

--
-- Name: parameter_score; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE parameter_score (
    mid bigint NOT NULL,
    maveragescore double precision,
    mcomment character varying(255),
    mparameterscored_mid bigint
);


ALTER TABLE awag_schema.parameter_score OWNER TO awag;

--
-- Name: parameter_score_factor_scored; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE parameter_score_factor_scored (
    parameterscore_mid bigint NOT NULL,
    mscoringfactorsscored_mid bigint NOT NULL
);


ALTER TABLE awag_schema.parameter_score_factor_scored OWNER TO awag;

--
-- Name: scale; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE scale (
    mid bigint NOT NULL,
    mmax integer,
    mmin integer,
    mname character varying(255)
);


ALTER TABLE awag_schema.scale OWNER TO awag;

--
-- Name: sequence; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE sequence (
    seq_name character varying(50) NOT NULL,
    seq_count numeric(38,0)
);


ALTER TABLE awag_schema.sequence OWNER TO awag;

--
-- Name: sex; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE sex (
    mid bigint NOT NULL,
    mname character varying(255)
);


ALTER TABLE awag_schema.sex OWNER TO awag;

--
-- Name: source; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE source (
    mid bigint NOT NULL,
    mname character varying(255) NOT NULL
);


ALTER TABLE awag_schema.source OWNER TO awag;

--
-- Name: species; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE species (
    mid bigint NOT NULL,
    mname character varying(255) NOT NULL,
    misdeleted boolean DEFAULT false NOT NULL
);


ALTER TABLE awag_schema.species OWNER TO awag;

--
-- Name: study; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE study (
    mid bigint NOT NULL,
    misopen boolean DEFAULT true,
    mstudynumber character varying(255) NOT NULL
);


ALTER TABLE awag_schema.study OWNER TO awag;

--
-- Name: study_group; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE study_group (
    mid bigint NOT NULL,
    mstudygroupnumber character varying(255)
);


ALTER TABLE awag_schema.study_group OWNER TO awag;

--
-- Name: study_group_animal; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE study_group_animal (
    studygroup_mid bigint NOT NULL,
    manimals_mid bigint NOT NULL
);


ALTER TABLE awag_schema.study_group_animal OWNER TO awag;

--
-- Name: study_study_group; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE study_study_group (
    study_mid bigint NOT NULL,
    mgroups_mid bigint NOT NULL
);


ALTER TABLE awag_schema.study_study_group OWNER TO awag;

--
-- Name: users; Type: TABLE; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE TABLE users (
    mid bigint NOT NULL,
    mname character varying(255) NOT NULL
);


ALTER TABLE awag_schema.users OWNER TO awag;

--
-- Data for Name: animal; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY animal (mid, manimalnumber, mdateofbirth, misalive, misassessed, massessmenttemplate_mid, mdam_mid, mfather_mid, msex_mid, msource_mid, mspecies_mid, misdeleted) FROM stdin;
10001	Animal 2	2012-03-20T00:00:00.000Z	t	t	10000	\N	\N	10001	10000	10000	f
10000	Animal 1	2010-02-10T00:00:00.000Z	t	t	10000	\N	\N	10000	10000	10000	f
10002	Animal 3	2013-02-01T00:00:00.000Z	t	t	10000	10000	10001	10000	10000	10000	f
10003	Animal 4	2011-02-01T00:00:00.000Z	t	t	10002	10000	10001	10000	10000	10000	f
\.


--
-- Data for Name: animal_housing; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY animal_housing (mid, mname) FROM stdin;
10000	Housing 1
10001	Housing 2
10002	Housing 3
\.


--
-- Data for Name: assessment; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY assessment (mid, mdate, miscomplete, manimal_mid, manimalhousing_mid, mperformedby_mid, mreason_mid, mscore_mid, mstudy_mid) FROM stdin;
\.


--
-- Data for Name: assessment_reason; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY assessment_reason (mid, mname) FROM stdin;
10000	Reason 1
10001	Reason 2
10002	Reason 3
\.


--
-- Data for Name: assessment_score; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY assessment_score (mid) FROM stdin;
\.


--
-- Data for Name: assessment_score_parameter_score; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY assessment_score_parameter_score (assessmentscore_mid, mparametersscored_mid) FROM stdin;
\.


--
-- Data for Name: assessment_template; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY assessment_template (mid, mname, mscale_mid) FROM stdin;
10000	Template 1	10000
10001	Template 2	10000
10002	Template 3	10000
\.


--
-- Data for Name: assessment_template_parameter_factor; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY assessment_template_parameter_factor (assessment_template_id, parameter_id, factor_id) FROM stdin;
10000	10000	10000
10000	10000	10001
10000	10000	10002
10000	10000	10003
10002	10000	10000
10002	10000	10001
10002	10000	10002
10002	10000	10003
10002	10001	10000
10002	10001	10001
10002	10001	10002
10002	10001	10003
\.


--
-- Data for Name: factor; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY factor (mid, mname) FROM stdin;
10000	Factor 1
10001	Factor 2
10002	Factor 3
10003	Factor 4
\.


--
-- Data for Name: factor_scored; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY factor_scored (mid, misignored, mscore, mscoringfactor_mid) FROM stdin;
\.


--
-- Data for Name: parameter; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY parameter (mid, mname) FROM stdin;
10000	Parameter 1
10001	Parameter 2
10002	Parameter 3
\.


--
-- Data for Name: parameter_score; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY parameter_score (mid, maveragescore, mcomment, mparameterscored_mid) FROM stdin;
\.


--
-- Data for Name: parameter_score_factor_scored; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY parameter_score_factor_scored (parameterscore_mid, mscoringfactorsscored_mid) FROM stdin;
\.


--
-- Data for Name: scale; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY scale (mid, mmax, mmin, mname) FROM stdin;
10000	10	1	1 to 10
10001	5	1	1 to 5
10002	3	1	1 to 3
\.


--
-- Data for Name: sequence; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY sequence (seq_name, seq_count) FROM stdin;
SEQ_GEN	1
\.


--
-- Data for Name: sex; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY sex (mid, mname) FROM stdin;
10000	Female
10001	Male
\.


--
-- Data for Name: source; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY source (mid, mname) FROM stdin;
10000	Source 1
10001	Source 2
10002	Source 3
\.


--
-- Data for Name: species; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY species (mid, mname, misdeleted) FROM stdin;
10000	Species 1	f
10001	Species 2	f
10002	Species 3	f
\.


--
-- Data for Name: study; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY study (mid, misopen, mstudynumber) FROM stdin;
10000	t	Study 1
10001	t	Study 2
10002	t	Study 3
\.


--
-- Data for Name: study_group; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY study_group (mid, mstudygroupnumber) FROM stdin;
10000	Study Group 1
10001	Study Group 2
10002	Study Group 3
\.


--
-- Data for Name: study_group_animal; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY study_group_animal (studygroup_mid, manimals_mid) FROM stdin;
10000	10000
10000	10001
\.


--
-- Data for Name: study_study_group; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY study_study_group (study_mid, mgroups_mid) FROM stdin;
10000	10000
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY users (mid, mname) FROM stdin;
10000	User 1
10001	User 2
10002	User 3
\.


--
-- Name: animal_housing_mname_key; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY animal_housing
    ADD CONSTRAINT animal_housing_mname_key UNIQUE (mname);


--
-- Name: animal_housing_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY animal_housing
    ADD CONSTRAINT animal_housing_pkey PRIMARY KEY (mid);


--
-- Name: animal_manimalnumber_key; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT animal_manimalnumber_key UNIQUE (manimalnumber);


--
-- Name: animal_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT animal_pkey PRIMARY KEY (mid);


--
-- Name: assessment_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT assessment_pkey PRIMARY KEY (mid);


--
-- Name: assessment_reason_mname_key; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY assessment_reason
    ADD CONSTRAINT assessment_reason_mname_key UNIQUE (mname);


--
-- Name: assessment_reason_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY assessment_reason
    ADD CONSTRAINT assessment_reason_pkey PRIMARY KEY (mid);


--
-- Name: assessment_score_parameter_score_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY assessment_score_parameter_score
    ADD CONSTRAINT assessment_score_parameter_score_pkey PRIMARY KEY (assessmentscore_mid, mparametersscored_mid);


--
-- Name: assessment_score_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY assessment_score
    ADD CONSTRAINT assessment_score_pkey PRIMARY KEY (mid);


--
-- Name: assessment_template_parameter_factor_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY assessment_template_parameter_factor
    ADD CONSTRAINT assessment_template_parameter_factor_pkey PRIMARY KEY (assessment_template_id, parameter_id, factor_id);


--
-- Name: assessment_template_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY assessment_template
    ADD CONSTRAINT assessment_template_pkey PRIMARY KEY (mid);


--
-- Name: factor_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY factor
    ADD CONSTRAINT factor_pkey PRIMARY KEY (mid);


--
-- Name: factor_scored_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY factor_scored
    ADD CONSTRAINT factor_scored_pkey PRIMARY KEY (mid);


--
-- Name: parameter_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY parameter
    ADD CONSTRAINT parameter_pkey PRIMARY KEY (mid);


--
-- Name: parameter_score_factor_scored_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY parameter_score_factor_scored
    ADD CONSTRAINT parameter_score_factor_scored_pkey PRIMARY KEY (parameterscore_mid, mscoringfactorsscored_mid);


--
-- Name: parameter_score_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY parameter_score
    ADD CONSTRAINT parameter_score_pkey PRIMARY KEY (mid);


--
-- Name: scale_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY scale
    ADD CONSTRAINT scale_pkey PRIMARY KEY (mid);


--
-- Name: sequence_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY sequence
    ADD CONSTRAINT sequence_pkey PRIMARY KEY (seq_name);


--
-- Name: sex_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY sex
    ADD CONSTRAINT sex_pkey PRIMARY KEY (mid);


--
-- Name: source_mname_key; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY source
    ADD CONSTRAINT source_mname_key UNIQUE (mname);


--
-- Name: source_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY source
    ADD CONSTRAINT source_pkey PRIMARY KEY (mid);


--
-- Name: species_mname_key; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY species
    ADD CONSTRAINT species_mname_key UNIQUE (mname);


--
-- Name: species_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY species
    ADD CONSTRAINT species_pkey PRIMARY KEY (mid);


--
-- Name: study_group_animal_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY study_group_animal
    ADD CONSTRAINT study_group_animal_pkey PRIMARY KEY (studygroup_mid, manimals_mid);


--
-- Name: study_group_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY study_group
    ADD CONSTRAINT study_group_pkey PRIMARY KEY (mid);


--
-- Name: study_mstudynumber_key; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY study
    ADD CONSTRAINT study_mstudynumber_key UNIQUE (mstudynumber);


--
-- Name: study_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY study
    ADD CONSTRAINT study_pkey PRIMARY KEY (mid);


--
-- Name: study_study_group_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY study_study_group
    ADD CONSTRAINT study_study_group_pkey PRIMARY KEY (study_mid, mgroups_mid);


--
-- Name: users_mname_key; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_mname_key UNIQUE (mname);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: awag_schema; Owner: awag; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (mid);


--
-- Name: animal_housing_lower_mname_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX animal_housing_lower_mname_unique ON animal_housing USING btree (lower((mname)::text));


--
-- Name: animal_lower_manimalnumber_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX animal_lower_manimalnumber_unique ON animal USING btree (lower((manimalnumber)::text));


--
-- Name: assessment_reason_lower_mname_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX assessment_reason_lower_mname_unique ON assessment_reason USING btree (lower((mname)::text));


--
-- Name: assessment_template_lower_mname_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX assessment_template_lower_mname_unique ON assessment_template USING btree (lower((mname)::text));


--
-- Name: factor_lower_mname_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX factor_lower_mname_unique ON factor USING btree (lower((mname)::text));


--
-- Name: housing_lower_mname_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX housing_lower_mname_unique ON animal_housing USING btree (lower((mname)::text));


--
-- Name: parameter_lower_mname_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX parameter_lower_mname_unique ON parameter USING btree (lower((mname)::text));


--
-- Name: reason_lower_mname_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX reason_lower_mname_unique ON assessment_reason USING btree (lower((mname)::text));


--
-- Name: scale_lower_mname_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX scale_lower_mname_unique ON scale USING btree (lower((mname)::text));


--
-- Name: source_lower_mname_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX source_lower_mname_unique ON source USING btree (lower((mname)::text));


--
-- Name: species_lower_mname_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX species_lower_mname_unique ON species USING btree (lower((mname)::text));


--
-- Name: study_group_lower_mstudygroupnumber_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX study_group_lower_mstudygroupnumber_unique ON study_group USING btree (lower((mstudygroupnumber)::text));


--
-- Name: study_lower_mstudynumber_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX study_lower_mstudynumber_unique ON study USING btree (lower((mstudynumber)::text));


--
-- Name: users_lower_mname_unique; Type: INDEX; Schema: awag_schema; Owner: awag; Tablespace: 
--

CREATE UNIQUE INDEX users_lower_mname_unique ON users USING btree (lower((mname)::text));


--
-- Name: fk_animal_massessmenttemplate_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_massessmenttemplate_mid FOREIGN KEY (massessmenttemplate_mid) REFERENCES assessment_template(mid);


--
-- Name: fk_animal_mdam_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_mdam_mid FOREIGN KEY (mdam_mid) REFERENCES animal(mid);


--
-- Name: fk_animal_mfather_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_mfather_mid FOREIGN KEY (mfather_mid) REFERENCES animal(mid);


--
-- Name: fk_animal_msex_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_msex_mid FOREIGN KEY (msex_mid) REFERENCES sex(mid);


--
-- Name: fk_animal_msource_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_msource_mid FOREIGN KEY (msource_mid) REFERENCES source(mid);


--
-- Name: fk_animal_mspecies_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_mspecies_mid FOREIGN KEY (mspecies_mid) REFERENCES species(mid);


--
-- Name: fk_assessment_manimal_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_manimal_mid FOREIGN KEY (manimal_mid) REFERENCES animal(mid);


--
-- Name: fk_assessment_manimalhousing_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_manimalhousing_mid FOREIGN KEY (manimalhousing_mid) REFERENCES animal_housing(mid);


--
-- Name: fk_assessment_mperformedby_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_mperformedby_mid FOREIGN KEY (mperformedby_mid) REFERENCES users(mid);


--
-- Name: fk_assessment_mreason_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_mreason_mid FOREIGN KEY (mreason_mid) REFERENCES assessment_reason(mid);


--
-- Name: fk_assessment_mscore_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_mscore_mid FOREIGN KEY (mscore_mid) REFERENCES assessment_score(mid);


--
-- Name: fk_assessment_mstudy_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_mstudy_mid FOREIGN KEY (mstudy_mid) REFERENCES study(mid);


--
-- Name: fk_assessment_score_parameter_score_assessmentscore_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment_score_parameter_score
    ADD CONSTRAINT fk_assessment_score_parameter_score_assessmentscore_mid FOREIGN KEY (assessmentscore_mid) REFERENCES assessment_score(mid);


--
-- Name: fk_assessment_score_parameter_score_mparametersscored_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment_score_parameter_score
    ADD CONSTRAINT fk_assessment_score_parameter_score_mparametersscored_mid FOREIGN KEY (mparametersscored_mid) REFERENCES parameter_score(mid);


--
-- Name: fk_assessment_template_mscale_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment_template
    ADD CONSTRAINT fk_assessment_template_mscale_mid FOREIGN KEY (mscale_mid) REFERENCES scale(mid);


--
-- Name: fk_assessment_template_parameter_factor_assessment_template_id; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment_template_parameter_factor
    ADD CONSTRAINT fk_assessment_template_parameter_factor_assessment_template_id FOREIGN KEY (assessment_template_id) REFERENCES assessment_template(mid);


--
-- Name: fk_assessment_template_parameter_factor_factor_id; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment_template_parameter_factor
    ADD CONSTRAINT fk_assessment_template_parameter_factor_factor_id FOREIGN KEY (factor_id) REFERENCES factor(mid);


--
-- Name: fk_assessment_template_parameter_factor_parameter_id; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY assessment_template_parameter_factor
    ADD CONSTRAINT fk_assessment_template_parameter_factor_parameter_id FOREIGN KEY (parameter_id) REFERENCES parameter(mid);


--
-- Name: fk_factor_scored_mscoringfactor_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY factor_scored
    ADD CONSTRAINT fk_factor_scored_mscoringfactor_mid FOREIGN KEY (mscoringfactor_mid) REFERENCES factor(mid);


--
-- Name: fk_parameter_score_factor_scored_parameterscore_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY parameter_score_factor_scored
    ADD CONSTRAINT fk_parameter_score_factor_scored_parameterscore_mid FOREIGN KEY (parameterscore_mid) REFERENCES parameter_score(mid);


--
-- Name: fk_parameter_score_mparameterscored_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY parameter_score
    ADD CONSTRAINT fk_parameter_score_mparameterscored_mid FOREIGN KEY (mparameterscored_mid) REFERENCES parameter(mid);


--
-- Name: fk_study_group_animal_manimals_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY study_group_animal
    ADD CONSTRAINT fk_study_group_animal_manimals_mid FOREIGN KEY (manimals_mid) REFERENCES animal(mid);


--
-- Name: fk_study_group_animal_studygroup_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY study_group_animal
    ADD CONSTRAINT fk_study_group_animal_studygroup_mid FOREIGN KEY (studygroup_mid) REFERENCES study_group(mid);


--
-- Name: fk_study_study_group_mgroups_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY study_study_group
    ADD CONSTRAINT fk_study_study_group_mgroups_mid FOREIGN KEY (mgroups_mid) REFERENCES study_group(mid);


--
-- Name: fk_study_study_group_study_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY study_study_group
    ADD CONSTRAINT fk_study_study_group_study_mid FOREIGN KEY (study_mid) REFERENCES study(mid);


--
-- Name: parameter_score_factor_scored_mscoringfactorsscored_mid; Type: FK CONSTRAINT; Schema: awag_schema; Owner: awag
--

ALTER TABLE ONLY parameter_score_factor_scored
    ADD CONSTRAINT parameter_score_factor_scored_mscoringfactorsscored_mid FOREIGN KEY (mscoringfactorsscored_mid) REFERENCES factor_scored(mid);


--
-- Name: awag_schema; Type: ACL; Schema: -; Owner: awag
--

REVOKE ALL ON SCHEMA awag_schema FROM PUBLIC;
REVOKE ALL ON SCHEMA awag_schema FROM awag;
GRANT ALL ON SCHEMA awag_schema TO awag;


--
-- PostgreSQL database dump complete
--

