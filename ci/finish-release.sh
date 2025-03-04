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

# Get the first release branch matching the pattern
# awk prints the last field of the line, stripping out any leading * and whitespace, leaving only the branch name
releaseBranch=$(git branch --list 'release/*' | head -1 | awk '{print $NF}')

if [ -z "$releaseBranch" ]; then
    exit_on_failure "No release/* branch found. Git branch --list for release branches"
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
# (check if the branch that the user was working on still exists, prior to checkout)
if git show-ref --verify --quiet "refs/heads/$branch"; then
    echo "Switching back to your original branch: $branch"
    git checkout "$branch" || exit_on_failure "Failed to switch back to branch: $branch"
else
    echo "The original branch '$branch' has been deleted as part of the process"
    branch=$(git branch --show-current)
    echo "Your current branch is: '$branch'"
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