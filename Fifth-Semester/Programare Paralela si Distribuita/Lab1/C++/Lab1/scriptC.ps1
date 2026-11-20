
$param1 = $args[0] # Nume fisier exe
$param4 = $args[1] # N
$param5 = $args[2] # M
$param6 = $args[3] # K
$param7 = $args[4] # secv/paralel -> 0/1
$param2 = $args[5] # No of threads
$param3 = $args[6] # No of runs


$suma = 0

for ($i = 0; $i -lt $param3; $i++){
    Write-Host "Rulare" ($i+1)
    $a = (cmd /c .\$param1 $param2 2`>`&1)
    Write-Host $a[$a.length-1]
    $suma += $a[$a.length-1]
    Write-Host ""
}
$media = $suma / $i
#Write-Host $suma
Write-Host "Timp de executie mediu:" $media

# Creare fisier .csv
if (!(Test-Path outC.csv)){
    New-Item outC.csv -ItemType File
    Set-Content outC.csv 'N, M, K, Secv/Paralel, Nr threads, Timp executie'
}

# Append
Add-Content outC.csv "$($args[1]), $($args[2]), $($args[3]), $($args[4]), $($args[5]), $($media)"