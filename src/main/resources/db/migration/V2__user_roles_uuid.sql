-- ============================================================
-- V2: Migración de relaciones user_roles a UUID
-- ============================================================
--
-- Objetivo:
-- Actualizar la relación entre usuarios y roles para utilizar
-- el nuevo identificador UUID de los usuarios.
--
-- Cambios realizados:
-- 1. Agregar user_uuid a user_roles.
-- 2. Migrar los identificadores existentes desde users.id
--    hacia users.uuid_id.
-- 3. Crear una clave foránea hacia users.uuid_id.
-- 4. Eliminar la antigua columna user_id.
--
-- Resultado:
-- La relación user_roles referencia a los usuarios mediante UUID.
--
-- IMPORTANTE:
-- Esta migración ya fue ejecutada anteriormente.
-- No debe volver a ejecutarse sobre la base de datos actual.
-- ============================================================

-- SQL histórico conservado para documentación.

SELECT 1;