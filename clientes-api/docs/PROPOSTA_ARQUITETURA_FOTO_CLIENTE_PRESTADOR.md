# Proposta de Reestruturacao do Campo Foto (Cliente e Prestador)

## 1. Objetivo

Este documento descreve uma proposta completa para substituir a arquitetura atual do campo de foto para os CRUDs de cliente e prestador.

Foco:
- aumentar confiabilidade
- reduzir acoplamento e ambiguidade
- facilitar manutencao
- melhorar desempenho
- permitir evolucao para storage externo sem retrabalho

Banco alvo: PostgreSQL 16.3.

---

## 2. Diagnostico do Modelo Atual

Hoje o modelo de imagem esta concentrado em entidade/tabela generica (`imagem`) com colunas como `data` (bytea), `documento_id` e `chave_id`.

Principais problemas encontrados:

1. Vinculo indireto e ambiguo
- O relacionamento por `documento + chave_id` nao explicita claramente dono (cliente/prestador) no nivel de dominio.

2. Possibilidade de colisao logica
- Fluxos de busca por `file_name`/`uuid` sem escopo forte de ownership podem gerar retornos indevidos em cenarios de crescimento.

3. Persistencia e entrega acopladas
- O mesmo modelo concentra metadata + binario + regras de entrega (inclusive resize), dificultando evolucao.

4. Resize com efeito colateral no objeto
- Escalonamento no proprio objeto pode gerar side effects indesejados.

5. Regra de negocio fraca para foto principal
- Nao ha regra robusta de "uma foto ativa por cliente/prestador" de forma inequvoca.

6. Custo de banco e app
- Armazenar foto bruta em `bytea` para todos os cenarios pode aumentar backup, I/O e latencia.

---

## 3. Requisitos da Nova Solucao

### 3.1 Funcionais

1. Suportar foto de perfil para cliente e prestador.
2. Garantir no maximo 1 foto ativa por dono (cliente ou prestador).
3. Permitir troca de foto com historico (opcional) sem perder rastreabilidade.
4. Permitir remocao de foto.
5. Fornecer endpoint de leitura simples para front-end.

### 3.2 Nao funcionais

1. Consistencia transacional no metadata.
2. Escalabilidade para crescimento de volume.
3. Isolamento de responsabilidades (metadata x conteudo binario).
4. Facilidade de observabilidade e troubleshooting.
5. Seguranca: validacao de mime/type/tamanho e autorizacao de acesso.

---

## 4. Arquitetura Proposta

## 4.1 Direcao recomendada (A): Metadata no Postgres + Arquivo em Object Storage

### Resumo

- Banco guarda apenas metadata e chave do objeto.
- Conteudo binario fica em storage externo (S3, Azure Blob, MinIO em dev).
- API retorna URL de acesso (assinado/temporario) ou stream proxy.

### Beneficios

1. Banco menor e mais rapido.
2. Backup/restores mais leves.
3. Escalabilidade superior para imagens.
4. Menor pressao de memoria na aplicacao.

### Trade-offs

1. Dependencia de infraestrutura de storage.
2. Requer gestao de credenciais/politicas de acesso.

## 4.2 Opcao de transicao (B): Metadata + bytea no Postgres (intermediaria)

Caso seja necessario manter tudo no banco inicialmente:
- criar novo modelo de ownership forte (cliente/prestador)
- manter coluna `data bytea` temporariamente
- preparar camada de storage para migrar depois sem quebrar API

---

## 5. Modelo de Dominio Sugerido

Nova entidade: `foto_perfil`

Campos principais:
- `id`
- `owner_type` (`CLIENTE` ou `PRESTADOR`)
- `owner_id`
- `storage_provider` (`S3`, `AZURE_BLOB`, `MINIO`, `DB`)
- `object_key` (chave do arquivo no storage)
- `file_name_original`
- `mime_type`
- `size_bytes`
- `sha256`
- `largura`
- `altura`
- `ativa`
- `created_at`, `updated_at`, `created_by`, `updated_by`

Regra central:
- indice unico parcial para garantir apenas 1 foto ativa por `(owner_type, owner_id)`.

---

## 6. Contrato de API Sugerido

### Cliente

1. `POST /api/clientes/{id}/foto`
- upload/replace da foto ativa

2. `GET /api/clientes/{id}/foto`
- obtem metadata e URL/stream

