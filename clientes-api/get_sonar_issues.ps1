$headers = @{ Authorization = "Basic " + [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("sqp_ae579a1a6cf6638dcf1ab52a27a234f5adc74956:")) }
$response = Invoke-RestMethod -Uri "http://localhost:9000/api/issues/search?componentKeys=clientes&resolved=false&severities=MAJOR,CRITICAL" -Headers $headers
$response.issues | Select-Object component, message, line, severity | ConvertTo-Json
