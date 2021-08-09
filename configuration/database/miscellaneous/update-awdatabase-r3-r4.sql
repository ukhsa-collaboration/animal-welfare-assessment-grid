CREATE TABLE awag_schema.activity_log
(
  mid bigint NOT NULL,
  maction character varying(255) NOT NULL,
  mdatetime character varying(255) NOT NULL,
  musername character varying(255) NOT NULL,
  CONSTRAINT activity_log_pkey PRIMARY KEY (mid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE awag_schema.activity_log OWNER TO awag;
