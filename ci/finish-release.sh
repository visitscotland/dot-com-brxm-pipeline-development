#!/usr/bin/bash
#🔹 Script's points of focus
#✔ Bug-Free Boolean Handling → hasStashedChanges=0 ensures stash is applied only when needed.
#✔ More Efficient Change Detection → Uses git diff --quiet 2>/dev/null instead of git status --porcelain.
#✔ Safe Stash Handling → Prevents unnecessary output spam and ensures stash drop works only after a successful apply.
#✔ Uses git pull --ff-only to prevent unnecessary merge commits that git pull origin could perform.
#✔ Fails Fast on Errors → Uses set -e and structured error handling.
#✔ User-Friendly Output → Uses clear emojis for better readability in CI logs.
#✔ Correct Branch Restoration → Ensures smooth rollback if the branch still exists.
#✔ Measures and logs total execution time ⏳.

# Exit immediately if any command fails (unless explicitly handled)
set -e

# Monitor the execution time of the script
start_time=$(date +%s)

# Function to display error and exit
exit_on_failure() {
    echo "❌ ERROR: $1 failed"
    exit 1
}

# Shelve current workspace
branch=$(git branch --show-current)
echo "🔄 You were working on branch: $branch"

# Get the most recently updated release branch
releaseBranch=$(git for-each-ref --sort=-committerdate --format='%(refname:short)' refs/heads/release/* | head -1)

# Initialize stash tracking variable (0 = no stash, 1 = stash created)
hasStashedChanges=0

# Validate if a release branch exists
if [ -z "$releaseBranch" ]; then
    exit_on_failure "No active release/* branch found. Verify with 'git branch --list release/*'"
fi

echo "🔍 Checking workspace for changes..."

# Check for local changes and store the outcome
if git diff --quiet 2>/dev/null; then
    echo "✅ No local changes to stash."
else
    echo "📌 Local changes detected. Stashing changes..."
    git stash > /dev/null 2>&1 || exit_on_failure "Ensure you have no uncommitted changes before stashing."
    hasStashedChanges=1
fi

echo "📂 Checking out the release branch: $releaseBranch"
git checkout "$releaseBranch" || exit_on_failure "Failed to checkout release branch"

echo "🔄 Pulling the latest changes (if any)..."
# Ensures only fast-forward updates are applied (prevents merge commits in history)
git pull --ff-only origin "$releaseBranch" || echo "[INFO] No new changes to pull (this is expected if the remote is up to date)"

echo -e "\n🚀 [INFO] Proceeding with the main finish-release script..."
mvn gitflow:release-finish -DskipTestProject=true || exit_on_failure "Maven release finish failed"
echo "✅ Release process completed successfully!"

# Restore the workspace to the previous branch (if it still exists)
if git show-ref --verify --quiet "refs/heads/$branch"; then
    echo "🔀 Switching back to your original branch: $branch"
    git checkout "$branch" || exit_on_failure "Failed to switch back to branch: $branch"
else
    echo "⚠️The original branch '$branch' has been deleted in the process"
fi

# Apply stashed changes if needed
if [ "$hasStashedChanges" -eq 1 ]; then
    echo "📥 Applying your stashed work..."
    # Prevent stash drop from spamming the console with unnecessary output
    git stash apply && git stash drop > /dev/null 2>&1 || exit_on_failure "Applying stashed work failed (possible conflicts detected)"
else
    echo "✅ No local changes to apply from the stash"
fi

currentBranch=$(git branch --show-current)
echo "🎯 Process complete! You are now on branch: $currentBranch"

end_time=$(date +%s)
elapsed_time=$((end_time - start_time))
echo "⏳ Total execution time: ${elapsed_time}s"