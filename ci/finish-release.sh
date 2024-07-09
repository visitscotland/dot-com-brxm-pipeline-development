#!/usr/bin/bash

# Shelve current workspace
branch=$(git branch --show-current)
# awk prints the last field of the line, stripping out any leading * and whitespace, leaving only the branch name
releaseBranch=$(git branch --list 'release/*' | head -1 | awk '{print $NF}')

echo "You were working on branch $branch"
echo Stashing your work...
git stash
echo "Checking out the release branch $releaseBranch"
git checkout  "$releaseBranch"
echo "Pulling the latest changes (if any)"
git pull

echo 'Proceeding with the main finish-release script...'
mvn gitflow:release-finish -DskipTestProject=true

# Recover the workspace to the state it was, prior to running the script
echo "Taking you back to your work on branch $branch"
git checkout "$branch"
echo 'Applying your stashed work...'
git stash apply