@echo off
title Push Portfolio to GitHub
echo ====================================================
echo   Deploying Portfolio to GitHub (Vijaykumar7060)
echo ====================================================
echo.
echo Make sure you have created the repository 'portfolio' on GitHub:
echo https://github.com/new
echo.
echo Pushing branch 'main' to GitHub...
git push -u origin main

if %errorlevel% equ 0 (
    echo.
    echo [SUCCESS] Code successfully pushed to GitHub!
    echo.
    echo Your GitHub Actions workflow is now deploying your site to GitHub Pages!
    echo Check status at: https://github.com/Vijaykumar7060/portfolio/actions
) else (
    echo.
    echo [NOTE] Push failed. If the repo does not exist yet, create it at:
    echo https://github.com/new (Repository name: portfolio)
)

pause
