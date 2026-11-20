param(
    [Parameter(Mandatory=$true, Position=0)] [string] $matrixFile,
    [Parameter(Mandatory=$true, Position=1)] [int]    $N,
    [Parameter(Mandatory=$true, Position=2)] [int]    $M,
    [Parameter(Mandatory=$true, Position=3)] [int]    $K,
    [Parameter(Mandatory=$true, Position=4)] [int]    $P,

    [ValidateSet("rows","cols")] [string] $mode = "rows",

    [int]     $runs = 10,
    [string]  $exe = ".\cuda_lab.exe",
    [string]  $csv = ".\timpi_lab1_cuda.csv",
    [int]     $blockMul = 32,
    [int]     $blockMax = 1024
)

$ErrorActionPreference = "Stop"

if (!(Test-Path $exe)) { throw "Exe not found: $exe" }
if (!(Test-Path $matrixFile)) { throw "Matrix not found: $matrixFile" }

if ($K -ne 3 -and $K -ne 5) { throw "K must be 3 or 5" }
if ($P -le 0) { throw "P must be > 0" }
if ($runs -le 0) { throw "runs must be > 0" }

# Ca in scriptul tau: blockSize pornind de la P
$blockSize = $P * $blockMul
if ($blockSize -gt $blockMax) { $blockSize = $blockMax }
if ($blockSize -lt 32) { $blockSize = 32 }

if (!(Test-Path $csv)) {
    "matrix,N,M,K,P,mode,blockSize,runs,cpu_avg_ms,gpu_avg_ms,ok" |
            Out-File -FilePath $csv -Encoding utf8
}

function Get-Value($lines, $key) {
    $line = ($lines | Where-Object { $_ -match "^$key=" } | Select-Object -First 1)
    if (-not $line) { return $null }
    return $line.Split("=")[1]
}

$cpuSum = 0.0
$gpuSum = 0.0
$allOk = $true

for ($i = 0; $i -lt $runs; $i++) {
    $out = & $exe $matrixFile $N $M $K $P $blockSize $mode

    if ($LASTEXITCODE -ne 0 -and $LASTEXITCODE -ne 2) {
        throw "Exe failed with exit code $LASTEXITCODE`n$out"
    }

    $cpu = Get-Value $out "CPU_MS"
    $gpu = Get-Value $out "GPU_MS"
    $ok  = Get-Value $out "OK"

    if ($cpu -eq $null -or $gpu -eq $null) {
        throw "Missing CPU_MS or GPU_MS in output.`n$out"
    }

    $cpuSum += [double]$cpu
    $gpuSum += [double]$gpu

    if ($ok -ne $null -and [int]$ok -ne 1) { $allOk = $false }
    if ($LASTEXITCODE -eq 2) { $allOk = $false }
}

$cpuAvg = $cpuSum / $runs
$gpuAvg = $gpuSum / $runs

"$matrixFile,$N,$M,$K,$P,$mode,$blockSize,$runs,$cpuAvg,$gpuAvg,$([int]$allOk)" |
        Out-File -FilePath $csv -Append -Encoding utf8

Write-Host "N=$N M=$M K=$K P=$P mode=$mode block=$blockSize CPU_AVG_MS=$cpuAvg GPU_AVG_MS=$gpuAvg OK=$([int]$allOk)"
