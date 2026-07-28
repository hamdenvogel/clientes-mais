# K3d no Windows — Item 4 do roadmap

Lab Kubernetes local: **K3d** + manifests + **RabbitMQ (Helm)** + PVC.

## Pré-requisitos

- Docker Desktop (rodando)
- `kubectl` (vem com Docker Desktop)
- `k3d` e `helm` (os scripts tentam instalar via `winget` se faltarem)
- `clientes-api/.env` preenchido (a partir de `.env.example`)

## Passo a passo

```powershell
cd C:\Hamden\Sistemas\Backend\clientes\infra\k3d

.\create-cluster.ps1
.\create-secret.ps1
.\deploy.ps1
```

Health:

```powershell
curl.exe http://localhost:8081/actuator/health
```

## O que sobe

| Recurso | Detalhe |
|---------|---------|
| Namespace | `clientes-mais` |
| Secret | `clientes-api-secrets` (a partir do `.env`) |
| ConfigMap | URL Postgres/Rabbit, profile `docker` |
| Postgres | Deployment + PVC 2Gi + init schema |
| RabbitMQ | Helm Bitnami + PVC 1Gi |
| API | Deployment `clientes-api:local` (importada no k3d) |
| Service / Ingress | Traefik → `localhost:8081` |
| Probes | startup + readiness + liveness (`/actuator/health*`) |
| Limits | CPU/memória no Deployment |
| HPA | opcional: `kubectl apply -f ../k8s/api-hpa.yaml` |

## Estrutura

```text
infra/
  k3d/          # scripts PowerShell + values RabbitMQ
  k8s/          # manifests + kustomization
```

## Destruir

```powershell
.\destroy-cluster.ps1
```

## Notas

- Imagem de lab: build local `clientes-api:local` (não exige GHCR).
- Para testar a imagem do registry: altere `image:` em `infra/k8s/api-deployment.yaml` para `ghcr.io/hamdenvogel/clientes-mais/clientes-api:latest` e configure `imagePullSecrets` se o pacote for privado.
- Segredos **nunca** vão no Git — só `secret.example.yaml` + `create-secret.ps1`.
