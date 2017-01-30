
SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;


CREATE TABLE rol (
    id integer NOT NULL,
    nombre character varying(50) NOT NULL
);



CREATE SEQUENCE rol_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




ALTER SEQUENCE rol_id_seq OWNED BY rol.id;


ALTER TABLE ONLY rol ALTER COLUMN id SET DEFAULT nextval('rol_id_seq'::regclass);



INSERT INTO rol (id, nombre) VALUES (1, 'Administrador');
INSERT INTO rol (id, nombre) VALUES (2, 'Usuario');


SELECT pg_catalog.setval('rol_id_seq', 2, true);


ALTER TABLE ONLY rol
    ADD CONSTRAINT rol_nombre_key UNIQUE (nombre);


ALTER TABLE ONLY rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id);
