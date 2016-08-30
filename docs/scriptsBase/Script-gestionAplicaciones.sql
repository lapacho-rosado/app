---
--- Creación de tablas
---
CREATE TABLE aplicacion (
    id integer NOT NULL,
    areareferente character varying(100),
    descripcion character varying(250),
    nombre character varying(100) NOT NULL,
    url character varying(100) NOT NULL,
    rutaimagen character varying(500)
);

CREATE TABLE aplicacionesxusuarios (
    usuario_fk bigint NOT NULL,
    aplicacion_fk bigint NOT NULL
);

CREATE TABLE usuario (
    id integer NOT NULL,
    nombre character varying(20) NOT NULL,
    persona character varying(100) NOT NULL,
    nombrecompleto character varying(100) NOT NULL
);

---
---Creación de secuencias
---

CREATE SEQUENCE aplicacion_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

---
--- Inserts
--- (Modificar el insert en la tabla 'usuario' según corresponda)
---

INSERT INTO aplicacion VALUES (1, 'CI - Desarrollo de Aplicaciones', 'Aplicación para la gestión de todas las aplicaciones de la SAyDS', 'Gestión de Aplicaciones', '/gestionAplicaciones', 'AppIn.jpg');
INSERT INTO aplicacion VALUES (2, 'CI - Desarrollo de Aplicaciones', 'Aplicación de prueba para sonsumir SSO', 'App Prueba Cliente', '/appJsfClient', 'AppVeg.jpg');
INSERT INTO aplicacion VALUES (3, 'CI - Desarrollo de Aplicaciones', 'Aplicación de administración del servicio de Centros Poblados', 'Gestión Territorial', '/gestionTerritorial', 'AppIn.jpg');
INSERT INTO aplicacion VALUES (4, 'CI - Desarrollo de Aplicaciones', 'Aplicación de administración del servicio de Especies Vegetales', 'Especies Vegetales', '/especiesForestales', 'AppVeg.jpg');
INSERT INTO aplicacion VALUES (5, 'Presupuesto', 'Aplicación para la planificación y control preupuestarios', 'Gestión Presupuestaria', '/gestionPresupuestaria', 'AppIn.jpg');
INSERT INTO aplicacion VALUES (6, 'CI - Desarrollo de Aplicaciones', 'Aplicación de administración del Servicio de Gestión de Trámites', 'Gestión de Trámites', '/gestionTramites', 'AppTrm.jpg');
INSERT INTO aplicacion VALUES (7, 'CI - Desarrollo de Aplicaciones', 'Aplicación de administración del servicio de Gestión de Personas', 'Gestión de Personas', '/gestionPersonas', 'AppPrs.jpg');
INSERT INTO aplicacion VALUES (8, 'DPyRA', 'Interface web para usuarios internos de gestión de efluentes líquidos', 'gestionEfluentesLiquidos-webInt', '/gestionEfluentesLiquidos-webInt', 'superniÃ±a.jpg');


--- Modificar esta sentencia
INSERT INTO usuario VALUES (1, '[usuario]', '[iniciales]', '[nombre completo]');
---

INSERT INTO aplicacionesxusuarios VALUES (1, 1);
INSERT INTO aplicacionesxusuarios VALUES (1, 2);
INSERT INTO aplicacionesxusuarios VALUES (1, 3);
INSERT INTO aplicacionesxusuarios VALUES (1, 4);
INSERT INTO aplicacionesxusuarios VALUES (1, 5);
INSERT INTO aplicacionesxusuarios VALUES (1, 6);
INSERT INTO aplicacionesxusuarios VALUES (1, 7);
INSERT INTO aplicacionesxusuarios VALUES (1, 8);

---
--- Constraints
---

ALTER TABLE ONLY aplicacion
    ADD CONSTRAINT aplicacion_nombre_key UNIQUE (nombre);

ALTER TABLE ONLY aplicacion
    ADD CONSTRAINT aplicacion_pkey PRIMARY KEY (id);

ALTER TABLE ONLY aplicacion
    ADD CONSTRAINT aplicacion_url_key UNIQUE (url);

ALTER TABLE ONLY aplicacionesxusuarios
    ADD CONSTRAINT aplicacionesxusuarios_pkey PRIMARY KEY (usuario_fk, aplicacion_fk);

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_nombre_key UNIQUE (nombre);

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);

ALTER TABLE ONLY aplicacionesxusuarios
    ADD CONSTRAINT fk_aplicacionesxusuarios_aplicacion_fk FOREIGN KEY (aplicacion_fk) REFERENCES aplicacion(id);

ALTER TABLE ONLY aplicacionesxusuarios
    ADD CONSTRAINT fk_aplicacionesxusuarios_usuario_fk FOREIGN KEY (usuario_fk) REFERENCES usuario(id);
