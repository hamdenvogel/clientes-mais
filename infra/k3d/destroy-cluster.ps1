#Requires -Version 5.1
<#
.SYNOPSIS
  Remove o cluster K3d clientes-mais.
#>
$ErrorActionPreference = "Stop"
$ClusterName = "clientes-mais"

if (-not (Get-Command k3d -ErrorAction SilentlyContinue)) {
  throw "k3d nao encontrado."
}

Write-Host "==> Removendo cluster '$ClusterName'..."
k3d cluster delete $ClusterName
Write-Host "Cluster removido."
