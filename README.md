# Clientes Mais

Monorepo do **Clientes Mais** — gestão de clientes, prestadores e serviços.

Repositório: [hamdenvogel/clientes-mais](https://github.com/hamdenvogel/clientes-mais)

## Estrutura

| Pasta | Descrição |
|-------|-----------|
| `clientes-api/` | Backend Spring Boot 3 / Java 21 |
| `clientes-app/` | Frontend Angular 9 (legado; será substituído no roadmap) |
| `ROADMAP_EVOLUCAO_CLIENTES_MAIS.md` | Plano de evolução (Docker, Rabbit, K8s, hexagonal, MF, etc.) |

## Backend local (Docker Compose)

```powershell
cd clientes-api
copy .env.example .env
docker compose up --build -d
```

Guia completo: [`clientes-api/DOCKER.md`](clientes-api/DOCKER.md)

## CI / imagens

- **CI:** `.github/workflows/ci-api.yml` — build, testes e imagem Docker
- **GHCR:** `.github/workflows/publish-ghcr.yml` — publica `ghcr.io/hamdenvogel/clientes-mais/clientes-api` (**amd64** + **arm64**)

Após o primeiro publish na `main`:

```bash
docker pull ghcr.io/hamdenvogel/clientes-mais/clientes-api:latest
```

> Pacotes GHCR podem exigir permissão “public” em *Packages* do repositório, ou login com token.

## Roadmap

Ver [ROADMAP_EVOLUCAO_CLIENTES_MAIS.md](ROADMAP_EVOLUCAO_CLIENTES_MAIS.md).
