-- Datos de relleno (basura/random) SOLO para demo visual.
-- El listener NO escribe estas columnas todavia (faltan fuente/reglas reales, ver conversacion).
USE censo_monitoreo;

ALTER TABLE cob_censista_productividad
    ADD COLUMN IF NOT EXISTS fecha_hora_sincronizacion DATETIME NULL,
    ADD COLUMN IF NOT EXISTS usuario_inactivo TINYINT NULL;

ALTER TABLE cob_censista_por_vivienda
    ADD COLUMN IF NOT EXISTS fecha_hora_sincronizacion DATETIME NULL,
    ADD COLUMN IF NOT EXISTS usuario_inactivo TINYINT NULL;

UPDATE cob_censista_productividad
SET estructuras_asignadas = estructuras_trabajadas + FLOOR(RAND()*10),
    estado = FLOOR(1 + RAND()*4),
    fecha_hora_sincronizacion = NOW() - INTERVAL FLOOR(RAND()*48) HOUR;

UPDATE cob_censista_productividad
SET estructuras_pendientes = GREATEST(estructuras_asignadas - estructuras_trabajadas, 0),
    pct_avance = ROUND(100 * estructuras_trabajadas / NULLIF(estructuras_asignadas, 0), 2),
    usuario_inactivo = CASE WHEN fecha_hora_sincronizacion < NOW() - INTERVAL 24 HOUR THEN 1 ELSE 0 END;

UPDATE cob_censista_por_vivienda
SET viviendas_transformadas = FLOOR(RAND()*3),
    fecha_hora_sincronizacion = NOW() - INTERVAL FLOOR(RAND()*48) HOUR;

UPDATE cob_censista_por_vivienda
SET usuario_inactivo = CASE WHEN fecha_hora_sincronizacion < NOW() - INTERVAL 24 HOUR THEN 1 ELSE 0 END;

SELECT 'DONE' AS resultado;
