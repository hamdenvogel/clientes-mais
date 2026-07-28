#Requires -Version 5.1
<#
.SYNOPSIS
  Cria/atualiza o Secret Kubernetes clientes-api-secrets a partir de clientes-api/.env
#>
$ErrorActionPreference = "Stop"
$Root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
$EnvFile = Join-Path $Root "clientes-api\.env"
$Namespace = "clientes-mais"

if (-not (Test-Path $EnvFile)) {
  throw "Arquivo nao encontrado: $EnvFile`nCopie clientes-api/.env.example para .env e preencha."
}

function Get-DotEnvValue([string]$Path, [string]$Key) {
  $line = Get-Content $Path | Where-Object { $_ -match "^\s*$Key\s*=" } | Select-Object -First 1
  if (-not $line) { return $null }
  return ($line -replace "^\s*$Key\s*=\s*", "").Trim().Trim('"').Trim("'")
}

$required = @(
  "POSTGRES_PASSWORD",
  "SPRING_DATASOURCE_PASSWORD",
  "SPRING_RABBITMQ_PASSWORD",
  "RABBITMQ_DEFAULT_PASS",
  "SECURITY_JWT_SIGNING_KEY",
  "SECURITY_JWT_CHAVE_ASSINATURA",
  "APP_REGISTRATION_TOKEN_SECRET"
)

$values = @{}
foreach ($k in $required) {
  $v = Get-DotEnvValue $EnvFile $k
  if ([string]::IsNullOrWhiteSpace($v) -or $v -like "CHANGE_ME*") {
    throw "Defina $k com valor real em $EnvFile (nao use CHANGE_ME)."
  }
  $values[$k] = $v
}

kubectl get ns $Namespace 2>$null | Out-Null
if ($LASTEXITCODE -ne 0) {
  kubectl apply -f (Join-Path $Root "infra\k8s\namespace.yaml")
}

Write-Host "==> Aplicando Secret clientes-api-secrets no namespace $Namespace..."
kubectl -n $Namespace create secret generic clientes-api-secrets `
  --from-literal=POSTGRES_PASSWORD=$($values.POSTGRES_PASSWORD) `
  --from-literal=SPRING_DATASOURCE_PASSWORD=$($values.SPRING_DATASOURCE_PASSWORD) `
  --from-literal=SPRING_RABBITMQ_PASSWORD=$($values.SPRING_RABBITMQ_PASSWORD) `
  --from-literal=RABBITMQ_PASSWORD=$($values.RABBITMQ_DEFAULT_PASS) `
  --from-literal=rabbitmq-password=$($values.RABBITMQ_DEFAULT_PASS) `
  --from-literal=SECURITY_JWT_SIGNING_KEY=$($values.SECURITY_JWT_SIGNING_KEY) `
  --from-literal=SECURITY_JWT_CHAVE_ASSINATURA=$($values.SECURITY_JWT_CHAVE_ASSINATURA) `
  --from-literal=APP_REGISTRATION_TOKEN_SECRET=$($values.APP_REGISTRATION_TOKEN_SECRET) `
  --dry-run=client -o yaml | kubectl apply -f -

Write-Host "Secret aplicado."
