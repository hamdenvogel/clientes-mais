# Roadmap de Evolução — Clientes Mais

**Data:** 2026-07-28 (atualizado)  
**Escopo:** `clientes-api` (Spring Boot) + `clientes-app` (Angular legado)  
**Repositório:** [hamdenvogel/clientes-mais](https://github.com/hamdenvogel/clientes-mais)  
**Objetivo:** evolução técnica para produto e currículo (complementar FilePack / HV Assistant)

---

## Contexto

| Projeto | Papel no portfólio |
|---------|-------------------|
| **FilePack API** | CI + Docker + Cloud Run (PaaS) |
| **HV Assistant** | Spring AI + RAG + Cloud Run |
| **Clientes Mais** | Mensageria, observabilidade, Kubernetes/ARM, hexagonal/DDD, **microsserviços + microfrontends** alinhados por domínio |

**Diretórios atuais**

- Backend: `clientes-api` (hoje monólito → depois serviços por domínio)
- Frontend legado: `clientes-app` (Angular 9)
- Frontend novo: a criar (Angular LTS + Nx + microfrontends)

---

## Lista oficial (fechada)

Ordem de execução acordada:

| # | Item | Status |
|---|------|--------|
| **1** | Docker multi-stage + Docker Compose (API + Postgres + RabbitMQ + Prometheus + Grafana) + métricas Actuator | ✅ concluída (local) |
| **2** | Pedidos + RabbitMQ (`RabbitTemplate`, `@RabbitListener`, TopicExchange, fila durável, retry, DLQ + endpoint de falha) | ✅ concluída (local) |
| **3** | GitHub Actions → publicação no GitHub Container Registry (imagem **amd64** e **arm64**) | ✅ concluída |
| **4** | Primeiro lab Kubernetes: **K3d no Windows** (manifests, probes, limits; RabbitMQ via Helm) | ✅ concluída |
| **5** | **Hexagonal + DDD no projeto inteiro** (`clientes-api` — todos os bounded contexts; modular monolith) | ⏳ pendente |
| **6** | Deploy público econômico: **Oracle Cloud Free Tier** + VM ARM + **K3s** + Helm + RabbitMQ + Spring Boot (scripts + README) — **já com a arquitetura do item 5** | ⏳ pendente |
| **7** | **Front Angular LTS novo** = paridade total do legado + **signals** + **microfrontends** (ex.: cliente, prestador, …) + **SSR** (Nx + Storybook como base) | ⏳ pendente |
| **8** | **Microsserviços no backend**, alinhados 1:1 com os microfrontends (ex.: `cliente-api` ↔ `mfe-cliente`, `prestador-api` ↔ `mfe-prestador`, …) + Rabbit entre serviços onde fizer sentido | ⏳ pendente |
| **9** | *(Opcional)* Polish Storybook / federação avançada / gateway (API Gateway ou BFF) | ⏳ pendente |

**Motivo da ordem 5 → 6:** o primeiro deploy público (K3s/OCI) sobe **depois** da hexagonal + DDD no modular monolith.

**Motivo da ordem 7 → 8:** microfrontends primeiro (ou em paralelo controlado); microsserviços **extraem** os bounded contexts do item 5, pareados com cada remote do item 7.

---

## Alinhamento domínio ↔ front ↔ back (meta)

| Domínio | Microfrontend (item 7) | Microsserviço (item 8) |
|---------|------------------------|-------------------------|
| Auth / identidade | shell (ou `mfe-auth`) | `auth-api` (ou módulo no gateway) |
| Cliente | `mfe-cliente` | `cliente-api` |
| Prestador | `mfe-prestador` | `prestador-api` |
| Serviço prestado | `mfe-servico-prestado` | `servico-prestado-api` |
| Pedido / pacote | `mfe-pedido` (ou pacote) | `pedido-api` |
| … | … | … |

Comunicação típica: HTTP síncrono (REST) + **RabbitMQ** para eventos assíncronos (pedido criado, notificação, etc.).

---

## Detalhamento por item

### 1 — Docker + Compose + observabilidade

- Dockerfile multi-stage da `clientes-api` (padrão próximo ao FilePack/HV Assistant)
- `docker-compose` local: API, Postgres, RabbitMQ (Management), Prometheus, Grafana
- Spring Boot Actuator → métricas scrapadas pelo Prometheus; dashboards no Grafana
- Health checks básicos da aplicação
- **Como usar:** ver [`clientes-api/DOCKER.md`](clientes-api/DOCKER.md)

**Ambiente:** desenvolvimento local com Docker Compose

**Artefatos**

- `clientes-api/Dockerfile`
- `clientes-api/docker-compose.yml`
- `clientes-api/.env.example`
- `clientes-api/docker/` (postgres init, prometheus, grafana)
- Perfil Spring `docker` (`application-docker.properties`)
- Dependência `micrometer-registry-prometheus` + liberação de `/actuator/health|info|prometheus` no Security

---

### 2 — Pedidos + RabbitMQ

- Publicação com `RabbitTemplate`
- Consumo com `@RabbitListener`
- `TopicExchange`, fila durável, retry e DLQ
- Endpoint para criar pedidos
- Endpoint para simular falha e testar a DLQ
- No item 2 o publisher/consumer ainda podem viver no **mesmo** monólito; no item 8 o consumo pode migrar para outro serviço
- **Como usar:** ver [`clientes-api/DOCKER.md`](clientes-api/DOCKER.md) (seção Item 2)

**Artefatos**

- Pacote `io.github.hvogel.clientes.messaging`
- Endpoints: `POST /api/pedidos/async`, `POST /api/pedidos/async/simular-falha`, `GET /api/pedidos/async/status`, `GET /api/pedidos/async/dlq`
- Exchange `clientes.pedidos.exchange` + fila `clientes.pedidos.criado.queue` + DLQ `clientes.pedidos.criado.dlq`

---

### 3 — CI/CD e registro de imagens

- GitHub Actions no monorepo [hamdenvogel/clientes-mais](https://github.com/hamdenvogel/clientes-mais)
- Publicação no **GitHub Container Registry (GHCR)**
- Imagem multi-arch: **amd64** e **arm64** (adequada também para Oracle Cloud Ampere)
- No item 8: pipeline/imagem **por serviço**

**Artefatos**

- `.github/workflows/ci-api.yml` — build + testes + `docker build` da `clientes-api`
- `.github/workflows/publish-ghcr.yml` — Maven no runner + `Dockerfile.runtime` via Buildx → `ghcr.io/hamdenvogel/clientes-mais/clientes-api` (`latest`, `sha-*`, tags `v*`) — **amd64/arm64** sem compilar Maven sob QEMU
- `clientes-api/Dockerfile.runtime` — imagem enxuta a partir do JAR já gerado
- `README.md` na raiz do monorepo

**Imagem**

```text
ghcr.io/hamdenvogel/clientes-mais/clientes-api:latest
```

---

### 4 — K3d (Windows)

- Manifests Kubernetes: Namespace, Secret, Deployment, Service, Ingress
- Health checks: startup, readiness, liveness
- Limites de CPU e memória
- HPA opcional
- Instalação do RabbitMQ com Helm + volume persistente
- Serve para validar o lab K8s **antes** da refatoração completa; o deploy “oficial” de currículo na nuvem fica no item 6 (e evolui no item 8)

**Ambiente:** primeiro laboratório Kubernetes no Windows (K3d)

**Como usar:** ver [`infra/k3d/README.md`](infra/k3d/README.md)

**Artefatos**

- `infra/k8s/` — namespace, configmap, postgres (+PVC), api deployment/service/ingress, HPA opcional, kustomization
- `infra/k8s/secret.example.yaml` — modelo (sem valores reais)
- `infra/k3d/create-cluster.ps1`, `create-secret.ps1`, `deploy.ps1`, `destroy-cluster.ps1`
- `infra/k3d/values-rabbitmq.yaml` — Helm Bitnami RabbitMQ + persistence

---

### 5 — Hexagonal + DDD (projeto inteiro)

- Arquitetura hexagonal + DDD em **toda** a `clientes-api`
- Bounded contexts claros (prontos para virar serviços no item 8)
- Preferência nesta fase: **modular monolith** (pacotes/módulos por contexto)
- Migração gradual (strangler), contexto a contexto, mantendo contrato da API estável para o front
- Pedido pode ser o primeiro contexto a migrar (já alinhado ao item 2)
- **Critério para avançar ao item 6:** backend com hexagonal/DDD concluído (ou em estado publicável acordado) e imagem GHCR atualizada

---

### 6 — K3s na Oracle Cloud

- Scripts de instalação do K3s e deploy
- Após publicar a imagem no GHCR (**versão já com arquitetura do item 5**)
- Stack: Oracle Cloud Free Tier + VM ARM + K3s + Helm + RabbitMQ + Spring Boot
- README com passo a passo completo
- Nesta fase ainda pode ser **um** deploy do modular monolith; o item 8 multiplica os Deployments

**Ambiente:** deploy público econômico — **já com hexagonal + DDD**

---

### 7 — Front Angular LTS (paridade + MF + signals + SSR)

**Meta:** substituir 100% o `clientes-app` legado.

Inclui:

- Angular **LTS mais recente**
- **Signals** desde o início (estado de UI)
- **Microfrontends** (ex.: um remote para cliente, outro para prestador, etc.)
- **SSR** (prioritariamente no shell / rotas públicas)
- Base com **Nx** (+ Storybook na lib de UI)
- Paridade total de telas/fluxos com o legado

**Ordem interna sugerida do item 7**

1. Scaffold Nx + shell (Angular LTS, signals) + auth + layout  
2. Remotes um a um até paridade com o legado  
3. Storybook em `libs/ui`  
4. SSR no shell (por último dentro do item 7)

O front legado (`clientes-app`) só é desligado após validação de paridade.

Cada remote deve conversar, no longo prazo, com o microsserviço do **mesmo domínio** (item 8). Enquanto o item 8 não existir, os remotes apontam para o modular monolith (item 5/6).

---

### 8 — Microsserviços (backend) alinhados aos microfrontends

**Meta:** extrair do modular monolith (item 5) um serviço por domínio, pareado com o MFE correspondente.

Inclui:

- Um deploy Spring Boot por contexto (ex.: `cliente-api`, `prestador-api`, …)
- Contratos REST estáveis; eventos via **RabbitMQ** entre serviços
- Compose / K3d / K3s atualizados para **N** serviços (+ gateway opcional)
- CI/CD e imagem GHCR por serviço
- Auth compartilhada (JWT/JWKS) entre serviços e shell/MFEs

**Ordem interna sugerida do item 8**

1. Extrair o 1º serviço “fácil” (ex.: Pedido ou Prestador) mantendo o restante no monólito  
2. Apontar o MFE correspondente para o novo serviço  
3. Repetir domínio a domínio até cobrir o mapa da tabela acima  

---

### 9 — Opcional

- Polish Storybook / Module Federation avançado
- API Gateway ou BFF na frente dos microsserviços

---

## Onde sobe o quê (conta free)

**Destino do deploy público:** **Oracle Cloud Always Free** (não Google Cloud) — VM **ARM (Ampere)** + **K3s** + Helm + RabbitMQ.

| Ambiente | Papel | O que sobe | Custo |
|----------|--------|------------|--------|
| **Docker Compose** (PC) | Desenvolvimento / lab completo | Stack **completa**: API(s), Postgres, RabbitMQ, Prometheus, Grafana, front/MFEs | **Free** (máquina local) |
| **K3d** (Windows) | Primeiro lab Kubernetes | Stack **completa** (mesmos conceitos do Compose, em K8s local) | **Free** (máquina local) |
| **Oracle Cloud Always Free** + K3s | **Demo pública** de currículo | Ver ondas abaixo | **Free** (Always Free), se não sair do shape free |

### Ondas na Oracle (Always Free)

| Onda | Conteúdo | Expectativa na VM free |
|------|----------|-------------------------|
| **1ª (item 6)** | Modular monolith hexagonal + Rabbit + Postgres + front | **Stack pública completa** desta fase — viável no Always Free |
| **2ª (itens 7–8)** | Microfrontends + vários microsserviços + Rabbit (+ obs.) | Lab **completo** continua no Compose/K3d; na OCI sobe **subconjunto demonstrável** (ex.: shell + 2–3 MFEs + 2–3 APIs + Rabbit) **ou** tudo com limites bem baixos de memória |

**Regra explícita**

- **Compose / K3d** = stack completa (tudo da lista que couber na máquina).  
- **OCI Always Free** = demo pública free; 1ª onda “cheia” do monólito; 2ª onda com cuidado / subconjunto se N serviços não couberem confortáveis na RAM.

Recomendações Always Free: budget alert baixo (ex. US$ 1–5); só shapes/recursos Always Free; não ligar VM/shape pago sem querer.

---

## Resumo dos ambientes

| Fase | Ambiente |
|------|----------|
| Desenvolvimento local (**stack completa**) | Docker Compose |
| Primeiro lab Kubernetes (**stack completa**) | K3d no Windows |
| Refatoração de arquitetura | Hexagonal + DDD (modular monolith) |
| Deploy público 1ª onda (**demo free “cheia” do monólito**) | OCI Always Free + VM ARM + K3s — pós–item 5 |
| Deploy público 2ª onda (**demo free**; full no PC) | Mesmo cluster OCI — MFEs + microsserviços (subconjunto ou packing cuidadoso) |

---

## O que fica de fora por enquanto

- Big-bang: criar todos os microsserviços **antes** do modular monolith hexagonal
- Migrar o Angular 9 com `ng update` em série (preferência: recriar no LTS e migrar a ideia/fontes)
- Subir o deploy OCI (item 6) **antes** da hexagonal/DDD (item 5)
- Assumir que a Oracle free aguenta **ilimitados** JVMs 24×7 sem ajuste de memória / subconjunto

---

## Próximo passo imediato

Item 4 (K3d) entregue. Iniciar o **item 5**: Hexagonal + DDD no projeto inteiro.

---

## Histórico de decisões

| Data | Decisão |
|------|----------|
| 2026-07-26 | Roadmap priorizado: plataforma antes de arquitetura profunda e front novo |
| 2026-07-26 | Hexagonal/DDD no **projeto inteiro** |
| 2026-07-26 | Front novo com **paridade total** do legado |
| 2026-07-27 | Front inclui Angular LTS + signals + microfrontends + SSR |
| 2026-07-27 | Documento físico criado neste arquivo |
| 2026-07-27 | Hexagonal/DDD = item 5; K3s/OCI = item 6 (deploy já com nova arquitetura) |
| 2026-07-27 | **Microsserviços promovidos a item 8**, alinhados 1:1 com microfrontends (cliente, prestador, etc.) |
| 2026-07-27 | Deploy: **Compose/K3d = stack completa**; **OCI Always Free = demo pública** (1ª onda monólito cheio; 2ª onda subconjunto ou packing cuidadoso) — **não GCP** |
| 2026-07-27 | **Item 1 iniciado:** Dockerfile, Compose (Postgres/Rabbit/Prom/Grafana), Actuator Prometheus, `DOCKER.md` |
| 2026-07-27 | **Item 2 concluído:** RabbitMQ TopicExchange + retry + DLQ; endpoints `/api/pedidos/async*` |
| 2026-07-28 | Monorepo publicado em [hamdenvogel/clientes-mais](https://github.com/hamdenvogel/clientes-mais) (`clientes-api` + `clientes-app`) |
| 2026-07-28 | Status do roadmap passou a usar checkboxes `[x]` / `[ ]` |
| 2026-07-28 | Status da lista alinhado ao estilo do roteiro CI/CD (`✅ concluída` / `⏳ pendente`) |
| 2026-07-28 | **Item 3:** workflows CI + publish GHCR multi-arch (`amd64`/`arm64`) |
| 2026-07-28 | Remoção de secrets do código versionado; `.gitignore`/README/CONTRIBUTING/SECURITY reforçados |
| 2026-07-28 | **Item 4:** lab K3d (manifests, probes, limits, Helm RabbitMQ, scripts Windows) |
