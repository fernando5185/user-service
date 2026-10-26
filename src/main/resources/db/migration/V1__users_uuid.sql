-- Agregar columna UUID solo si no existe
ALTER TABLE users ADD COLUMN IF NOT EXISTS uuid_id UUID;

-- Poblar la columna con valores aleatorios si está vacía
UPDATE users SET uuid_id = gen_random_uuid() WHERE uuid_id IS NULL;

-- Crear PK sobre uuid_id solo si no existe
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'users_pkey'
    ) THEN
        ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (uuid_id);
    END IF;
END$$;
