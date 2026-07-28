BEGIN;

ALTER TABLE meusservicos.foto_perfil
    ADD COLUMN IF NOT EXISTS data bytea;

COMMIT;
