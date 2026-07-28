[xml]$report = Get-Content "target/site/jacoco/jacoco.xml"
$classes = $report.report.package.class
$lowCoverage = $classes | ForEach-Object {
    $counters = $_.counter
    $instructionCounter = $counters | Where-Object { $_.type -eq 'INSTRUCTION' }
    
    if ($instructionCounter) {
        $missed = [int]$instructionCounter.missed
        $covered = [int]$instructionCounter.covered
        $total = $missed + $covered
        
        if ($total -gt 0) {
            $percentage = ($covered / $total) * 100
            [PSCustomObject]@{
                Name = $_.name
                Missed = $missed
                Covered = $covered
                Total = $total
                Percentage = [math]::Round($percentage, 2)
            }
        }
    }
} | Where-Object { $_.Percentage -lt 90 } | Sort-Object Percentage

$lowCoverage | Select-Object -First 20 | ForEach-Object { "$($_.Name): $($_.Percentage)% ($($_.Missed) missed instructions)" }

$totalMissed = ($classes | ForEach-Object { $_.counter | Where-Object { $_.type -eq 'INSTRUCTION' } | Measure-Object -Property missed -Sum }).Sum
$totalCovered = ($classes | ForEach-Object { $_.counter | Where-Object { $_.type -eq 'INSTRUCTION' } | Measure-Object -Property covered -Sum }).Sum
$totalInstructions = $totalMissed + $totalCovered
if ($totalInstructions -gt 0) {
    $totalPercentage = ($totalCovered / $totalInstructions) * 100
    Write-Host "Overall Coverage: $([math]::Round($totalPercentage, 2))%"
}
