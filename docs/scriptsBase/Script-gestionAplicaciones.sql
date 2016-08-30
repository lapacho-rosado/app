---
--- Creaci�n de tablas
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
---Creaci�n de secuencias
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
--- (Modificar el insert en la tabla 'usuario' seg�n corresponda)
---

INSERT INTO aplicacion VALUES (1, 'CI - Desarrollo de Aplicaciones', 'Aplicaci�n para la gesti�n de todas las aplicaciones de la SAyDS', 'Gesti�n de Aplicaciones', '/gestionAplicaciones', 'AppIn.jpg');
INSERT INTO aplicacion VALUES (2, 'CI - Desarrollo de Aplicaciones', 'Aplicaci�n de prueba para sonsumir SSO', 'App Prueba Cliente', '/appJsfClient', 'AppVeg.jpg');
INSERT INTO aplicacion VALUES (3, 'CI - Desarrollo de Aplicaciones', 'Aplicaci�n de administraci�n del servicio de Centros Poblados', 'Gesti�n Territorial', '/gestionTerritorial', 'AppIn.jpg');
INSERT INTO aplicacion VALUES (4, 'CI - Desarrollo de Aplicaciones', 'Aplicaci�n de administraci�n del servicio de Especies Vegetales', 'Especies Vegetales', '/especiesForestales', 'AppVeg.jpg');
INSERT INTO aplicacion VALUES (5, 'Presupuesto', 'Aplicaci�n para la planificaci�n y control preupuestarios', 'Gesti�n Presupuestaria', '/gestionPresupuestaria', 'AppIn.jpg');
INSERT INTO aplicacion VALUES (6, 'CI - Desarrollo de Aplicaciones', 'Aplicaci�n de administraci�n del Servicio de Gesti�n de Tr�mites', 'Gesti�n de Tr�mites', '/gestionTramites', 'AppTrm.jpg');
INSERT INTO aplicacion VALUES (7, 'CI - Desarrollo de Aplicaciones', 'Aplicaci�n de administraci�n del servicio de Gesti�n de Personas', 'Gesti�n de Personas', '/gestionPersonas', 'AppPrs.jpg');
INSERT INTO aplicacion VALUES (8, 'DPyRA', 'Interface web para usuarios internos de gesti�n de efluentes l�quidos', 'gestionEfluentesLiquidos-webInt', '/gestionEfluentesLiquidos-webInt', 'superniña.jpg');


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
