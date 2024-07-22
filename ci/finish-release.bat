@echo off
setlocal enabledelayedexpansion

:: Debug statement
echo Starting finish-release script

:: Shelve current workspace
for /f "tokens=*" %%b in ('git branch --show-current') do set branch=%%b
echo You were working on branch %branch%

:: Get the first release branch matching the pattern
for /f "tokens=*" %%r in ('git branch --list release/*') do (
  set releaseBranch=%%r
  goto foundBranch
)

:foundBranch
:: Check if the releaseBranch variable was set
if "%releaseBranch%"=="" (
  call :exit_on_failure "No release/* branch found. Git branch --list for release branches"
  goto :EOF
)
echo "Release branch: %releaseBranch%"

:: Stash current work
echo Stashing your work
git stash
if errorlevel 1 (
  call :exit_on_failure "Ensure you have no uncommitted changes. Stashing your work"
  goto :EOF
)

:: Checkout to the release branch
echo Checking out the release branch %releaseBranch%
git checkout "%releaseBranch%"
if errorlevel 1 (
  call :exit_on_failure "Checkout to release branch"
  goto :EOF
)

:: Pull the latest changes
echo Pulling the latest changes (if any)
git pull origin "%releaseBranch%"
if errorlevel 1 (
  call :exit_on_failure "Pulling latest changes"
  goto :EOF
)

:: Proceed with the main finish-release script
echo Proceeding with the main finish-release script...
call mvn gitflow:release-finish -DskipTestProject=true
if errorlevel 1 (
  call :exit_on_failure "Maven release finish"
  goto :EOF
)

:: Recover the workspace to the state it was prior to running the script
echo Taking you back to your work on branch %branch%
git checkout "%branch%"
if errorlevel 1 (
  call :exit_on_failure "Checkout back to branch"
  goto :EOF
)

:: Apply the stashed work
echo Applying your stashed work
git stash apply
if errorlevel 1 (
  call :exit_on_failure "Applying stashed work"
  goto :EOF
)

endlocal
echo Exiting script
exit /b 0

:: Function to display error and exit
:exit_on_failure
echo %1 failed
endlocal
exit /b 1