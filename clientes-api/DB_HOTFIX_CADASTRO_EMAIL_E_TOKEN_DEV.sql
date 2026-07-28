BEGIN;

-- Usuario: colunas de confirmacao de e-mail
ALTER TABLE meusservicos.usuario
  ADD COLUMN IF NOT EXISTS email_confirmado boolean;

ALTER TABLE meusservicos.usuario
  ADD COLUMN IF NOT EXISTS email_confirmado_em timestamp;

ALTER TABLE meusservicos.usuario
  ALTER COLUMN email_confirmado SET DEFAULT false;

-- Backfill legado: mantem contas antigas habilitadas
UPDATE meusservicos.usuario
SET email_confirmado = true,
    email_confirmado_em = COALESCE(email_confirmado_em, NOW())
WHERE email_confirmado IS NULL;

-- Caso algum legado tenha ficado false sem data, libera no DEV
UPDATE meusservicos.usuario
SET email_confirmado = true,
    email_confirmado_em = COALESCE(email_confirmado_em, NOW())
WHERE email_confirmado = false
  AND email_confirmado_em IS NULL;

ALTER TABLE meusservicos.usuario
  ALTER COLUMN email_confirmado SET NOT NULL;

-- Tabela de tokens de cadastro
CREATE TABLE IF NOT EXISTS meusservicos.registration_token (
  id bigserial NOT NULL,
  usuario_id int8 NOT NULL,
  token_hash varchar(128) NOT NULL,
  purpose varchar(40) NOT NULL,
  expires_at timestamp NOT NULL,
  used_at timestamp NULL,
  created_at timestamp NOT NULL,
  CONSTRAINT registration_token_pkey PRIMARY KEY (id)
);

-- FK idempotente
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_constraint c
    JOIN pg_namespace n ON n.oid = c.connamespace
    WHERE c.conname = 'fk_registration_token_usuario'
      AND n.nspname = 'meusservicos'
  ) THEN
    ALTER TABLE meusservicos.registration_token
      ADD CONSTRAINT fk_registration_token_usuario
      FOREIGN KEY (usuario_id) REFERENCES meusservicos.usuario(id);
  END IF;
END $$;

-- Indices para consultas de token aberto
CREATE INDEX IF NOT EXISTS idx_registration_token_lookup_open
  ON meusservicos.registration_token (token_hash, purpose)
  WHERE used_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_registration_token_user_open
  ON meusservicos.registration_token (usuario_id, purpose)
  WHERE used_at IS NULL;

COMMIT;

