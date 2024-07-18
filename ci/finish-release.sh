#!/usr/bin/bash

# Function to display error and exit
exit_on_failure() {
    echo "$1 failed"
    exit 1
}

# Shelve current workspace
branch=$(git branch --show-current)
echo "You were working on branch $branch"

# Get the first release branch matching the pattern
# awk prints the last field of the line, stripping out any leading * and whitespace, leaving only the branch name
releaseBranch=$(git branch --list 'release/*' | head -1 | awk '{print $NF}')

if [ -z "$releaseBranch" ]; then
    exit_on_failure "No release/* branch found. Git branch --list for release branches"
fi

echo 'Stashing your work...'
if ! git stash; then
    exit_on_failure "Ensure you have no uncommitted changes. Stashing your work"
fi

echo "Checking out the release branch $releaseBranch"
if ! git checkout "$releaseBranch"; then
    exit_on_failure "Checkout to release branch"
fi

echo "Pulling the latest changes (if any)"
if ! git pull origin "$releaseBranch"; then
    exit_on_failure "Pulling latest changes"
fi

echo 'Proceeding with the main finish-release script...'
if ! mvn gitflow:release-finish -DskipTestProject=true; then
    exit_on_failure "Maven release finish"
fi

# Recover the workspace to the state it was, prior to running the script
echo "Taking you back to your work on branch $branch"
if ! git checkout "$branch"; then
    exit_on_failure "Checkout back to branch"
fi

echo 'Applying your stashed work...'
if ! git stash apply; then
    exit_on_failure "Applying stashed work"
fi