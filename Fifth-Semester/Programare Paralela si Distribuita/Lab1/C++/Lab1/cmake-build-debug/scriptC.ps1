$param1 = $args[0] # Nume fisier exe
$param2 = $args[1] # No of threads
$param3 = $args[2] # No of runs
$param4 = $args[3] # 0=static 1=dynamic

# Executare exe in cmd mode

$suma = 0


for ($i = 0; $i -lt $param3; $i++){
    Write-Host "Rulare" ($i+1)

    # fortăm un ARRAY de ieșiri și capturăm stdout+stderr
    $a = @(& .\$param1 $param2 $param4 2>&1)

    if (-not $a) {
        Write-Warning "Nu s-a primit niciun output la rularea $($i+1)."
        continue
    }

    # ultima LINIE, nu ultimul caracter
    $lastLine = $a | Select-Object -Last 1
    Write-Host $lastLine

    # adună numeric (nu ca string)
    $suma += [double]$lastLine
    Write-Host ""
}

$media = $suma / $i
#Write-Host $suma
Write-Host "Timp de executie mediu:" $media

# Creare fisier .csv
if (!(Test-Path outC.csv)){
    New-Item outC.csv -ItemType File
    #Scrie date in csv
    Set-Content outC.csv 'Tip Matrice,Tip alocare,Nr threads,Timp executie'
}

# Append
Add-Content outC.csv ",,$($args[1]),$($media)"