-- V2: Delegate role management to Keycloak.
--
-- Changes already applied manually in PostgreSQL:
-- - Added keycloak_id to users.
-- - Added UNIQUE constraint to keycloak_id.
-- - Dropped user_roles.
-- - Dropped roles.
-- - Removed the foreign key relationship with roles.
--
-- No additional schema changes are required.

SELECT 1;