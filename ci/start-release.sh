#!/usr/bin/bash

# Shelve current workspace
branch=$(git branch --show-current)
echo "You were working on branch $branch"
echo 'Stashing your work...'
git stash

echo 'Proceeding with the main start-release script...'
git checkout main
git pull
git checkout develop
git pull
mvn gitflow:release-start --batch-mode
mvn versions:use-releases scm:checkin -Dmessage="Updated snapshot dependencies to release versions" -DpushChanges=false

# Recover the workspace to the state it was, prior to running the script
echo "Taking you back to your work on branch $branch"
git checkout "$branch"
echo 'Applying your stashed work...'
git stash apply