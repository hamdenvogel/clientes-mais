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
} | Where-Object { $_.Percentage -lt 80 } | Sort-Object Percentage

$lowCoverage | Format-Table -AutoSize
