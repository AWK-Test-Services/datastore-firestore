#!/usr/bin/env bash

export PROJECT_ID=featrz-uat
export SERVICE=data-service
export VERSION=`mvn -q  -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec`
export IMAGE=gcr.io/$PROJECT_ID/data-service

gcloud config set project --no-user-output-enabled "$PROJECT_ID"

mvn clean package
if [ $? != 0 ]
then
    exit "Failed: " + $?
fi

# Remove old images
docker rmi $IMAGE:latest $IMAGE:$VERSION

# Build (and tag) docker image
docker build -f src/main/docker/Dockerfile.jvm -t $IMAGE:latest .
docker tag $IMAGE:latest $IMAGE:$VERSION

