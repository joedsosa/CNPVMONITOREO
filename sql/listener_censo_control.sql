-- ============================================================
-- Esquema de la base de CONTROL del listener: listener_censo_control
-- Reconstruido a partir del uso real en:
--   ConfigCorteDAO, ControlCorteDAO, CensistaCodigoDAO
-- ============================================================

CREATE DATABASE IF NOT EXISTS listener_censo_control
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE listener_censo_control;

-- ------------------------------------------------------------
-- config_cortes: define los cortes programados (hora, activo/inactivo)
-- Usada por ConfigCorteDAO.obtenerCortesActivos()
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS config_cortes (
    id           INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100)      NOT NULL,
    activo       TINYINT(1)        NOT NULL DEFAULT 1,
    hora_corte   TIME              NOT NULL,
    descripcion  VARCHAR(255)      NULL
);

-- ------------------------------------------------------------
-- ejecucion_corte: una fila por cada corrida del corte
-- Usada por ControlCorteDAO.crearEjecucion / finalizarEjecucion / existeEjecucionHoy
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ejecucion_corte (
    id                INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_config_corte   INT UNSIGNED NOT NULL,
    fecha_corte       DATE         NOT NULL,
    hora_inicio       DATETIME     NOT NULL,
    hora_fin          DATETIME     NULL,
    estado            VARCHAR(30)  NOT NULL DEFAULT 'EN_PROCESO',
    total_censistas   INT UNSIGNED NULL,
    total_procesados  INT UNSIGNED NULL,
    total_error       INT UNSIGNED NULL,
    CONSTRAINT fk_ejecucion_config
        FOREIGN KEY (id_config_corte) REFERENCES config_cortes (id),
    INDEX idx_ejecucion_config_fecha (id_config_corte, fecha_corte)
);

-- ------------------------------------------------------------
-- control_censista_corte: progreso por censista/segmento dentro de una ejecucion
-- Usada por ControlCorteDAO.censistaFinalizado / marcarProcesando / marcarFinalizado / marcarError
-- El UNIQUE KEY es obligatorio: marcarProcesando hace INSERT ... ON DUPLICATE KEY UPDATE
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS control_censista_corte (
    id             INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_ejecucion   INT UNSIGNED NOT NULL,
    depto          VARCHAR(2)   NOT NULL,
    muni           VARCHAR(2)   NOT NULL,
    zona           INT UNSIGNED NOT NULL,
    sector         INT UNSIGNED NOT NULL,
    segmento       VARCHAR(9)   NOT NULL,
    censista       VARCHAR(20)  NOT NULL,
    estado         VARCHAR(20)  NOT NULL DEFAULT 'PENDIENTE',
    hora_inicio    DATETIME     NULL,
    hora_fin       DATETIME     NULL,
    mensaje_error  TEXT         NULL,
    CONSTRAINT fk_control_ejecucion
        FOREIGN KEY (id_ejecucion) REFERENCES ejecucion_corte (id),
    UNIQUE KEY uq_control_censista (id_ejecucion, depto, muni, zona, sector, segmento, censista),
    INDEX idx_control_estado (id_ejecucion, estado)
);

-- ------------------------------------------------------------
-- censista_codigo: asigna un codigo secuencial propio (Cddmmnnnn) por censista de origen
-- Usada por CensistaCodigoDAO.buscarCodigo / generarSiguienteCodigo / insertarCodigo
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS censista_codigo (
    id                INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    depto             VARCHAR(2)   NOT NULL,
    muni              VARCHAR(2)   NOT NULL,
    censista_origen   VARCHAR(20)  NOT NULL,
    codigo_censista   VARCHAR(10)  NOT NULL,
    creado_en         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_censista_origen (depto, muni, censista_origen),
    UNIQUE KEY uq_codigo_censista (codigo_censista),
    INDEX idx_censista_depto_muni (depto, muni)
);
