-- Table: aplicacion

-- DROP TABLE aplicacion;

CREATE TABLE aplicacion
(
  id serial NOT NULL,
  areareferente character varying(100),
  descripcion character varying(250),
  nombre character varying(100) NOT NULL,
  url character varying(100) NOT NULL,
  rutaimagen character varying(500),
  CONSTRAINT aplicacion_pkey PRIMARY KEY (id),
  CONSTRAINT aplicacion_nombre_key UNIQUE (nombre),
  CONSTRAINT aplicacion_url_key UNIQUE (url)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE aplicacion
  OWNER TO postgres;
