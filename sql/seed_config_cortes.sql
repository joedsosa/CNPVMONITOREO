-- ============================================================
-- Datos semilla para listener_censo_control.config_cortes
-- Los IDs deben quedar 1 y 2 porque Main.java tiene hardcodeado
-- corteService.ejecutarCorte(1) para la prueba manual
-- (comentario en Main.java: ID 1 = CORTE_MEDIODIA, ID 2 = CORTE_NOCHE)
-- ============================================================

USE listener_censo_control;

INSERT INTO config_cortes (id, nombre, activo, hora_corte, descripcion) VALUES
    (1, 'CORTE_MEDIODIA', 1, '12:00:00', 'Corte de mediodia'),
    (2, 'CORTE_NOCHE',     1, '20:00:00', 'Corte de la noche')
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    activo = VALUES(activo),
    hora_corte = VALUES(hora_corte),
    descripcion = VALUES(descripcion);
