@echo off
setlocal enabledelayedexpansion

:: Debug statement
echo Starting start-release script

:: Shelve current workspace
for /f "tokens=*" %%b in ('git branch --show-current') do set branch=%%b
echo You were working on branch %branch%
echo Stashing your work...
git stash
if errorlevel 1 (
  call :exit_on_failure "Stashing your work"
  goto :EOF
)

:: Proceeding with the main start-release script
echo Proceeding with the main start-release script...
git checkout main
if errorlevel 1 (
  call :exit_on_failure "Checkout to main"
  goto :EOF
)

git pull origin main
if errorlevel 1 (
  call :exit_on_failure "Pulling main"
  goto :EOF
)

git checkout develop
if errorlevel 1 (
  call :exit_on_failure "Checkout to develop"
  goto :EOF
)

git pull origin develop
if errorlevel 1 (
  call :exit_on_failure "Pulling develop"
  goto :EOF
)

call mvn gitflow:release-start --batch-mode
if errorlevel 1 (
  call :exit_on_failure "Maven release start"
  goto :EOF
)

call mvn versions:use-releases scm:checkin -Dmessage="Updated snapshot dependencies to release versions" -DpushChanges=false
if errorlevel 1 (
  call :exit_on_failure "Maven versions use-releases and scm checkin"
  goto :EOF
)

echo Taking you back to your work on branch %branch%
git checkout "%branch%"
if errorlevel 1 (
  call :exit_on_failure "Checkout back to branch"
  goto :EOF
)

echo Applying your stashed work
git stash apply
if errorlevel 1 (
  call :exit_on_failure "Applying stashed work"
  goto :EOF
)

echo You can follow the progress of the artefacts at https://jenkinssb.visitscotland.com/job/release-brc.visitscotland.com/

endlocal
exit /b 0

:: Function to display error and exit
:exit_on_failure
echo %1 failed
endlocal
exit /b 1
