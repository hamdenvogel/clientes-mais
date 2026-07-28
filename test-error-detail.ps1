$ErrorActionPreference = "Stop"

$baseUrl = "http://localhost:8080"
$login = "cicrano"
$senha = "1234"

# 1) Login
$signinBody = @{ login = $login; senha = $senha } | ConvertTo-Json
$auth = Invoke-RestMethod -Method Post -Uri "$baseUrl/api/auth/signin" -ContentType "application/json" -Body $signinBody
$token = $auth.token

# 2) Chamada com detalhes de erro
$uri = "$baseUrl/api/clientes/pesquisa-paginada?page=0&size=11&nome="

try {
  Invoke-WebRequest -Method Get -Uri $uri -Headers @{ Authorization = "Bearer $token" } | Out-Null
} catch {
  $resp = $_.Exception.Response
  $reader = New-Object System.IO.StreamReader($resp.GetResponseStream())
  $body = $reader.ReadToEnd()

  Write-Host "Status Code:" $resp.StatusCode
  Write-Host "Status Description:" $resp.StatusDescription
  Write-Host "`nResponse Body:"
  Write-Host $body
  Write-Host "`n--- Formatted ---"
  $body | ConvertFrom-Json | ConvertTo-Json -Depth 10
}