3. `DELETE /api/clientes/{id}/foto`
- desativa/remocao da foto ativa

### Prestador

1. `POST /api/prestador/{id}/foto`
2. `GET /api/prestador/{id}/foto`
3. `DELETE /api/prestador/{id}/foto`

Padrao de resposta recomendado:
- nao retornar binario no JSON de listagem
- retornar metadata e referencia de download

---

## 7. Fluxo de Upload (Transacional)

1. Validar dono (cliente/prestador existe).
2. Validar arquivo (mime permitido, tamanho maximo, assinatura basica).
3. Gerar `object_key` unico.
4. Upload no storage.
5. Em transacao:
- desativar foto ativa anterior do mesmo dono
- inserir nova foto ativa
6. Em caso de falha de banco apos upload:
- executar compensacao (delete do objeto no storage) para evitar orfaos.

---

## 8. Proposta de DDL (PostgreSQL 16.3)

## 8.1 DDL principal (recomendada)

```sql
-- Schema assume: meusservicos

-- 1) Tabela de metadata de foto
CREATE TABLE IF NOT EXISTS meusservicos.foto_perfil (
	id                  bigserial PRIMARY KEY,
	owner_type          varchar(20) NOT NULL,
	owner_id            bigint NOT NULL,

	storage_provider    varchar(30) NOT NULL,
	object_key          varchar(500) NOT NULL,

	file_name_original  varchar(255),
	mime_type           varchar(100) NOT NULL,
	size_bytes          bigint NOT NULL,
	sha256              varchar(64),
	largura             integer,
	altura              integer,

	ativa               boolean NOT NULL DEFAULT true,

	created_at          timestamp without time zone NOT NULL DEFAULT now(),
	updated_at          timestamp without time zone NOT NULL DEFAULT now(),
	created_by          varchar(120),
	updated_by          varchar(120),

	CONSTRAINT ck_foto_perfil_size_positive
		CHECK (size_bytes > 0),

	CONSTRAINT ck_foto_perfil_owner_type
		CHECK (owner_type IN ('CLIENTE', 'PRESTADOR')),
	CONSTRAINT ck_foto_perfil_storage_provider
		CHECK (storage_provider IN ('S3', 'AZURE_BLOB', 'MINIO', 'DB')),
	CONSTRAINT ck_foto_perfil_mime
		CHECK (mime_type IN ('image/jpeg', 'image/png', 'image/webp'))
);

-- 3) Unicidade de chave de objeto
CREATE UNIQUE INDEX IF NOT EXISTS ux_foto_perfil_object_key
	ON meusservicos.foto_perfil (object_key);

-- 4) Apenas 1 foto ativa por dono
CREATE UNIQUE INDEX IF NOT EXISTS ux_foto_perfil_owner_ativa
	ON meusservicos.foto_perfil (owner_type, owner_id)
	WHERE ativa = true;

-- 5) Indices de consulta
CREATE INDEX IF NOT EXISTS ix_foto_perfil_owner
	ON meusservicos.foto_perfil (owner_type, owner_id);

CREATE INDEX IF NOT EXISTS ix_foto_perfil_owner_ativa
	ON meusservicos.foto_perfil (owner_type, owner_id, ativa);

CREATE INDEX IF NOT EXISTS ix_foto_perfil_created_at
	ON meusservicos.foto_perfil (created_at);

-- 6) Trigger para manter updated_at
CREATE OR REPLACE FUNCTION meusservicos.fn_set_updated_at_foto_perfil()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
	NEW.updated_at := now();
	RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trg_set_updated_at_foto_perfil ON meusservicos.foto_perfil;

CREATE TRIGGER trg_set_updated_at_foto_perfil
BEFORE UPDATE ON meusservicos.foto_perfil
FOR EACH ROW
EXECUTE FUNCTION meusservicos.fn_set_updated_at_foto_perfil();
```

## 8.2 Variante com binario no banco (transicao)

Se for necessario manter foto no Postgres por algum tempo:

```sql
ALTER TABLE meusservicos.foto_perfil
ADD COLUMN IF NOT EXISTS data bytea;
```

Nesse caso:
- `storage_provider = 'DB'`
- `object_key` pode receber chave logica interna
- migracao para storage externo pode ser feita por lote sem mudar contrato externo da API

---

## 9. Script de Migracao de Dados Legados (imagem -> foto_perfil)

