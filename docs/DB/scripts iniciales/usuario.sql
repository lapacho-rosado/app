-- Table: usuario

-- DROP TABLE usuario;

CREATE TABLE usuario
(
  id serial NOT NULL,
  nombre character varying(20) NOT NULL,
  persona character varying(100) NOT NULL,
  nombrecompleto character varying(100) NOT NULL,
  CONSTRAINT usuario_pkey PRIMARY KEY (id),
  CONSTRAINT usuario_nombre_key UNIQUE (nombre)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE usuario
  OWNER TO postgres;
