-- Agegado de la nueva columna en para guardar el rol

ALTER TABLE usuario ADD COLUMN rol_id bigint;


-- Eliminar el campo "persona"

ALTER TABLE usuario DROP COLUMN persona;