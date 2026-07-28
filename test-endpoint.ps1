$ErrorActionPreference = "Stop"

# Config
$baseUrl = "http://localhost:8080"
$login = "cicrano"
$senha = "1234"

Write-Host "=== STEP 1: SIGNIN ===" -ForegroundColor Green

try {
  $signinBody = @{ login = $login; senha = $senha } | ConvertTo-Json
  Write-Host "POST $baseUrl/api/auth/signin"
  Write-Host "Body: $signinBody"

  $auth = Invoke-RestMethod -Method Post -Uri "$baseUrl/api/auth/signin" -ContentType "application/json" -Body $signinBody

  Write-Host "Signin Response:" -ForegroundColor Cyan
  Write-Host ($auth | ConvertTo-Json -Depth 10)

  $token = $auth.token
  if (-not $token) {
    throw "Signin respondeu sem campo token."
  }
} catch {
  Write-Host "ERRO no signin:" -ForegroundColor Red
  Write-Host $_.Exception.Message
  exit 1
}

Write-Host "`n=== STEP 2: DECODE JWT ===" -ForegroundColor Green

try {
  $jwtParts = $token.Split(".")
  if ($jwtParts.Length -lt 2) {
    throw "JWT invalido no formato (esperado 3 partes)."
  }

  $payloadB64 = $jwtParts[1].Replace('-', '+').Replace('_', '/')
  switch ($payloadB64.Length % 4) {
    2 { $payloadB64 += "==" }
    3 { $payloadB64 += "=" }
  }

  $payloadJson = [System.Text.Encoding]::UTF8.GetString([Convert]::FromBase64String($payloadB64))
  $payload = $payloadJson | ConvertFrom-Json

  Write-Host "JWT Payload:" -ForegroundColor Cyan
  Write-Host "  sub (username): $($payload.sub)"
  Write-Host "  exp (epoch): $($payload.exp)"
  Write-Host "  authorities: $($payload.authorities)"
  Write-Host "  id: $($payload.id)"
  Write-Host "  email: $($payload.email)"
} catch {
  Write-Host "ERRO ao decodificar JWT:" -ForegroundColor Red
  Write-Host $_.Exception.Message
  exit 1
}

Write-Host "`n=== STEP 3: CALL PESQUISA-PAGINADA ===" -ForegroundColor Green

$uri = "$baseUrl/api/clientes/pesquisa-paginada?page=0&size=11&nome="
Write-Host "GET $uri"
Write-Host "Authorization: Bearer [token truncado]"

try {
  $response = Invoke-WebRequest -Method Get -Uri $uri -Headers @{ Authorization = "Bearer $token" }

  Write-Host "HTTP Status:" $response.StatusCode -ForegroundColor Green
  Write-Host "Content:"
  Write-Host ($response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 5)
} catch {
  $resp = $_.Exception.Response
  if ($resp -ne $null) {
    $reader = New-Object System.IO.StreamReader($resp.GetResponseStream())
    $body = $reader.ReadToEnd()

    Write-Host "HTTP Status:" [int]$resp.StatusCode -ForegroundColor Red
    Write-Host "Error Body:"
    Write-Host $body
  } else {
    Write-Host "Exception:" -ForegroundColor Red
    Write-Host $_.Exception.Message
  }
}

Write-Host "`n=== FIM ===" -ForegroundColor Yellow

