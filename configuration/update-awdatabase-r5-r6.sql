--
-- Update from AWAG release v5 to v6
-- Import tablesa
--
-- Release v6.0 changes
--

CREATE TABLE awag_schema.import_header
(
  importheaderid bigint NOT NULL,
  date_import DATE NOT NULL DEFAULT CURRENT_DATE,
  activity_log_action character varying(255) NULL,
  user_name character varying(255) NOT NULL,
  CONSTRAINT importheader_pkey PRIMARY KEY(importheaderid)
);

CREATE TABLE awag_schema.import_factor
(
	importfactorid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    factor_name character varying(255) NOT NULL,
    factorid bigint NULL,
    line_number bigint DEFAULT 0 NULL,
    factor_description TEXT NULL,
    CONSTRAINT import_factor_pkey PRIMARY KEY (importfactorid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_parameter
(
	importparameterid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    parameter_name character varying(255) NOT NULL,
    parameterid bigint NULL,
    line_number bigint DEFAULT 0 NULL,
    CONSTRAINT import_parameter_pkey PRIMARY KEY (importparameterid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_scale
(
	importscaleid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    scale_name character varying(255) NOT NULL,
    max integer DEFAULT 0 NULL,
    min integer DEFAULT 0 NULL,
    scaleid bigint NULL,
    line_number bigint DEFAULT 0 NULL,
    CONSTRAINT import_scale_pkey PRIMARY KEY (importscaleid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_template
(
	importtemplateid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    assessment_template_name character varying(255) NOT NULL,
    assessment_templateid bigint NULL,
    parameter_count bigint NULL,
    scale_name character varying(255) NULL,
	scaleid bigint NULL,
    line_number bigint DEFAULT 0 NULL,
    CONSTRAINT import_template_pkey PRIMARY KEY (importtemplateid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_template_parameter
(
    import_template_parameter_id bigint NOT NULL,
	importtemplateid bigint NOT NULL,
    parameter_name character varying(255) NOT NULL,
    parameterid bigint NULL,
    factors TEXT NULL,
    CONSTRAINT import_template_parameter_pkey PRIMARY KEY (import_template_parameter_id),
    CONSTRAINT import_template_fkey FOREIGN KEY (importtemplateid) REFERENCES awag_schema.import_template(importtemplateid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_template_parameter_factor
(
	import_template_parameter_factor_id bigint NOT NULL,
    import_template_parameter_id bigint NOT NULL,
    factor_name character varying(255) NOT NULL,
    factorid bigint NULL,
    CONSTRAINT import_template_parameter_factor_pkey PRIMARY KEY (import_template_parameter_factor_id),
    CONSTRAINT import_template_parameter_fkey FOREIGN KEY (import_template_parameter_id) REFERENCES awag_schema.import_template_parameter(import_template_parameter_id) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_source
(
    importsourceid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    source_name character varying(255) NOT NULL,
    sourceid bigint NULL,
    line_number bigint DEFAULT 0 NULL,
    CONSTRAINT import_source_pkey PRIMARY KEY (importsourceid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_animal_housing
(
    importanimalhousingid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    animal_housing_name character varying(255) NOT NULL,
    animalhousingid bigint NULL,
    line_number bigint DEFAULT 0 NULL,
    CONSTRAINT import_animal_housing_pkey PRIMARY KEY (importanimalhousingid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_species
(
    importspeciesid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    species_name character varying(255) NOT NULL,
    speciesid bigint NULL,
    line_number bigint DEFAULT 0 NULL,
    CONSTRAINT import_species_pkey PRIMARY KEY (importspeciesid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_animal
(
    importanimalid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    animal_number character varying(255) NOT NULL,
    date_birth character varying(255) NULL,
    sex character varying(50) NULL,
    species_name character varying(255) NULL,
    source_name character varying(255) NULL,
    dam_animal_name character varying(255) NULL,
    father_animal_name character varying(255) NULL,
    assessment_template_name character varying(255) NULL,
    isalive boolean DEFAULT FALSE,
    animalid bigint NULL,
    sexid bigint NULL,
    speciesid bigint NULL,
    sourceid bigint NULL,
    damanimalid bigint NULL,
    fatheranimalid bigint NULL,
    dam_importanimalid bigint NULL,
    father_importanimalid bigint NULL,
    assessmenttemplateid bigint NULL,
    line_number bigint DEFAULT 0 NULL,
    CONSTRAINT import_animal_pkey PRIMARY KEY (importanimalid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_study
(
    importstudyid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    studynumber character varying(255) NOT NULL,
    studynumberid bigint NULL,
    isstudyopen boolean DEFAULT FALSE NULL,
    studystudygroupnumbers TEXT NULL,
    line_number bigint DEFAULT 0 NULL,
    CONSTRAINT import_study_pkey PRIMARY KEY (importstudyid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_study_study_group
(
    importstudystudygroupid bigint NOT NULL,
    importstudyid bigint NOT NULL,
    studystudygroupnumber character varying(255) NULL,
    studystudygroupnumberid bigint NULL,
    CONSTRAINT import_study_study_group_pkey PRIMARY KEY (importstudystudygroupid),
    CONSTRAINT import_study_fkey FOREIGN KEY (importstudyid) REFERENCES awag_schema.import_study(importstudyid) ON DELETE CASCADE--,
);

CREATE TABLE awag_schema.import_study_group
(
    importstudygroupid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    studygroupnumber character varying(255) NULL,
    studygroupnumberid bigint NULL,
    studygroupanimalnumbers TEXT NULL,
    line_number bigint DEFAULT 0 NULL,
    CONSTRAINT import_study_group_pkey PRIMARY KEY (importstudygroupid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_study_group_animal
(
    importstudygroupanimalid bigint NOT NULL,
    importstudygroupid bigint NOT NULL,
    studygroupanimalnumber character varying(255) NULL,
    studygroupanimalnumberid bigint NULL,
    CONSTRAINT import_study_group_animal_pkey PRIMARY KEY (importstudygroupanimalid),
    CONSTRAINT import_study_group_fkey FOREIGN KEY (importstudygroupid) REFERENCES awag_schema.import_study_group(importstudygroupid) ON DELETE CASCADE--,
);

CREATE TABLE awag_schema.import_assessment_reason
(
    importassessment_reasonid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    assessment_reason_name character varying(255) NOT NULL,
    assessmentreasonid bigint NULL,
    line_number bigint DEFAULT 0 NULL,
    CONSTRAINT import_assessment_reason_pkey PRIMARY KEY (importassessment_reasonid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);


CREATE TABLE awag_schema.import_assessment
(
    importassessmentid bigint NOT NULL,
    importheaderid bigint NOT NULL,
    date_assessment character varying(255) NULL,
    iscomplete boolean DEFAULT FALSE NULL,
    animal_number character varying(255) NULL,
    animal_housing_name character varying(255) NULL,
    assessmentreason_name character varying(255) NULL,
    animalnumberid bigint NULL,
    animalhousingid bigint NULL,
    assessmentreasonid bigint NULL,
    importassessmenttemplateid bigint NOT NULL,
    performed_by_user character varying(255) NULL,
    performed_by_user_id bigint NULL,
    line_number bigint DEFAULT 0 NULL,
    CONSTRAINT import_assessment_pkey PRIMARY KEY (importassessmentid),
    CONSTRAINT import_header_fkey FOREIGN KEY (importheaderid) REFERENCES awag_schema.import_header(importheaderid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.import_assessment_parameter_factor_scores
(
    importassessmentparameterfactorscoresid bigint NOT NULL,
    importassessmentid bigint NOT NULL,
    factor_scores text NULL,
    parameter_comments text NULL,
    parameter_number bigint NULL,
    CONSTRAINT import_assessment_parameter_factor_scores_pkey PRIMARY KEY (importassessmentparameterfactorscoresid),
    CONSTRAINT import_assessment_fkey FOREIGN KEY (importassessmentid) REFERENCES awag_schema.import_assessment(importassessmentid) ON DELETE CASCADE
);

CREATE TABLE awag_schema.assessment_template_parameter
(
    assessment_template_id bigint NOT NULL,
    parameter_id bigint NOT NULL,
    clockwise_display_order_number bigint DEFAULT 0 NOT NULL,
    CONSTRAINT pk_assessment_template_parameter PRIMARY KEY (assessment_template_id, parameter_id),
    CONSTRAINT fk_assessment_template_parameter_assessment_template_id
        FOREIGN KEY (assessment_template_id) REFERENCES awag_schema.assessment_template (mid) MATCH SIMPLE ON
    UPDATE NO ACTION ON
    DELETE NO ACTION,
    CONSTRAINT fk_assessment_template_parameter_parameter_id
        FOREIGN KEY (parameter_id) REFERENCES awag_schema.parameter (mid) MATCH SIMPLE ON
        UPDATE NO ACTION ON
        DELETE NO ACTION
);

ALTER TABLE awag_schema.factor ADD factor_description TEXT NULL;


ALTER TABLE awag_schema.import_header OWNER to awag;
ALTER TABLE awag_schema.import_source OWNER to awag;
ALTER TABLE awag_schema.import_assessment_reason OWNER to awag;
ALTER TABLE awag_schema.import_animal_housing OWNER to awag;
ALTER TABLE awag_schema.import_species OWNER to awag;
ALTER TABLE awag_schema.import_animal OWNER to awag;
ALTER TABLE awag_schema.import_assessment OWNER to awag;
ALTER TABLE awag_schema.import_study OWNER to awag;
ALTER TABLE awag_schema.import_study_study_group OWNER to awag;
ALTER TABLE awag_schema.import_study_group OWNER to awag;
ALTER TABLE awag_schema.import_study_group_animal OWNER to awag;
ALTER TABLE awag_schema.import_assessment OWNER to awag;
ALTER TABLE awag_schema.import_assessment_parameter_factor_scores OWNER to awag;

-- Fix the display order for older imports
INSERT INTO awag_schema.assessment_template_parameter(assessment_template_id, parameter_id)
SELECT DISTINCT assessment_template_id,
                parameter_id
FROM awag_schema.assessment_template_parameter_factor;

-- Add allow zero scores
ALTER TABLE awag_schema.assessment_template ADD is_allow_zero_scores BOOLEAN DEFAULT TRUE NOT NULL;

