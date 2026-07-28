# Clientes Mais

Monorepo do **Clientes Mais** — gestão de clientes, prestadores e serviços.

Repositório: [hamdenvogel/clientes-mais](https://github.com/hamdenvogel/clientes-mais)

## Estrutura

| Pasta | Descrição |
|-------|-----------|
| `clientes-api/` | Backend Spring Boot 3 / Java 21 |
| `clientes-app/` | Frontend Angular 9 (legado; será substituído no roadmap) |
| `ROADMAP_EVOLUCAO_CLIENTES_MAIS.md` | Plano de evolução |

## Segurança — segredos

**Não versionamos** senhas, API keys, JWT, reCAPTCHA secret, `.env`, etc.

| Arquivo | Uso |
|---------|-----|
| `clientes-api/.env.example` | Modelo → copie para `.env` e preencha |
| `clientes-api/.env` | **Local apenas** (gitignored) |
| `clientes-app/src/environments/environment.example.ts` | Modelo das chaves do front |
| `application-*.properties` | Só referências `${ENV}` — sem valores secretos |

```powershell
cd clientes-api
copy .env.example .env
# edite .env com valores LOCAIS
docker compose up --build -d
```

Se alguma chave já vazou em commit antigo: **rotacione** (DB, JWT, reCAPTCHA, Rabbit, Grafana).

Guia Docker: [`clientes-api/DOCKER.md`](clientes-api/DOCKER.md)

## Frontend local

```powershell
cd clientes-app
npm install
# preencha recaptchaSiteKey em src/environments/environment.ts (local)
npm start
```

## CI / imagens

- `.github/workflows/ci-api.yml` — build + testes + imagem
- `.github/workflows/publish-ghcr.yml` — `ghcr.io/hamdenvogel/clientes-mais/clientes-api` (amd64 + arm64 via `Dockerfile.runtime`)

```bash
docker pull ghcr.io/hamdenvogel/clientes-mais/clientes-api:latest
```

## Roadmap

Ver [ROADMAP_EVOLUCAO_CLIENTES_MAIS.md](ROADMAP_EVOLUCAO_CLIENTES_MAIS.md).

## Contribuindo

Ver [CONTRIBUTING.md](CONTRIBUTING.md).
