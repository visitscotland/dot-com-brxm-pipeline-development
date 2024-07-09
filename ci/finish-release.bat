@echo off
:: enable the use of ! for delayed variable expansion, which is necessary
:: when modifying variables within loops and conditionals in batch scripts.
setlocal enabledelayedexpansion

:: Get current branch
for /f "tokens=*" %%b in ('git branch --show-current') do set branch=%%b

:: Get release branch ()captures the first release branch matching the pattern)
for /f "tokens=*" %%r in ('git branch --list release/*') do (
  set releaseBranch=%%r
  :: ensure that only the first matching branch is captured.
  goto foundBranch
)
:foundBranch

:: Check if the releaseBranch contains '* ' and strip it if it does
if "!releaseBranch:~0,2!"=="* " (
  :: removes the first two characters if the condition
  set releaseBranch=!releaseBranch:~2!
)

:: Shelve current workspace
echo You were working on branch %branch%
echo Stashing your work...
git stash
echo Checking out the release branch !releaseBranch!
git checkout "!releaseBranch!"
echo Pulling the latest changes (if any)
git pull

echo Proceeding with the main finish-release script...
call mvn gitflow:release-finish -DskipTestProject=true

:: Recover the workspace to the state it was, prior to running the script
echo Taking you back to your work on branch %branch%
git checkout "%branch%"
echo Applying your stashed work...
git stash apply

endlocal