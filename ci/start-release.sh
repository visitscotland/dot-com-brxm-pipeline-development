#!/usr/bin/bash

# Function to display error and exit
exit_on_failure() {
    echo "$1 failed"
    exit 1
}

# Shelve current workspace
branch=$(git branch --show-current)
echo "You were working on branch $branch"
echo 'Stashing your work...'
if ! git stash; then
    exit_on_failure "Stashing your work"
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

echo 'Applying your stashed work...'
if ! git stash apply; then
    exit_on_failure "Applying stashed work"
fi

echo 'You can follow the progress of the artefacts at https://jenkinssb.visitscotland.com/job/release-brc.visitscotland.com/'
