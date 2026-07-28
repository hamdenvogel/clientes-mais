-- Hotfix para ambientes ja existentes sem as colunas do novo fluxo de confirmacao.
-- PostgreSQL 9.6+

BEGIN;

ALTER TABLE meusservicos.usuario
  ADD COLUMN IF NOT EXISTS email_confirmado boolean;

ALTER TABLE meusservicos.usuario
  ADD COLUMN IF NOT EXISTS email_confirmado_em timestamp;

ALTER TABLE meusservicos.usuario
  ALTER COLUMN email_confirmado SET DEFAULT false;

-- Mantem usuarios antigos com acesso, como no comportamento anterior ao fluxo de confirmacao.
UPDATE meusservicos.usuario
SET email_confirmado = true,
    email_confirmado_em = COALESCE(email_confirmado_em, NOW())
WHERE email_confirmado IS NULL;

-- Caso a coluna tenha sido criada com default false e tenha marcado legados como false,
-- libera contas antigas sem sobrescrever quem ja confirmou em data valida.
UPDATE meusservicos.usuario
SET email_confirmado = true,
    email_confirmado_em = COALESCE(email_confirmado_em, NOW())
WHERE email_confirmado = false
  AND email_confirmado_em IS NULL;

ALTER TABLE meusservicos.usuario
  ALTER COLUMN email_confirmado SET NOT NULL;

COMMIT;

