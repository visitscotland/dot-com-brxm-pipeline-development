#!/usr/bin/bash
#🔹 Script's points of focus
#✔ Bug-Free Boolean Handling → hasStashedChanges=0 ensures stash is applied only when needed.
#✔ "if" clauses use the exit code of a command (0 is true and any other exit code means that the command has failed)
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
# Covers both cases: missing branches and corrupted refs
# Ensures git rev-parse can verify the branch exists before proceeding (rare edge cases due to a corrupt repository or missing refs.)
if [[ -z "$releaseBranch" || ! $(git rev-parse --verify "$releaseBranch" 2>/dev/null) ]]; then
    exit_on_failure "No active or valid release/* branch found. Verify with 'git branch --list release/*'"
fi

echo "🔍 Checking workspace for changes..."

#################################################################################
# ✅ git diff --quiet checks for unstaged changes
# ✅ git diff --cached --quiet checks for staged changes
# ✅ Only runs git stash if there are actual modifications
# ✅ Prevents unnecessary error messages when there’s nothing to stash
# ######################## 🎯 Summary of Exit Codes ##########################
# Scenario: git diff --quiet | git diff --cached --quiet | git stash > /dev/null 2>&1
# No changes: 0 |	0 |	0
# Unstaged changes: 1 | 0 | 0
# Staged changes: 0 | 1 | 0
# Untracked files: 0 | 0 | 0
# Ignored files: 0 | 0 | 0
# Detached HEAD: N/A | N/A | 0
# Corrupt repo: N/A | N/A | 128
# Read-only filesystem: N/A | N/A | 1
# Corrupt stash stack: N/A | N/A | 1
#################################################################################
# Check for local changes and store the outcome
# Both diff commands must return 0 (no changes) to skip stashing
# If either returns 1 (indicating changes), the stash operation is performed
# Parentheses ensure correct evaluation order
#################################################################################
if ! (git diff --quiet && git diff --cached --quiet); then
    echo "📌 Local changes detected. Attempting to stash changes..."
    if git stash > /dev/null 2>&1; then
        hasStashedChanges=1
    else
        exit_on_failure "Ensure you have no untracked changes before stashing."
    fi
else
    echo "✅ No local changes to stash."
fi

echo "📂 Checking out the release branch: $releaseBranch"
git checkout "$releaseBranch" || exit_on_failure "Failed to checkout release branch"

echo "🔄 Pulling the latest changes (if any)..."
# Ensures only fast-forward updates are applied (prevents merge commits in history)
# If git pull fails due to connectivity issues, the script does not assume "no new changes."
if ! git pull --ff-only origin "$releaseBranch"; then
    echo "⚠️ Warning: Could not pull latest changes. Check network connectivity or verify branch status."
fi

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
#    git stash apply && git stash drop > /dev/null 2>&1 || exit_on_failure "Applying stashed work failed (possible conflicts detected)"
    # - Prevent stash drop from spamming the console with unnecessary output
    # - Only runs exit_on_failure if git stash apply fails.
    # - Prevents git stash drop failures from incorrectly triggering exit_on_failure.
    # - Logs a warning instead of failing the script if git stash drop encounters an issue.
    if git stash apply; then
        # Edge scenario: 'git stash apply' works but doesn't apply anything
        # Improvement to consider: use the exact ID for the stash, rather than stash@{0} which is the latest one
        if git stash list | grep -q "stash@{0}"; then
                git stash drop > /dev/null 2>&1 || echo "⚠️ Warning: Failed to drop stash, but proceeding..."
            else
                echo "⚠️ Warning: No stash entry found after applying. It may have already been dropped."
        fi
    else
        exit_on_failure "Applying stashed work failed (possible conflicts detected)"
    fi
else
    echo "✅ No local changes to apply from the stash"
fi

currentBranch=$(git branch --show-current)
echo "🎯 Process complete! You are now on branch: $currentBranch"

end_time=$(date +%s)
elapsed_time=$((end_time - start_time))
echo "⏳ Total execution time: ${elapsed_time}s"