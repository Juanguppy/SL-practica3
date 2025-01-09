$filePath = ".\DATOS.DAT"
$fileStream = [System.IO.File]::Open($filePath, 'Open', 'Write', 'None')
Start-Sleep -Seconds 3600 # Mantener el archivo abierto durante 1 hora
$fileStream.Close()