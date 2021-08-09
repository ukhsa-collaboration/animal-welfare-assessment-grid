--
-- Template data as per Appendix 1 in the user guide
--


SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;


SET search_path = awag_schema, pg_catalog;

COPY scale (mid, mmax, mmin, mname) FROM stdin;
454	10	1	1-10
\.

--
-- Data for Name: assessment_template; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--


COPY assessment_template (mid, mname, mscale_mid) FROM stdin;
456	Animal Welfare	454
\.


--
-- Data for Name: parameter; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY parameter (mid, mname) FROM stdin;
446	Physical
448	Behavioural/psychological
450	Environmental
452	Experimental/clinical event
\.


--
-- Data for Name: factor; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY factor (mid, mname) FROM stdin;
404	General condition (weight-loss, condition score)
406	Clinical assessment
408	Activity level, mobility
410	Presence of injury
412	Not eating/ drinking
414	Stereotypy, self-harming, unusual grooming
416	Response to catching event
418	Hierarchy upset/ dispute, aggression/ bullying
420	Alopecia score
422	Use of enrichment
424	Aversion to 'normal' events
426	Housing
428	Group size
430	Provision of 3D enrichment
432	Provision of manipulable enrichment
434	Contingent events
436	Restraint
438	Sedation
440	Planned Licensed procedure
442	Veterinary/ Husbandry procedure
444	Change in daily routine
\.


--
-- Data for Name: assessment_template_parameter_factor; Type: TABLE DATA; Schema: awag_schema; Owner: awag
--

COPY assessment_template_parameter_factor (assessment_template_id, parameter_id, factor_id) FROM stdin;
456	446	404
456	446	406
456	446	408
456	446	410
456	446	412
456	448	414
456	448	416
456	448	418
456	448	420
456	448	422
456	448	424
456	450	426
456	450	428
456	450	430
456	450	432
456	450	434
456	452	436
456	452	438
456	452	440
456	452	442
456	452	444
\.

--
--
