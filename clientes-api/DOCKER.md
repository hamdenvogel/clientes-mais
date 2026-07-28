# Docker Compose local — Clientes API (Itens 1 e 2 do roadmap)

Stack: **API Spring Boot** + **Postgres** + **RabbitMQ (Management)** + **Prometheus** + **Grafana**.

## Pré-requisitos

- Docker Desktop (Windows) com Compose v2
- Portas livres: `8080`, `5435`, `5672`, `15672`, `9090`, `3000`

## Subir tudo

```powershell
cd C:\Hamden\Sistemas\Backend\clientes\clientes-api
copy .env.example .env
# Edite .env e troque todos os CHANGE_ME_* por valores locais
docker compose up --build -d
```

**Importante:** o Compose **exige** `.env` preenchido (senhas/JWT). Não há senha padrão no repositório.

Primeira build da API pode demorar (Maven baixa dependências na imagem).

## URLs

| Serviço | URL |
|---------|-----|
| API health | http://localhost:8080/actuator/health |
| API Prometheus | http://localhost:8080/actuator/prometheus |
| RabbitMQ Management | http://localhost:15672 (user/pass do `.env`) |
| Prometheus | http://localhost:9090 |
| Grafana | http://localhost:3000 (user/pass do `.env`) |
| Postgres (host) | `localhost:5435` — credenciais do `.env` |

No Grafana: datasource **Prometheus** já provisionado; dashboard **Clientes API — JVM** na pasta *Clientes Mais*.

---

## Item 2 — Pedidos + RabbitMQ (lab)

Fluxo assíncrono em `/api/pedidos/async` (não substitui o CRUD `/api/pedidos`).

| Peça | Nome |
|------|------|
| TopicExchange | `clientes.pedidos.exchange` |
| Fila durável | `clientes.pedidos.criado.queue` |
| Routing key | `pedidos.criado` |
| DLX / DLQ | `clientes.pedidos.dlx` / `clientes.pedidos.criado.dlq` |
| Retry | 3 tentativas (backoff 1s → 2s → …) e depois DLQ |

### Criar pedido (sucesso)

```powershell
Set-Content $env:TEMP\pedido-ok.json '{"descricao":"Pedido lab OK","total":99.90}'
curl.exe -X POST http://localhost:8080/api/pedidos/async `
  -H "Content-Type: application/json" `
  --data-binary "@$env:TEMP\pedido-ok.json"
```

### Simular falha → retry → DLQ

```powershell
Set-Content $env:TEMP\pedido-fail.json '{"descricao":"Pedido lab FALHA","total":10.00}'
curl.exe -X POST http://localhost:8080/api/pedidos/async/simular-falha `
  -H "Content-Type: application/json" `
  --data-binary "@$env:TEMP\pedido-fail.json"
```

Aguarde ~10s (retries) e confira:

```powershell
curl.exe http://localhost:8080/api/pedidos/async/status
curl.exe http://localhost:8080/api/pedidos/async/dlq
```

No RabbitMQ Management (`Queues`): veja `clientes.pedidos.criado.queue` e `clientes.pedidos.criado.dlq`.

---

## Só infra (API no IDE)

```powershell
docker compose up -d postgres rabbitmq prometheus grafana
```

Ajuste o `application-dev.properties` (ou env) para apontar ao Postgres do Compose (`localhost:5435`) e Rabbit em `localhost:5672`.  
**Nota:** Prometheus scrapa `api:8080` (hostname do Compose). Com a API só no host, edite `docker/prometheus/prometheus.yml` para `host.docker.internal:8080` ou suba a API no Compose.

## Parar / limpar

```powershell
docker compose down
# apaga volumes (banco/filas/métricas):
docker compose down -v
```

## Perfil Spring

Container da API usa `SPRING_PROFILES_ACTIVE=docker` → `application-docker.properties`.
