#!/usr/bin/env bash

export PROJECT_ID=featrz-uat
export SERVICE=data-service
export GOOGLE_APPLICATION_CREDENTIALS="/Users/arjan/Projects/Feature/datastore_firestore/data-user.json"

export VERSION=`mvn -q  -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec`
export IP_ADDRESS=`ifconfig | grep 192.168.1 | cut -d' ' -f2`

# Run local
echo "Starting data-service on $IP_ADDRESS"
docker run -d --rm -p8180:8080 -v $GOOGLE_APPLICATION_CREDENTIALS:/tmp/keys/google-key.json:ro -e GOOGLE_APPLICATION_CREDENTIALS=/tmp/keys/google-key.json --name $SERVICE gcr.io/$PROJECT_ID/$SERVICE
docker logs -f $SERVICE

