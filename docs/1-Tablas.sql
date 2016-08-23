-- Tablas

CREATE TABLE usuario
(
  id serial NOT NULL,
  nombre character varying(20) NOT NULL,
  persona character varying(100) NOT NULL,
  nombrecompleto character varying(100) NOT NULL,
  CONSTRAINT usuario_pkey PRIMARY KEY (id),
  CONSTRAINT usuario_nombre_key UNIQUE (nombre)
)

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

CREATE TABLE aplicacionesxusuarios
(
  usuario_fk bigint NOT NULL,
  aplicacion_fk bigint NOT NULL,
  CONSTRAINT aplicacionesxusuarios_pkey PRIMARY KEY (usuario_fk, aplicacion_fk),
  CONSTRAINT fk_aplicacionesxusuarios_aplicacion_fk FOREIGN KEY (aplicacion_fk)
      REFERENCES aplicacion (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_aplicacionesxusuarios_usuario_fk FOREIGN KEY (usuario_fk)
      REFERENCES usuario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)



