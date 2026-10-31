-- V2: Delegate role management to Keycloak.
--
-- Changes were already applied manually in PostgreSQL:
-- - Added keycloak_id to users.
-- - Added UNIQUE constraint to keycloak_id.
-- - Dropped user_roles.
-- - Dropped roles.
--
-- No additional schema changes are required.

SELECT 1;