-- ============================================================
-- V0: Esquema inicial de User Service
-- ============================================================
--
-- Objetivo:
-- Crear la estructura inicial de la base de datos.
--
-- Estructura:
-- 1. users: almacena los usuarios.
-- 2. roles: almacena los roles.
-- 3. user_roles: relaciona usuarios y roles.
--
-- Identificadores:
-- - users.id: BIGSERIAL.
-- - roles.id: BIGSERIAL.
--
-- Relaciones:
-- - user_roles.user_id -> users.id.
-- - user_roles.role_id -> roles.id.
--
-- Esta versión representa la arquitectura inicial,
-- antes de implementar UUID y Keycloak.
-- ============================================================

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(250) NOT NULL UNIQUE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_roles FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT uq_user_roles UNIQUE (user_id, role_id)
);

-- Baseline inicial para Flyway
-- Este script refleja el estado actual de la base de datos antes de migrar a UUID

-- Tabla users (con columna Long id original)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(250) NOT NULL UNIQUE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla roles (con PK Long)
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Tabla intermedia user_roles (ManyToMany)
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_roles FOREIGN KEY (role_id) REFERENCES roles(id),
    CONSTRAINT uq_user_roles UNIQUE (user_id, role_id)
);
