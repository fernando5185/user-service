-- Crear FK hacia roles solo si no existe
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fkh8ciramu9cc9q3qcqiv4ue8a6'
    ) THEN
        ALTER TABLE user_roles
        ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES roles(id);
    END IF;
END$$;
