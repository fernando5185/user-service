-- Crear columna solo si no existe
ALTER TABLE user_roles ADD COLUMN IF NOT EXISTS user_uuid UUID;

-- Poblar la columna
UPDATE user_roles ur
SET user_uuid = u.uuid_id
FROM users u
WHERE ur.user_id = u.id;

-- Crear FK solo si no existe
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_user_roles_users'
    ) THEN
        ALTER TABLE user_roles
        ADD CONSTRAINT fk_user_roles_users FOREIGN KEY (user_uuid) REFERENCES users(uuid_id);
    END IF;
END$$;

-- Eliminar columna antigua solo si existe
ALTER TABLE user_roles DROP COLUMN IF EXISTS user_id;
