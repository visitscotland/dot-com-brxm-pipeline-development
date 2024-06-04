#!/usr/bin/bash
git checkout main
git pull
git checkout develop
git pull
mvn gitflow:release-start --batch-mode
mvn versions:use-releases scm:checkin -Dmessage="Updated snapshot dependencies to release versions" -DpushChanges=false