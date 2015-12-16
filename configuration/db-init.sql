--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

ALTER TABLE ONLY public.parameter_score_factor_scored DROP CONSTRAINT parameter_score_factor_scored_mscoringfactorsscored_mid;
ALTER TABLE ONLY public.study_study_group DROP CONSTRAINT fk_study_study_group_study_mid;
ALTER TABLE ONLY public.study_study_group DROP CONSTRAINT fk_study_study_group_mgroups_mid;
ALTER TABLE ONLY public.study_group_animal DROP CONSTRAINT fk_study_group_animal_studygroup_mid;
ALTER TABLE ONLY public.study_group_animal DROP CONSTRAINT fk_study_group_animal_manimals_mid;
ALTER TABLE ONLY public.parameter_score DROP CONSTRAINT fk_parameter_score_mparameterscored_mid;
ALTER TABLE ONLY public.parameter_score_factor_scored DROP CONSTRAINT fk_parameter_score_factor_scored_parameterscore_mid;
ALTER TABLE ONLY public.factor_scored DROP CONSTRAINT fk_factor_scored_mscoringfactor_mid;
ALTER TABLE ONLY public.assessment_template_parameter_factor DROP CONSTRAINT fk_assessment_template_parameter_factor_parameter_id;
ALTER TABLE ONLY public.assessment_template_parameter_factor DROP CONSTRAINT fk_assessment_template_parameter_factor_factor_id;
ALTER TABLE ONLY public.assessment_template_parameter_factor DROP CONSTRAINT fk_assessment_template_parameter_factor_assessment_template_id;
ALTER TABLE ONLY public.assessment_template DROP CONSTRAINT fk_assessment_template_mscale_mid;
ALTER TABLE ONLY public.assessment_score_parameter_score DROP CONSTRAINT fk_assessment_score_parameter_score_mparametersscored_mid;
ALTER TABLE ONLY public.assessment_score_parameter_score DROP CONSTRAINT fk_assessment_score_parameter_score_assessmentscore_mid;
ALTER TABLE ONLY public.assessment DROP CONSTRAINT fk_assessment_mstudy_mid;
ALTER TABLE ONLY public.assessment DROP CONSTRAINT fk_assessment_mscore_mid;
ALTER TABLE ONLY public.assessment DROP CONSTRAINT fk_assessment_mreason_mid;
ALTER TABLE ONLY public.assessment DROP CONSTRAINT fk_assessment_mperformedby_mid;
ALTER TABLE ONLY public.assessment DROP CONSTRAINT fk_assessment_manimalhousing_mid;
ALTER TABLE ONLY public.assessment DROP CONSTRAINT fk_assessment_manimal_mid;
ALTER TABLE ONLY public.animal DROP CONSTRAINT fk_animal_mspecies_mid;
ALTER TABLE ONLY public.animal DROP CONSTRAINT fk_animal_msource_mid;
ALTER TABLE ONLY public.animal DROP CONSTRAINT fk_animal_msex_mid;
ALTER TABLE ONLY public.animal DROP CONSTRAINT fk_animal_mfather_mid;
ALTER TABLE ONLY public.animal DROP CONSTRAINT fk_animal_mdam_mid;
ALTER TABLE ONLY public.animal DROP CONSTRAINT fk_animal_massessmenttemplate_mid;
DROP INDEX public.users_lower_mname_unique;
DROP INDEX public.study_lower_mstudynumber_unique;
DROP INDEX public.study_group_lower_mstudygroupnumber_unique;
DROP INDEX public.species_lower_mname_unique;
DROP INDEX public.source_lower_mname_unique;
DROP INDEX public.scale_lower_mname_unique;
DROP INDEX public.reason_lower_mname_unique;
DROP INDEX public.parameter_lower_mname_unique;
DROP INDEX public.housing_lower_mname_unique;
DROP INDEX public.factor_lower_mname_unique;
DROP INDEX public.assessment_template_lower_mname_unique;
DROP INDEX public.assessment_reason_lower_mname_unique;
DROP INDEX public.animal_lower_manimalnumber_unique;
DROP INDEX public.animal_housing_lower_mname_unique;
ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
ALTER TABLE ONLY public.users DROP CONSTRAINT users_mname_key;
ALTER TABLE ONLY public.study_study_group DROP CONSTRAINT study_study_group_pkey;
ALTER TABLE ONLY public.study DROP CONSTRAINT study_pkey;
ALTER TABLE ONLY public.study DROP CONSTRAINT study_mstudynumber_key;
ALTER TABLE ONLY public.study_group DROP CONSTRAINT study_group_pkey;
ALTER TABLE ONLY public.study_group_animal DROP CONSTRAINT study_group_animal_pkey;
ALTER TABLE ONLY public.species DROP CONSTRAINT species_pkey;
ALTER TABLE ONLY public.species DROP CONSTRAINT species_mname_key;
ALTER TABLE ONLY public.source DROP CONSTRAINT source_pkey;
ALTER TABLE ONLY public.source DROP CONSTRAINT source_mname_key;
ALTER TABLE ONLY public.sex DROP CONSTRAINT sex_pkey;
ALTER TABLE ONLY public.sequence DROP CONSTRAINT sequence_pkey;
ALTER TABLE ONLY public.scale DROP CONSTRAINT scale_pkey;
ALTER TABLE ONLY public.parameter_score DROP CONSTRAINT parameter_score_pkey;
ALTER TABLE ONLY public.parameter_score_factor_scored DROP CONSTRAINT parameter_score_factor_scored_pkey;
ALTER TABLE ONLY public.parameter DROP CONSTRAINT parameter_pkey;
ALTER TABLE ONLY public.factor_scored DROP CONSTRAINT factor_scored_pkey;
ALTER TABLE ONLY public.factor DROP CONSTRAINT factor_pkey;
ALTER TABLE ONLY public.assessment_template DROP CONSTRAINT assessment_template_pkey;
ALTER TABLE ONLY public.assessment_template_parameter_factor DROP CONSTRAINT assessment_template_parameter_factor_pkey;
ALTER TABLE ONLY public.assessment_score DROP CONSTRAINT assessment_score_pkey;
ALTER TABLE ONLY public.assessment_score_parameter_score DROP CONSTRAINT assessment_score_parameter_score_pkey;
ALTER TABLE ONLY public.assessment_reason DROP CONSTRAINT assessment_reason_pkey;
ALTER TABLE ONLY public.assessment_reason DROP CONSTRAINT assessment_reason_mname_key;
ALTER TABLE ONLY public.assessment DROP CONSTRAINT assessment_pkey;
ALTER TABLE ONLY public.animal DROP CONSTRAINT animal_pkey;
ALTER TABLE ONLY public.animal DROP CONSTRAINT animal_manimalnumber_key;
ALTER TABLE ONLY public.animal_housing DROP CONSTRAINT animal_housing_pkey;
ALTER TABLE ONLY public.animal_housing DROP CONSTRAINT animal_housing_mname_key;
DROP TABLE public.users;
DROP TABLE public.study_study_group;
DROP TABLE public.study_group_animal;
DROP TABLE public.study_group;
DROP TABLE public.study;
DROP TABLE public.species;
DROP TABLE public.source;
DROP TABLE public.sex;
DROP TABLE public.sequence;
DROP TABLE public.scale;
DROP TABLE public.parameter_score_factor_scored;
DROP TABLE public.parameter_score;
DROP TABLE public.parameter;
DROP TABLE public.factor_scored;
DROP TABLE public.factor;
DROP TABLE public.assessment_template_parameter_factor;
DROP TABLE public.assessment_template;
DROP TABLE public.assessment_score_parameter_score;
DROP TABLE public.assessment_score;
DROP TABLE public.assessment_reason;
DROP TABLE public.assessment;
DROP TABLE public.animal_housing;
DROP TABLE public.animal;
DROP EXTENSION plpgsql;
DROP SCHEMA public;
--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: animal; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.animal OWNER TO postgres;

