BEGIN;

ALTER TABLE meusservicos.usuario
    ADD COLUMN IF NOT EXISTS cpf VARCHAR(14),
    ADD COLUMN IF NOT EXISTS telefone VARCHAR(20),
    ADD COLUMN IF NOT EXISTS endereco VARCHAR(250),
    ADD COLUMN IF NOT EXISTS cidade VARCHAR(100),
    ADD COLUMN IF NOT EXISTS uf VARCHAR(2),
    ADD COLUMN IF NOT EXISTS cep VARCHAR(8),
    ADD COLUMN IF NOT EXISTS ativo BOOLEAN NOT NULL DEFAULT TRUE;

-- Garante valor unico para registros antigos sem CPF,
-- permitindo aplicar NOT NULL + UNIQUE com seguranca.
UPDATE meusservicos.usuario
SET cpf = 'USR' || LPAD(id::text, 11, '0')
WHERE cpf IS NULL OR BTRIM(cpf) = '';

ALTER TABLE meusservicos.usuario
    ALTER COLUMN cpf SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uk_usuario_cpf'
          AND conrelid = 'meusservicos.usuario'::regclass
    ) THEN
        ALTER TABLE meusservicos.usuario
            ADD CONSTRAINT uk_usuario_cpf UNIQUE (cpf);
    END IF;
END
$$;

COMMIT;
