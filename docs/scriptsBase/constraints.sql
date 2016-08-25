/*
 CONSTRAINTS usuario
 */


ALTER TABLE ONLY usuario
    ADD CONSTRAINT fk_usuario_adminentidad_id FOREIGN KEY (admin_id) REFERENCES adminentidad(id);


ALTER TABLE ONLY usuario
    ADD CONSTRAINT fk_usuario_rol_id FOREIGN KEY (rol_id) REFERENCES rol(id);


/*
 CONSTRAINTS rol
 */

ALTER TABLE ONLY rol
    ADD CONSTRAINT fk_rol_adminentidad_id FOREIGN KEY (admin_id) REFERENCES adminentidad(id);


/*
 CONSTRAINTS adminentidad
 */


ALTER TABLE ONLY adminentidad
    ADD CONSTRAINT fk_adminentidad_usalta_id FOREIGN KEY (usalta_id) REFERENCES usuario(id);


ALTER TABLE ONLY adminentidad
    ADD CONSTRAINT fk_adminentidad_usbaja_id FOREIGN KEY (usbaja_id) REFERENCES usuario(id);


ALTER TABLE ONLY adminentidad
    ADD CONSTRAINT fk_adminentidad_usmodif_id FOREIGN KEY (usmodif_id) REFERENCES usuario(id);


