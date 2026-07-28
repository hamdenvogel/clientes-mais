$csv = Import-Csv "c:\Hamden\Sistemas\Backend\clientes\des\clientes\target\site\jacoco\jacoco.csv"
$list = $csv | Select-Object GROUP, PACKAGE, CLASS, INSTRUCTION_MISSED, INSTRUCTION_COVERED | ForEach-Object {
    $m = [int]$_.INSTRUCTION_MISSED
    $c = [int]$_.INSTRUCTION_COVERED
    $t = $m + $c
    $p = 0
    if ($t -gt 0) { $p = ($c / $t) * 100 }
    [PSCustomObject]@{
        Class = $_.CLASS
        Missed = $m
        Covered = $c
        Percent = [math]::Round($p, 2)
    }
}
$list | Sort-Object Missed -Descending | Select-Object -First 20 | Format-Table -AutoSize
$totalMissed = ($list | Measure-Object Missed -Sum).Sum
$totalCovered = ($list | Measure-Object Covered -Sum).Sum
$totalPercent = ($totalCovered / ($totalMissed + $totalCovered)) * 100
Write-Host "Overall Coverage: $("{0:N2}" -f $totalPercent)%"