--
-- Name: animal_housing; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE animal_housing (
    mid bigint NOT NULL,
    mname character varying(255) NOT NULL
);


ALTER TABLE public.animal_housing OWNER TO postgres;

--
-- Name: assessment; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
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


ALTER TABLE public.assessment OWNER TO postgres;

--
-- Name: assessment_reason; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE assessment_reason (
    mid bigint NOT NULL,
    mname character varying(255) NOT NULL
);


ALTER TABLE public.assessment_reason OWNER TO postgres;

--
-- Name: assessment_score; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE assessment_score (
    mid bigint NOT NULL
);


ALTER TABLE public.assessment_score OWNER TO postgres;

--
-- Name: assessment_score_parameter_score; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE assessment_score_parameter_score (
    assessmentscore_mid bigint NOT NULL,
    mparametersscored_mid bigint NOT NULL
);


ALTER TABLE public.assessment_score_parameter_score OWNER TO postgres;

--
-- Name: assessment_template; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE assessment_template (
    mid bigint NOT NULL,
    mname character varying(255),
    mscale_mid bigint
);


ALTER TABLE public.assessment_template OWNER TO postgres;

--
-- Name: assessment_template_parameter_factor; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE assessment_template_parameter_factor (
    assessment_template_id bigint NOT NULL,
    parameter_id bigint NOT NULL,
    factor_id bigint NOT NULL
);


