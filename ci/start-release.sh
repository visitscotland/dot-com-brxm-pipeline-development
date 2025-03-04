#!/usr/bin/bash

# Function to display error and exit
exit_on_failure() {
    echo "$1 failed"
    exit 1
}

# Shelve current workspace
branch=$(git branch --show-current)
# Initialize stash tracking variable (0 = no stash, 1 = stash created)
hasStashedChanges=0

# Check for local changes
# git status --porcelain provides a machine-readable output of Git status
# If there are any changes, the output will not be empty (yes → Run git stash, no → Skip stashing)
if [[ -n $(git status --porcelain) ]]; then
    echo "You were working on branch $branch"
    echo "Local changes detected. Stashing your work..."
    if ! git stash; then
        exit_on_failure "Local changes detected. Stashing your work..."
    fi
    hasStashedChanges=1
else
    echo "No local changes to stash."
fi

echo 'Proceeding with the main start-release script...'
if ! git checkout main; then
    exit_on_failure "Checkout to main"
fi

if ! git pull origin main; then
    exit_on_failure "Pulling main"
fi

if ! git checkout develop; then
    exit_on_failure "Checkout to develop"
fi

if ! git pull origin develop; then
    exit_on_failure "Pulling develop"
fi

if ! mvn gitflow:release-start --batch-mode; then
    exit_on_failure "Maven release start"
fi

if ! mvn versions:use-releases scm:checkin -Dmessage="Updated snapshot dependencies to release versions" -DpushChanges=false; then
    exit_on_failure "Maven versions use-releases and scm checkin"
fi

echo "Taking you back to your work on branch $branch"
if ! git checkout "$branch"; then
    exit_on_failure "Checkout back to branch"
fi

# Apply stashed changes, if any
if [ "$hasStashedChanges" -eq 1 ]; then
    echo "Applying your stashed work..."
    if ! git stash apply; then
        exit_on_failure "Applying your stashed work"
    fi
else
    echo "No local changes to apply from the stash"
fi

echo 'You can follow the progress of the artefacts at https://jenkinssb.visitscotland.com/job/release-brc.visitscotland.com/'
