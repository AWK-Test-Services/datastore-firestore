#!/usr/bin/env bash

export PROJECT_ID=featrz-uat
export IMAGE=gcr.io/$PROJECT_ID/data-service
export VERSION=`mvn -q  -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec`

gcloud config set project --no-user-output-enabled "$PROJECT_ID"

# Push the resulting docker image to Cloud Registry
docker push $IMAGE:$VERSION

# Deploy & start the image
gcloud beta run deploy data-service --platform managed --no-allow-unauthenticated --image $IMAGE:$VERSION --set-env-vars DISABLE_SIGNAL_HANDLERS=foobar
gcloud run services add-iam-policy-binding data-service --platform managed --member='serviceAccount:82030072436-compute@developer.gserviceaccount.com' --role='roles/run.invoker'