ALTER TABLE public.assessment_template_parameter_factor OWNER TO postgres;

--
-- Name: factor; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE factor (
    mid bigint NOT NULL,
    mname character varying(255)
);


ALTER TABLE public.factor OWNER TO postgres;

--
-- Name: factor_scored; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE factor_scored (
    mid bigint NOT NULL,
    misignored boolean,
    mscore integer,
    mscoringfactor_mid bigint
);


ALTER TABLE public.factor_scored OWNER TO postgres;

--
-- Name: parameter; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE parameter (
    mid bigint NOT NULL,
    mname character varying(255)
);


ALTER TABLE public.parameter OWNER TO postgres;

--
-- Name: parameter_score; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE parameter_score (
    mid bigint NOT NULL,
    maveragescore double precision,
    mcomment character varying(255),
    mparameterscored_mid bigint
);


ALTER TABLE public.parameter_score OWNER TO postgres;

--
-- Name: parameter_score_factor_scored; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE parameter_score_factor_scored (
    parameterscore_mid bigint NOT NULL,
    mscoringfactorsscored_mid bigint NOT NULL
);


ALTER TABLE public.parameter_score_factor_scored OWNER TO postgres;

--
-- Name: scale; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE scale (
    mid bigint NOT NULL,
    mmax integer,
    mmin integer,
    mname character varying(255)
);


ALTER TABLE public.scale OWNER TO postgres;

--
-- Name: sequence; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sequence (
    seq_name character varying(50) NOT NULL,
    seq_count numeric(38,0)
);


ALTER TABLE public.sequence OWNER TO postgres;

--
-- Name: sex; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sex (
    mid bigint NOT NULL,
    mname character varying(255)
);


ALTER TABLE public.sex OWNER TO postgres;

--
-- Name: source; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE source (
    mid bigint NOT NULL,
    mname character varying(255) NOT NULL
);


ALTER TABLE public.source OWNER TO postgres;

--
-- Name: species; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE species (
    mid bigint NOT NULL,
    mname character varying(255) NOT NULL,
    misdeleted boolean DEFAULT false NOT NULL
);


ALTER TABLE public.species OWNER TO postgres;

--
-- Name: study; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE study (
    mid bigint NOT NULL,
    misopen boolean DEFAULT true,
    mstudynumber character varying(255) NOT NULL
);


ALTER TABLE public.study OWNER TO postgres;

--
-- Name: study_group; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE study_group (
    mid bigint NOT NULL,
    mstudygroupnumber character varying(255)
);


ALTER TABLE public.study_group OWNER TO postgres;

