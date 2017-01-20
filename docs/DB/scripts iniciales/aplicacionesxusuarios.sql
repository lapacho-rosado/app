-- Table: aplicacionesxusuarios

-- DROP TABLE aplicacionesxusuarios;

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
WITH (
  OIDS=FALSE
);
ALTER TABLE aplicacionesxusuarios
  OWNER TO postgres;
