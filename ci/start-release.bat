@echo off
setlocal enabledelayedexpansion

:: Function to check the status of the previous command and exit if it fails
:exit_on_failure
if not errorlevel 1 goto :EOF
echo %1 failed
endlocal
exit /b 1

:: Shelve current workspace
for /f "tokens=*" %%b in ('git branch --show-current') do set branch=%%b
echo You were working on branch %branch%
echo Stashing your work...
git stash
call :exit_on_failure "Stashing your work"

:: Proceeding with release's main script
echo Proceeding with the main start-release script...
git checkout main
call :exit_on_failure "Checkout to main"

git pull origin main
call :exit_on_failure "Pulling main"

git checkout develop
call :exit_on_failure "Checkout to develop"

git pull origin develop
call :exit_on_failure "Pulling develop"

call mvn gitflow:release-start --batch-mode
call :exit_on_failure "Maven release start"

call mvn versions:use-releases scm:checkin -Dmessage="Updated snapshot dependencies to release versions" -DpushChanges=false
call :exit_on_failure "Maven versions use-releases and scm checkin"

:: Recover the workspace to the state it was, prior to running the script
echo Taking you back to your work on branch %branch%
git checkout "%branch%"
call :exit_on_failure "Checkout back to branch"

echo Applying your stashed work...
git stash apply
call :exit_on_failure "Applying stashed work"

echo You can follow the progress of the artefacts at https://jenkinssb.visitscotland.com/job/release-brc.visitscotland.com/

endlocal
