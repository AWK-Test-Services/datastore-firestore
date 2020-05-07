#!/usr/bin/env bash

export PROJECT_ID=featrs-uat
export IMAGE=gcr.io/$PROJECT_ID/data-service
export VERSION=`mvn -q  -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec`

gcloud config set project --no-user-output-enabled "$PROJECT_ID"

# Push the resulting docker image to Cloud Registry
docker push $IMAGE:$VERSION

# Deploy & start the image
gcloud beta run deploy --platform managed --allow-unauthenticated --image $IMAGE:$VERSION --set-env-vars DISABLE_SIGNAL_HANDLERS=foobar

#docker run -d --rm -p8180:8080 -v $GOOGLE_APPLICATION_CREDENTIALS:/tmp/keys/google-key.json:ro -e GOOGLE_APPLICATION_CREDENTIALS=/tmp/keys/google-key.json --name $SERVICE gcr.io/$PROJECT_ID/$SERVICE
