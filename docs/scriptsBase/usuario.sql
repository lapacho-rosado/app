--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.4
-- Dumped by pg_dump version 9.3.4
-- Started on 2016-08-24 23:10:45

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
-- TOC entry 209 (class 1259 OID 256515)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE usuario (
    id integer NOT NULL,
    nombre character varying(50) NOT NULL,
    rol_id bigint,
    admin_id bigint,
    nombrecompleto character varying(100)
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 256518)
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usuario_id_seq OWNER TO postgres;

--
-- TOC entry 2097 (class 0 OID 0)
-- Dependencies: 210
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE usuario_id_seq OWNED BY usuario.id;


--
-- TOC entry 1977 (class 2604 OID 256535)
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario ALTER COLUMN id SET DEFAULT nextval('usuario_id_seq'::regclass);


--
-- TOC entry 2091 (class 0 OID 256515)
-- Dependencies: 209
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

/*
 Reemplazar:
 [nomUsuario] por nombre del usuario registrado en el AD
 [nomCompleto] por el nombre y apellido completo registrado en el AD
*/

INSERT INTO usuario VALUES (1, '[nomUsuario]', 2, 1, '[nomCompleto]');


--
-- TOC entry 2098 (class 0 OID 0)
-- Dependencies: 210
-- Name: usuario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('usuario_id_seq', 1, true);


--
-- TOC entry 1979 (class 2606 OID 256581)
-- Name: usuario_nombre_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_nombre_key UNIQUE (nombre);


--
-- TOC entry 1981 (class 2606 OID 256583)
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


