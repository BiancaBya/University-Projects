param(
    [Parameter(Mandatory=$true, Position=0)] [string] $matrixFile,
    [Parameter(Mandatory=$true, Position=1)] [int]    $N,
    [Parameter(Mandatory=$true, Position=2)] [int]    $M,
    [Parameter(Mandatory=$true, Position=3)] [int]    $K,
    [Parameter(Mandatory=$true, Position=4)] [int]    $P,

    [ValidateSet("cpu","gpu","both")]
    [string]  $mode = "both",

    [int]     $runs = 10,
    [string]  $exe = ".\cuda_lab.exe",
    [string]  $csv = ".\timpi.csv",
    [int]     $blockMul = 32,
    [int]     $blockMax = 1024
)

$ErrorActionPreference = "Stop"

if (!(Test-Path $exe)) { throw "Exe not found: $exe" }
if (!(Test-Path $matrixFile)) { throw "Matrix not found: $matrixFile" }

if ($K -ne 3 -and $K -ne 5) { throw "K must be 3 or 5" }
if ($P -le 0) { throw "P must be > 0" }
if ($runs -le 0) { throw "runs must be > 0" }

$blockSize = $P * $blockMul
if ($blockSize -gt $blockMax) { $blockSize = $blockMax }
if ($blockSize -lt 32) { $blockSize = 32 }

if (!(Test-Path $csv)) {
    "N,M,K,P,Blocksize,Runs,CPU_AVG_MS,GPU_AVG_MS,OK" |
            Out-File -FilePath $csv -Encoding utf8
}

function Get-Value($lines, $key) {
    $line = ($lines | Where-Object { $_ -match "^$key=" } | Select-Object -First 1)
    if (-not $line) { return $null }
    return $line.Split("=")[1]
}

$cpuSum = 0.0
$gpuSum = 0.0
$allOk  = $true

Write-Host "=================================================="
Write-Host "START BENCHMARK"
Write-Host "Mode      : $mode"
Write-Host "Matrix    : $matrixFile"
Write-Host "N=$N M=$M K=$K P=$P"
Write-Host "BlockSize : $blockSize"
Write-Host "Runs      : $runs"
Write-Host "=================================================="

for ($i = 0; $i -lt $runs; $i++) {
    $runNo = $i + 1
    Write-Host ""
    Write-Host "[RUN $runNo / $runs] mode=$mode"

    $out = & $exe $matrixFile $N $M $K $P $blockSize $mode

    if ($LASTEXITCODE -ne 0 -and $LASTEXITCODE -ne 2) {
        throw "Exe failed with exit code $LASTEXITCODE`n$out"
    }

    $ok = Get-Value $out "OK"
    if ($ok -ne $null -and [int]$ok -ne 1) { $allOk = $false }
    if ($LASTEXITCODE -eq 2) { $allOk = $false }

    if ($mode -eq "cpu") {
        $cpu = Get-Value $out "CPU_MS"
        if ($cpu -eq $null) { throw "Missing CPU_MS in output.`n$out" }

        $cpuSum += [double]$cpu
        Write-Host "  CPU_MS = $cpu ms"
    }
    elseif ($mode -eq "gpu") {
        $gpu = Get-Value $out "GPU_MS"
        if ($gpu -eq $null) { throw "Missing GPU_MS in output.`n$out" }

        $gpuSum += [double]$gpu
        Write-Host "  GPU_MS = $gpu ms"
    }
    else {
        $cpu = Get-Value $out "CPU_MS"
        $gpu = Get-Value $out "GPU_MS"
        if ($cpu -eq $null -or $gpu -eq $null) {
            throw "Missing CPU_MS or GPU_MS in output.`n$out"
        }

        $cpuSum += [double]$cpu
        $gpuSum += [double]$gpu
        Write-Host "  CPU_MS = $cpu ms"
        Write-Host "  GPU_MS = $gpu ms"
    }

    Write-Host "  OK = $ok"
}

$cpuAvg = 0.0
$gpuAvg = 0.0

if ($mode -eq "cpu") {
    $cpuAvg = $cpuSum / $runs
    $gpuAvg = 0.0
}
elseif ($mode -eq "gpu") {
    $cpuAvg = 0.0
    $gpuAvg = $gpuSum / $runs
}
else {
    $cpuAvg = $cpuSum / $runs
    $gpuAvg = $gpuSum / $runs
}

Write-Host ""
Write-Host "==================== SUMMARY ====================="
Write-Host "CPU_AVG_MS = $cpuAvg"
Write-Host "GPU_AVG_MS = $gpuAvg"
Write-Host "OK         = $([int]$allOk)"
Write-Host "=================================================="

"$N,$M,$K,$P,$blockSize,$runs,$cpuAvg,$gpuAvg,$([int]$allOk)" |
        Out-File -FilePath $csv -Append -Encoding utf8

Write-Host "mode=$mode N=$N M=$M K=$K P=$P block=$blockSize CPU_AVG_MS=$cpuAvg GPU_AVG_MS=$gpuAvg OK=$([int]$allOk)"
