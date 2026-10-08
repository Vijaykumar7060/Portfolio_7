@echo off
title Vijaykumar Karegama - Java Portfolio Server
echo ===================================================
echo  Starting Vijaykumar's Java 17 Portfolio Server...
echo ===================================================

where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH!
    echo Please install OpenJDK 17 or higher.
    pause
    exit /b 1
)

echo [*] Compiling PortfolioServer.java...
javac PortfolioServer.java
if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed.
    pause
    exit /b 1
)

echo [*] Launching Java Portfolio Server on port 8080...
start "" http://localhost:8080
java PortfolioServer
pause
