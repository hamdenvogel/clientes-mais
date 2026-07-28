#Requires -Version 5.1
<#
.SYNOPSIS
  Deploy completo no K3d: manifests + RabbitMQ (Helm) + imagem local da API.
#>
$ErrorActionPreference = "Stop"
$Root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
$ClusterName = "clientes-mais"
$Namespace = "clientes-mais"
$Image = "clientes-api:local"
$ApiDir = Join-Path $Root "clientes-api"
$K8sDir = Join-Path $Root "infra\k8s"
$ValuesRabbit = Join-Path $PSScriptRoot "values-rabbitmq.yaml"

function Assert-Command($Name) {
  if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) {
    throw "Comando '$Name' nao encontrado."
  }
}

Assert-Command docker
Assert-Command kubectl
Assert-Command k3d
Assert-Command helm

kubectl config use-context "k3d-$ClusterName" | Out-Null

Write-Host "==> Namespace + Secret..."
kubectl apply -f (Join-Path $K8sDir "namespace.yaml")
& (Join-Path $PSScriptRoot "create-secret.ps1")

Write-Host "==> Build imagem Docker local (se necessario)..."
Push-Location $ApiDir
try {
  docker build -t $Image .
} finally {
  Pop-Location
}

Write-Host "==> Importando imagem no k3d..."
k3d image import $Image -c $ClusterName

Write-Host "==> Postgres + ConfigMap + API + Ingress..."
kubectl apply -k $K8sDir

Write-Host "==> RabbitMQ via Helm (Bitnami) + PVC..."
helm repo add bitnami https://charts.bitnami.com/bitnami 2>$null
helm repo update bitnami
helm upgrade --install rabbitmq bitnami/rabbitmq `
  --namespace $Namespace `
  --values $ValuesRabbit `
  --wait --timeout 10m

Write-Host "==> Aguardando API ficar Ready..."
kubectl -n $Namespace rollout status deployment/clientes-api --timeout=300s
kubectl -n $Namespace get pods,svc,ingress

Write-Host ""
Write-Host "Pronto. Teste:"
Write-Host "  curl.exe http://localhost:8081/actuator/health"
Write-Host "  (Ingress Traefik do k3d na porta 8081)"
Write-Host ""
Write-Host "HPA opcional:"
Write-Host "  kubectl apply -f infra\k8s\api-hpa.yaml"
