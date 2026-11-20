param(
  [int]$Port = 5050,
  [int]$Threads = 8,
  [int]$TPlataSec = 5,
  [int]$ShutdownAfterSec = 30,
  [int]$Clients = 4,
  [int]$ReserveEverySec = 2,
  [int]$WarmupSec = 3
)

function Run-One([int]$CheckIntervalSec) {
  $stamp = Get-Date -Format "yyyyMMdd_HHmmss"
  $outDir = "runs\run_${CheckIntervalSec}s_$stamp"
  New-Item -ItemType Directory -Force -Path $outDir | Out-Null

  Write-Host "=== RUN checkerInterval=${CheckIntervalSec}s => $outDir ==="

  if (Test-Path "data") { Remove-Item -Recurse -Force "data" }
  New-Item -ItemType Directory -Force -Path "data" | Out-Null

  $serverArgs = "--port=$Port --threads=$Threads --tPlataSec=$TPlataSec --checkIntervalSec=$CheckIntervalSec --shutdownAfterSec=$ShutdownAfterSec --dataDir=data"
  $serverLog = Join-Path $outDir "server.log"
  Write-Host "Starting server..."
  $serverProc = Start-Process -FilePath "powershell" -ArgumentList "-NoProfile", "-Command", "cd '$PWD'; ./gradlew runServer --args='$serverArgs' | Tee-Object -FilePath '$serverLog'" -PassThru

  Start-Sleep -Seconds $WarmupSec

  $simArgs = "--host=127.0.0.1 --port=$Port --clients=$Clients --reserveEverySec=$ReserveEverySec"
  $simLog = Join-Path $outDir "simulator.log"
  Write-Host "Starting simulator..."
  & ./gradlew runSimulator --args="$simArgs" | Tee-Object -FilePath $simLog

  Write-Host "Waiting for server to stop..."
  Wait-Process -Id $serverProc.Id

  Copy-Item -Recurse -Force "data" (Join-Path $outDir "data")

  Write-Host "Saved outputs to $outDir"
}

Run-One 10

Run-One 5

Write-Host "=== DONE. Check folder: runs/ ==="
