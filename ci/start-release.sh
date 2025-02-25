#!/usr/bin/bash
#🔹 Script's points of focus
#✔ Bug-Free Boolean Handling → hasStashedChanges=0 ensures stash is applied only when needed.
#✔ Only runs git stash if local changes exist (git diff --quiet)
#✔ More Efficient Change Detection → Uses git diff --quiet 2>/dev/null instead of git stash blindly.
#✔ Safe Stash Handling → Prevents unnecessary output spam and ensures stash drop works only after a successful apply.
#✔ Uses git pull --ff-only to prevent unnecessary merge commits that git pull origin could perform.
#✔ Fails Fast on Errors → Uses set -e and structured error handling.
#✔ User-Friendly Output → Uses clear emojis for better readability in CI logs.
#✔ Correct Branch Restoration → Ensures smooth rollback if the branch still exists.
#✔ Notifies the user of the newly created release branch
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

# Initialize stash tracking variable (0 = no stash, 1 = stash created)
hasStashedChanges=0

echo "🔍 Checking workspace for changes..."

# Check for local changes and store the outcome
if git diff --quiet 2>/dev/null; then
    echo "✅ No local changes to stash."
else
    echo "📌 Local changes detected. Stashing changes..."
    git stash > /dev/null 2>&1 || exit_on_failure "Ensure you have no uncommitted changes before stashing."
    hasStashedChanges=1
fi

echo "📂 Switching to main branch..."
git checkout main || exit_on_failure "Failed to checkout main"
git pull --ff-only origin main || echo "[INFO] No new changes to pull from main"

echo "📂 Switching to develop branch..."
git checkout develop || exit_on_failure "Failed to checkout develop"
git pull --ff-only origin develop || echo "[INFO] No new changes to pull from develop"

echo -e "\n🚀 [INFO] Proceeding with the main start-release script..."
mvn gitflow:release-start --batch-mode || exit_on_failure "Maven release start failed"
mvn versions:use-releases scm:checkin -Dmessage="Updated snapshot dependencies to release versions" -DpushChanges=false || exit_on_failure "Maven versions use-releases and scm checkin failed"

# Detect the newly created release branch
newReleaseBranch=$(git for-each-ref --sort=-committerdate --format='%(refname:short)' refs/heads/release/* | head -1)

# Restore the workspace to the previous branch
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

if [ -n "$newReleaseBranch" ]; then
    echo "📢 Release branch created: **$newReleaseBranch**"
    echo "📂 You can visit it by running: git checkout $newReleaseBranch"
else
    echo "⚠️No new release branch detected! Please verify manually."
fi

currentBranch=$(git branch --show-current)
echo "🎯 Process complete! You are now on branch: $currentBranch"

echo 'To follow the progress of the build, visit https://jenkinssb.visitscotland.com/job/release-brc.visitscotland.com/'

end_time=$(date +%s)
elapsed_time=$((end_time - start_time))
echo "⏳ Total execution time: ${elapsed_time}s"