--
-- Name: study_group_animal; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE study_group_animal (
    studygroup_mid bigint NOT NULL,
    manimals_mid bigint NOT NULL
);


ALTER TABLE public.study_group_animal OWNER TO postgres;

--
-- Name: study_study_group; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE study_study_group (
    study_mid bigint NOT NULL,
    mgroups_mid bigint NOT NULL
);


ALTER TABLE public.study_study_group OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE users (
    mid bigint NOT NULL,
    mname character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Data for Name: sequence; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sequence (seq_name, seq_count) FROM stdin;
SEQ_GEN	301
\.

--
-- Data for Name: sex; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY sex (mid, mname) FROM stdin;
1	Female
2	Male
\.

--
-- Name: animal_housing_mname_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY animal_housing
    ADD CONSTRAINT animal_housing_mname_key UNIQUE (mname);


--
-- Name: animal_housing_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY animal_housing
    ADD CONSTRAINT animal_housing_pkey PRIMARY KEY (mid);


--
-- Name: animal_manimalnumber_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT animal_manimalnumber_key UNIQUE (manimalnumber);


--
-- Name: animal_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT animal_pkey PRIMARY KEY (mid);


--
-- Name: assessment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT assessment_pkey PRIMARY KEY (mid);


--
-- Name: assessment_reason_mname_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY assessment_reason
    ADD CONSTRAINT assessment_reason_mname_key UNIQUE (mname);


--
-- Name: assessment_reason_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY assessment_reason
    ADD CONSTRAINT assessment_reason_pkey PRIMARY KEY (mid);


--
-- Name: assessment_score_parameter_score_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY assessment_score_parameter_score
    ADD CONSTRAINT assessment_score_parameter_score_pkey PRIMARY KEY (assessmentscore_mid, mparametersscored_mid);


--
-- Name: assessment_score_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY assessment_score
    ADD CONSTRAINT assessment_score_pkey PRIMARY KEY (mid);


--
-- Name: assessment_template_parameter_factor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY assessment_template_parameter_factor
    ADD CONSTRAINT assessment_template_parameter_factor_pkey PRIMARY KEY (assessment_template_id, parameter_id, factor_id);


--
-- Name: assessment_template_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY assessment_template
    ADD CONSTRAINT assessment_template_pkey PRIMARY KEY (mid);


--
-- Name: factor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY factor
    ADD CONSTRAINT factor_pkey PRIMARY KEY (mid);


--
-- Name: factor_scored_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY factor_scored
    ADD CONSTRAINT factor_scored_pkey PRIMARY KEY (mid);


--
-- Name: parameter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY parameter
    ADD CONSTRAINT parameter_pkey PRIMARY KEY (mid);


--
-- Name: parameter_score_factor_scored_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY parameter_score_factor_scored
    ADD CONSTRAINT parameter_score_factor_scored_pkey PRIMARY KEY (parameterscore_mid, mscoringfactorsscored_mid);


--
-- Name: parameter_score_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY parameter_score
    ADD CONSTRAINT parameter_score_pkey PRIMARY KEY (mid);


--
-- Name: scale_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY scale
    ADD CONSTRAINT scale_pkey PRIMARY KEY (mid);


--
-- Name: sequence_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sequence
    ADD CONSTRAINT sequence_pkey PRIMARY KEY (seq_name);


--
-- Name: sex_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sex
    ADD CONSTRAINT sex_pkey PRIMARY KEY (mid);


--
-- Name: source_mname_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY source
    ADD CONSTRAINT source_mname_key UNIQUE (mname);


--
-- Name: source_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY source
    ADD CONSTRAINT source_pkey PRIMARY KEY (mid);


--
-- Name: species_mname_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY species
    ADD CONSTRAINT species_mname_key UNIQUE (mname);


--
-- Name: species_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY species
    ADD CONSTRAINT species_pkey PRIMARY KEY (mid);


--
-- Name: study_group_animal_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY study_group_animal
    ADD CONSTRAINT study_group_animal_pkey PRIMARY KEY (studygroup_mid, manimals_mid);


--
-- Name: study_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY study_group
    ADD CONSTRAINT study_group_pkey PRIMARY KEY (mid);


--
-- Name: study_mstudynumber_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY study
    ADD CONSTRAINT study_mstudynumber_key UNIQUE (mstudynumber);


--
-- Name: study_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY study
    ADD CONSTRAINT study_pkey PRIMARY KEY (mid);


--
-- Name: study_study_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY study_study_group
    ADD CONSTRAINT study_study_group_pkey PRIMARY KEY (study_mid, mgroups_mid);


--
-- Name: users_mname_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_mname_key UNIQUE (mname);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (mid);


--
-- Name: animal_housing_lower_mname_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX animal_housing_lower_mname_unique ON animal_housing USING btree (lower((mname)::text));


--
-- Name: animal_lower_manimalnumber_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX animal_lower_manimalnumber_unique ON animal USING btree (lower((manimalnumber)::text));


--
-- Name: assessment_reason_lower_mname_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX assessment_reason_lower_mname_unique ON assessment_reason USING btree (lower((mname)::text));


--
-- Name: assessment_template_lower_mname_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX assessment_template_lower_mname_unique ON assessment_template USING btree (lower((mname)::text));


--
-- Name: factor_lower_mname_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX factor_lower_mname_unique ON factor USING btree (lower((mname)::text));


--
-- Name: housing_lower_mname_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX housing_lower_mname_unique ON animal_housing USING btree (lower((mname)::text));


--
-- Name: parameter_lower_mname_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX parameter_lower_mname_unique ON parameter USING btree (lower((mname)::text));


--
-- Name: reason_lower_mname_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX reason_lower_mname_unique ON assessment_reason USING btree (lower((mname)::text));


--
-- Name: scale_lower_mname_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX scale_lower_mname_unique ON scale USING btree (lower((mname)::text));


--
-- Name: source_lower_mname_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX source_lower_mname_unique ON source USING btree (lower((mname)::text));


--
-- Name: species_lower_mname_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX species_lower_mname_unique ON species USING btree (lower((mname)::text));


--
-- Name: study_group_lower_mstudygroupnumber_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX study_group_lower_mstudygroupnumber_unique ON study_group USING btree (lower((mstudygroupnumber)::text));


--
-- Name: study_lower_mstudynumber_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX study_lower_mstudynumber_unique ON study USING btree (lower((mstudynumber)::text));


--
-- Name: users_lower_mname_unique; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE UNIQUE INDEX users_lower_mname_unique ON users USING btree (lower((mname)::text));


--
-- Name: fk_animal_massessmenttemplate_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_massessmenttemplate_mid FOREIGN KEY (massessmenttemplate_mid) REFERENCES assessment_template(mid);


--
-- Name: fk_animal_mdam_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_mdam_mid FOREIGN KEY (mdam_mid) REFERENCES animal(mid);


--
-- Name: fk_animal_mfather_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_mfather_mid FOREIGN KEY (mfather_mid) REFERENCES animal(mid);


--
-- Name: fk_animal_msex_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_msex_mid FOREIGN KEY (msex_mid) REFERENCES sex(mid);


--
-- Name: fk_animal_msource_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_msource_mid FOREIGN KEY (msource_mid) REFERENCES source(mid);


--
-- Name: fk_animal_mspecies_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY animal
    ADD CONSTRAINT fk_animal_mspecies_mid FOREIGN KEY (mspecies_mid) REFERENCES species(mid);


--
-- Name: fk_assessment_manimal_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_manimal_mid FOREIGN KEY (manimal_mid) REFERENCES animal(mid);


--
-- Name: fk_assessment_manimalhousing_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_manimalhousing_mid FOREIGN KEY (manimalhousing_mid) REFERENCES animal_housing(mid);


--
-- Name: fk_assessment_mperformedby_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_mperformedby_mid FOREIGN KEY (mperformedby_mid) REFERENCES users(mid);


--
-- Name: fk_assessment_mreason_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_mreason_mid FOREIGN KEY (mreason_mid) REFERENCES assessment_reason(mid);


--
-- Name: fk_assessment_mscore_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_mscore_mid FOREIGN KEY (mscore_mid) REFERENCES assessment_score(mid);


--
-- Name: fk_assessment_mstudy_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment
    ADD CONSTRAINT fk_assessment_mstudy_mid FOREIGN KEY (mstudy_mid) REFERENCES study(mid);


--
-- Name: fk_assessment_score_parameter_score_assessmentscore_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment_score_parameter_score
    ADD CONSTRAINT fk_assessment_score_parameter_score_assessmentscore_mid FOREIGN KEY (assessmentscore_mid) REFERENCES assessment_score(mid);


--
-- Name: fk_assessment_score_parameter_score_mparametersscored_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment_score_parameter_score
    ADD CONSTRAINT fk_assessment_score_parameter_score_mparametersscored_mid FOREIGN KEY (mparametersscored_mid) REFERENCES parameter_score(mid);


--
-- Name: fk_assessment_template_mscale_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment_template
    ADD CONSTRAINT fk_assessment_template_mscale_mid FOREIGN KEY (mscale_mid) REFERENCES scale(mid);


--
-- Name: fk_assessment_template_parameter_factor_assessment_template_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment_template_parameter_factor
    ADD CONSTRAINT fk_assessment_template_parameter_factor_assessment_template_id FOREIGN KEY (assessment_template_id) REFERENCES assessment_template(mid);


--
-- Name: fk_assessment_template_parameter_factor_factor_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment_template_parameter_factor
    ADD CONSTRAINT fk_assessment_template_parameter_factor_factor_id FOREIGN KEY (factor_id) REFERENCES factor(mid);


--
-- Name: fk_assessment_template_parameter_factor_parameter_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assessment_template_parameter_factor
    ADD CONSTRAINT fk_assessment_template_parameter_factor_parameter_id FOREIGN KEY (parameter_id) REFERENCES parameter(mid);


--
-- Name: fk_factor_scored_mscoringfactor_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY factor_scored
    ADD CONSTRAINT fk_factor_scored_mscoringfactor_mid FOREIGN KEY (mscoringfactor_mid) REFERENCES factor(mid);


--
-- Name: fk_parameter_score_factor_scored_parameterscore_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY parameter_score_factor_scored
    ADD CONSTRAINT fk_parameter_score_factor_scored_parameterscore_mid FOREIGN KEY (parameterscore_mid) REFERENCES parameter_score(mid);


--
-- Name: fk_parameter_score_mparameterscored_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY parameter_score
    ADD CONSTRAINT fk_parameter_score_mparameterscored_mid FOREIGN KEY (mparameterscored_mid) REFERENCES parameter(mid);


--
-- Name: fk_study_group_animal_manimals_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY study_group_animal
    ADD CONSTRAINT fk_study_group_animal_manimals_mid FOREIGN KEY (manimals_mid) REFERENCES animal(mid);


--
-- Name: fk_study_group_animal_studygroup_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY study_group_animal
    ADD CONSTRAINT fk_study_group_animal_studygroup_mid FOREIGN KEY (studygroup_mid) REFERENCES study_group(mid);


--
-- Name: fk_study_study_group_mgroups_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY study_study_group
    ADD CONSTRAINT fk_study_study_group_mgroups_mid FOREIGN KEY (mgroups_mid) REFERENCES study_group(mid);


--
-- Name: fk_study_study_group_study_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY study_study_group
    ADD CONSTRAINT fk_study_study_group_study_mid FOREIGN KEY (study_mid) REFERENCES study(mid);


--
-- Name: parameter_score_factor_scored_mscoringfactorsscored_mid; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY parameter_score_factor_scored
    ADD CONSTRAINT parameter_score_factor_scored_mscoringfactorsscored_mid FOREIGN KEY (mscoringfactorsscored_mid) REFERENCES factor_scored(mid);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

