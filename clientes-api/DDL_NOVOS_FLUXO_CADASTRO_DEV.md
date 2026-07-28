# DDLs novos - fluxo de cadastro com confirmacao por e-mail (DEV)

Data: 2026-04-19  
Escopo: banco `meusservicos` (PostgreSQL)

## Objetivo
Este documento consolida os DDLs novos relacionados as ultimas implementacoes de cadastro com confirmacao por e-mail:

1. Novas colunas na tabela `meusservicos.usuario`:
   - `email_confirmado`
   - `email_confirmado_em`
2. Nova tabela `meusservicos.registration_token`
3. Indices para consulta de token aberto
4. Backfill para manter usuarios legados com acesso (comportamento pre-existente)

## Script unico (idempotente) para DEV

> Execute este bloco inteiro no banco de desenvolvimento.

```sql
BEGIN;

-- 1) Usuario: colunas de confirmacao de e-mail
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

-- 2) Tabela de tokens de cadastro
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

-- 3) Indices para as consultas do RegistrationTokenRepository
CREATE INDEX IF NOT EXISTS idx_registration_token_lookup_open
  ON meusservicos.registration_token (token_hash, purpose)
  WHERE used_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_registration_token_user_open
  ON meusservicos.registration_token (usuario_id, purpose)
  WHERE used_at IS NULL;

COMMIT;
```

## Validacao rapida apos aplicar

```sql
-- Colunas em usuario
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_schema = 'meusservicos'
  AND table_name = 'usuario'
  AND column_name IN ('email_confirmado', 'email_confirmado_em');

-- Tabela e estrutura basica
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_schema = 'meusservicos'
  AND table_name = 'registration_token'
ORDER BY ordinal_position;

-- FK
SELECT conname
FROM pg_constraint c
JOIN pg_namespace n ON n.oid = c.connamespace
WHERE n.nspname = 'meusservicos'
  AND conname = 'fk_registration_token_usuario';

-- Indices
SELECT indexname
FROM pg_indexes
WHERE schemaname = 'meusservicos'
  AND tablename = 'registration_token'
  AND indexname IN (
    'idx_registration_token_lookup_open',
    'idx_registration_token_user_open'
  );
```

## Observacao importante
- O backfill acima foi pensado para DEV e libera usuarios antigos automaticamente.
- Para homolog/producao, se quiser, eu te entrego uma versao mais restritiva (sem confirmar todos os legados automaticamente).

