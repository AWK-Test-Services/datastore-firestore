#!/usr/bin/env bash


export PROJECT_ID=featrz
export IMAGE=gcr.io/$PROJECT_ID/data-service
export VERSION=`mvn -q  -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec`

gcloud config set project --no-user-output-enabled "$PROJECT_ID"
gcloud config set run/platform managed

# Tag the last staging image
docker tag gcr.io/featrz-uat/data-service:$VERSION $IMAGE:$VERSION

# Push the image to Cloud Registry
docker push $IMAGE:$VERSION

# Deploy & start the image
gcloud beta run deploy data-service --no-allow-unauthenticated --image $IMAGE:$VERSION
gcloud run services add-iam-policy-binding data-service --member='serviceAccount:636326476202-compute@developer.gserviceaccount.com' --role='roles/run.invoker'
