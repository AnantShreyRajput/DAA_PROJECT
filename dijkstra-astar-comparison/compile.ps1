# compile.ps1 — Windows PowerShell
New-Item -ItemType Directory -Force -Path out | Out-Null
$files = Get-ChildItem -Recurse -Filter "*.java" src | ForEach-Object { $_.FullName }
javac -d out -sourcepath src $files
Write-Host "✔  Compilation complete. Run with: java -cp out Main"

# If the script is called with the single argument 'web', open the simple web GUI
if ($args -contains 'web') {
	$webPath = Join-Path $PSScriptRoot 'web\index.html'
	if (Test-Path $webPath) {
		Start-Process $webPath
		Write-Host "Opened web GUI: $webPath"
	} else {
		Write-Host "Web GUI not found at: $webPath"
	}
}
