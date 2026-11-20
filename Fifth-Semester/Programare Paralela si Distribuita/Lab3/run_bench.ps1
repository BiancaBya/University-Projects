param(
  [Parameter(Mandatory=$true, Position=0)] [string] $exe,
  [Parameter(Mandatory=$true, Position=1)] [int]    $procs,
  [Parameter(Mandatory=$true, Position=2)] [int]    $runs,
  [Parameter(Mandatory=$true, Position=3)] [string] $label,
  [Parameter(ValueFromRemainingArguments=$true)]    [string[]] $rest
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
$PSNativeCommandUseErrorActionPreference = $false

$IsMpi = $false
$outCsv = "outC.csv"
$extraArgs = @()
$passThrough = $false
for ($i = 0; $i -lt $rest.Count; $i++) {
  $tok = $rest[$i]
  if ($passThrough) { $extraArgs += $tok; continue }
  switch ($tok) {
    '--mpi' { $IsMpi = $true }
    '--out' { if ($i + 1 -ge $rest.Count) { throw "--out needs a filename" }; $outCsv = $rest[$i + 1]; $i++ }
    '--'    { $passThrough = $true }
    default { $extraArgs += $tok }
  }
}

if (-not (Test-Path -Path $exe -PathType Leaf)) { throw "Eroare: executabilul '$exe' nu există." }

if ($IsMpi) { $cmd = @("mpiexec","-n","$procs","$exe") + $extraArgs } else { $cmd = @("$exe") + $extraArgs }
$modeStr = if ($IsMpi) { "MPI" } else { "SEQ" }
Write-Host "=== Rulez: $($cmd -join ' ') | runs=$runs | procs=$procs | mode=$modeStr | label=$label ==="

function Parse-TimeMs {
  param([string] $text)
  $m = [regex]::Matches($text, '([0-9]+(?:\.[0-9]+)?)\s*ms', 'IgnoreCase')
  if ($m.Count -gt 0) { return [double] $m[$m.Count-1].Groups[1].Value }
  $s = [regex]::Matches($text, '([0-9]+(?:\.[0-9]+)?)\s*(?:sec|seconds|s)\b', 'IgnoreCase')
  if ($s.Count -gt 0) { return ([double] $s[$s.Count-1].Groups[1].Value) * 1000.0 }
  return $null
}

[double] $sum = 0.0
for ($i = 1; $i -le $runs; $i++) {
  $prevEA = $ErrorActionPreference
  $ErrorActionPreference = "Continue"
  try {
    if ($cmd.Count -gt 1) { $outLines = & $cmd[0] $cmd[1..($cmd.Count-1)] 2>&1 } else { $outLines = & $cmd[0] 2>&1 }
  } finally {
    $ErrorActionPreference = $prevEA
  }
  $out = ($outLines | ForEach-Object { $_.ToString() }) -join "`n"
  if ($LASTEXITCODE -ne 0) { Write-Host $out; throw "Procesul a ieșit cu cod $LASTEXITCODE" }

  $ms = Parse-TimeMs -text $out
  if ($null -eq $ms) { throw "Nu am găsit un timp în 'ms' sau 'sec'." }
  Write-Host ("Rulare {0}: {1:N3} ms" -f $i, $ms)
  $sum += $ms
}

$avg = if ($runs -gt 0) { [Math]::Round($sum / $runs, 6) } else { 0.0 }
Write-Host ("Timp mediu: {0:N3} ms" -f $avg)

if (-not (Test-Path -Path $outCsv -PathType Leaf)) { '"Label","Mode","Procs","Timp executie (ms)"' | Out-File -FilePath $outCsv -Encoding utf8 }
'"{0}","{1}",{2},{3}' -f $label, $modeStr, $procs, $avg | Out-File -FilePath $outCsv -Append -Encoding utf8
Write-Host "Am scris media in: $outCsv"