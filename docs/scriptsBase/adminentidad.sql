--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.4
-- Started on 2016-08-24 23:44:36

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 174 (class 1259 OID 256395)
-- Name: adminentidad; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE adminentidad (
    id integer NOT NULL,
    fechaalta timestamp without time zone,
    fechabaja timestamp without time zone,
    fechamodif timestamp without time zone,
    habilitado boolean,
    usalta_id bigint,
    usbaja_id bigint,
    usmodif_id bigint
);


ALTER TABLE public.adminentidad OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 256398)
-- Name: adminentidad_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE adminentidad_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.adminentidad_id_seq OWNER TO postgres;

--
-- TOC entry 2096 (class 0 OID 0)
-- Dependencies: 175
-- Name: adminentidad_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE adminentidad_id_seq OWNED BY adminentidad.id;


--
-- TOC entry 1977 (class 2604 OID 256521)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY adminentidad ALTER COLUMN id SET DEFAULT nextval('adminentidad_id_seq'::regclass);


--
-- TOC entry 2090 (class 0 OID 256395)
-- Dependencies: 174
-- Data for Name: adminentidad; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO adminentidad VALUES (1, '2016-03-17 12:51:33.436', NULL, NULL, true, 1, NULL, NULL);
INSERT INTO adminentidad VALUES (2, '2016-03-17 13:50:39.082', NULL, NULL, true, 1, NULL, NULL);
INSERT INTO adminentidad VALUES (3, '2016-03-17 14:25:07.301', NULL, NULL, true, 1, NULL, NULL);


--
-- TOC entry 2097 (class 0 OID 0)
-- Dependencies: 175
-- Name: adminentidad_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('adminentidad_id_seq', 3, true);


--
-- TOC entry 1979 (class 2606 OID 256539)
-- Name: adminentidad_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY adminentidad
    ADD CONSTRAINT adminentidad_pkey PRIMARY KEY (id);

