-- ============================================================
-- V3: Establecimiento de clave foránea hacia roles
-- ============================================================
--
-- Objetivo:
-- Garantizar la integridad referencial entre user_roles y roles.
--
-- Cambios realizados:
-- 1. Crear una clave foránea desde user_roles.role_id
--    hacia roles.id.
--
-- Resultado:
-- La relación entre usuarios y roles queda protegida
-- mediante una restricción de integridad referencial.
--
-- IMPORTANTE:
-- Esta migración pertenece a la arquitectura anterior,
-- donde la API administraba los roles localmente.
-- Actualmente, los roles son administrados por Keycloak.
--
-- La tabla roles y la tabla user_roles fueron eliminadas.
-- Esta migración no debe volver a ejecutarse.
-- ============================================================

-- SQL histórico conservado para documentación.

SELECT 1;