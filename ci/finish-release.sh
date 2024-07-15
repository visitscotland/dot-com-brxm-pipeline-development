#!/usr/bin/bash

# Shelve current workspace
branch=$(git branch --show-current)
# awk prints the last field of the line, stripping out any leading * and whitespace, leaving only the branch name
releaseBranch=$(git branch --list 'release/*' | head -1 | awk '{print $NF}')

if [ -z "$releaseBranch" ]; then
    echo "No release branch found."
    exit 1
fi

echo "You were working on branch $branch"
echo Stashing your work...
if ! git stash; then
    echo 'Failed to stash changes. Ensure you have no uncommitted changes.'
    exit 1
fi

echo "Checking out the release branch $releaseBranch"
if ! git checkout "$releaseBranch"; then
    echo 'Checkout failed'
    exit 1
fi

echo "Pulling the latest changes (if any)"
if ! git pull origin "$releaseBranch"; then
    echo 'Pull failed'
    exit 1
fi

echo 'Proceeding with the main finish-release script...'
if ! mvn gitflow:release-finish -DskipTestProject=true; then
    echo 'Release finish failed'
    exit 1
fi

# Recover the workspace to the state it was, prior to running the script
echo "Taking you back to your work on branch $branch"
if ! git checkout "$branch"; then
    echo 'Checkout back to branch failed'
    exit 1
fi

echo 'Applying your stashed work...'
if ! git stash apply; then
    echo 'Stash apply failed'
    exit 1
fi