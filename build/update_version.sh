#!/bin/bash

set -e

if [ "$1" == "" ]; then
    echo "Usage: $0 VERSION"
    exit 1
fi

mvn -B tycho-versions:set-version -Dtycho.localArtifacts=ignore -DnewVersion=$1
mvn -B tycho-versions:update-pom -Dtycho.localArtifacts=ignore