Premissas do legado:
- `imagem.documento_id` referencia tabela `documento`.
- Em `documento.descricao`: `C` = Cliente, `P` = Prestador.
- `imagem.chave_id` representa id do dono.

Script base (ajustavel apos validacao de consistencia):

```sql
INSERT INTO meusservicos.foto_perfil (
	owner_type,
	owner_id,
	storage_provider,
	object_key,
	file_name_original,
	mime_type,
	size_bytes,
	sha256,
	ativa,
	created_at,
	updated_at,
	created_by,
	updated_by
)
SELECT
	CASE d.descricao
		WHEN 'C' THEN 'CLIENTE'
		WHEN 'P' THEN 'PRESTADOR'
	END AS owner_type,
	i.chave_id::bigint AS owner_id,
	'DB' AS storage_provider,
	CONCAT('legacy/', i.id) AS object_key,
	i.original_file_name,
	COALESCE(i.file_type, 'image/jpeg') AS mime_type,
	COALESCE(i.size, 1) AS size_bytes,
	NULL AS sha256,
	true AS ativa,
	COALESCE(i.created_date, now()) AS created_at,
	COALESCE(i.updated_date, now()) AS updated_at,
	i.created_by,
	i.updated_by
FROM meusservicos.imagem i
JOIN meusservicos.documento d ON d.id = i.documento_id
WHERE i.chave_id IS NOT NULL
  AND d.descricao IN ('C', 'P');
```

Observacao importante:
- Se existir mais de uma imagem por dono no legado, sera necessario definir criterio de foto ativa (ex.: mais recente por `updated_date`/`id`) e marcar as demais como `ativa = false`.

---

## 10. Plano de Implementacao por Fases

## Fase 1 - Infra e modelo

1. Criar DDL de `foto_perfil`.
2. Criar camada de dominio (`PhotoProfile`, repositorio, service).
3. Criar abstração de storage (`PhotoStoragePort`).

## Fase 2 - API nova

1. Implementar endpoints em cliente e prestador para foto.
2. Implementar DTO de resposta sem binario inline.
3. Validacoes de arquivo + autorizacao.

## Fase 3 - Migracao

1. Executar migracao legado -> nova tabela.
2. Validar consistencia por amostragem e contagem.
3. Habilitar leitura preferencial da nova arquitetura.

## Fase 4 - Desativacao legado

1. Congelar escrita na tabela antiga `imagem`.
2. Monitorar por periodo definido.
3. Remover codigo legado.

---

## 11. Proposta de Testes JUnit

## 11.1 Unitarios

1. Upload substitui foto ativa anterior do mesmo owner.
2. Upload rejeita mime invalido e tamanho acima do limite.
3. Falha no storage nao persiste metadata.
4. Falha no banco apos upload aciona compensacao (delete no storage).

## 11.2 Integracao (SpringBootTest + Testcontainers Postgres 16)

1. Upload/Get/Delete de cliente ponta a ponta.
2. Upload/Get/Delete de prestador ponta a ponta.
3. Conflito de concorrencia para mesmo owner.
4. Validacao do indice unico parcial (uma ativa por owner).

## 11.3 WebMvc

1. Contratos HTTP dos novos endpoints.
2. Erros 404 para owner inexistente.
3. Erros 400 para arquivo invalido.
4. Regras de autorizacao por perfil.

## 11.4 Regressao

1. CRUD de cliente/prestador sem foto deve continuar estavel.
2. Fluxos legados (se temporariamente habilitados) devem manter compatibilidade ate o cutover.

---

## 12. Riscos e Mitigacoes

1. Duplicidade historica de imagem por dono
- Mitigacao: regra de desambiguacao na migracao e log de conflitos.

2. Orfaos entre banco e storage
- Mitigacao: compensacao transacional e job periodico de reconciliacao.

3. Impacto no front-end
- Mitigacao: contrato claro de `fotoUrl`/endpoint estavel e rollout com feature flag.

4. Volume de dados legado
- Mitigacao: migracao em lotes, com janela controlada e metricas.

---

## 13. Recomendacao Final

Recomenda-se adotar a arquitetura A (metadata no Postgres + object storage) com implantacao por fases e camada de compatibilidade temporaria.

Essa abordagem resolve os problemas atuais de confiabilidade e prepara o sistema para escala, mantendo rastreabilidade e controle de negocio para foto de cliente e prestador.

