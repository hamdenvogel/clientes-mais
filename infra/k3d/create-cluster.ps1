#Requires -Version 5.1
<#
.SYNOPSIS
  Cria cluster K3d "clientes-mais" no Windows (item 4 do roadmap).
#>
$ErrorActionPreference = "Stop"
$ClusterName = "clientes-mais"
$HttpPort = 8081

function Assert-Command($Name, $Hint) {
  if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) {
    throw "Comando '$Name' nao encontrado. $Hint"
  }
}

Write-Host "==> Verificando ferramentas..."
Assert-Command docker "Instale Docker Desktop e garanta que esta rodando."
Assert-Command kubectl "kubectl costuma vir com Docker Desktop."

if (-not (Get-Command k3d -ErrorAction SilentlyContinue)) {
  Write-Host "k3d nao encontrado. Tentando instalar via winget..."
  winget install --id RancherLabs.k3d -e --accept-package-agreements --accept-source-agreements
  $env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" +
              [System.Environment]::GetEnvironmentVariable("Path", "User")
  if (-not (Get-Command k3d -ErrorAction SilentlyContinue)) {
    throw "Instale k3d manualmente: https://k3d.io/#installation"
  }
}

if (-not (Get-Command helm -ErrorAction SilentlyContinue)) {
  Write-Host "helm nao encontrado. Tentando instalar via winget..."
  winget install --id Helm.Helm -e --accept-package-agreements --accept-source-agreements
  $env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" +
              [System.Environment]::GetEnvironmentVariable("Path", "User")
  if (-not (Get-Command helm -ErrorAction SilentlyContinue)) {
    throw "Instale Helm manualmente: https://helm.sh/docs/intro/install/"
  }
}

$existingJson = k3d cluster list -o json 2>$null
if ($existingJson) {
  $existing = $existingJson | ConvertFrom-Json
  if (@($existing) | Where-Object { $_.name -eq $ClusterName }) {
    Write-Host "Cluster '$ClusterName' ja existe. Use destroy-cluster.ps1 para recriar."
    kubectl config use-context "k3d-$ClusterName" | Out-Null
    exit 0
  }
}

Write-Host "==> Criando cluster k3d '$ClusterName' (http://localhost:$HttpPort via Traefik)..."
k3d cluster create $ClusterName `
  --agents 0 `
  --servers 1 `
  -p "${HttpPort}:80@loadbalancer" `
  --wait

kubectl config use-context "k3d-$ClusterName"
kubectl cluster-info
Write-Host ""
Write-Host "Cluster pronto."
Write-Host "Proximos passos:"
Write-Host "  1) .\create-k8s-env.ps1"
Write-Host "  2) .\deploy.ps1"
