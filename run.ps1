# PowerShell Launcher for Vijaykumar's Java 17 Portfolio Server
Write-Host "===================================================" -ForegroundColor Cyan
Write-Host " Starting Vijaykumar's Java 17 Portfolio Server... " -ForegroundColor Green
Write-Host "===================================================" -ForegroundColor Cyan

if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "[ERROR] Java is not installed or not found in PATH." -ForegroundColor Red
    Exit 1
}

Write-Host "[*] Compiling PortfolioServer.java..." -ForegroundColor Yellow
javac PortfolioServer.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Compilation failed." -ForegroundColor Red
    Exit 1
}

Write-Host "[*] Launching Java Portfolio Server on http://localhost:8080 ..." -ForegroundColor Green
Start-Process "http://localhost:8080"
java PortfolioServer
