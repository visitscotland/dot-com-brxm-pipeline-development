git checkout main
git pull
git checkout develop
git pull
call mvn gitflow:release-start --batch-mode
call mvn versions:use-releases scm:checkin -Dmessage="Updated snapshot dependencies to release versions" -DpushChanges=false