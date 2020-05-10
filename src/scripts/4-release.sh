#!/usr/bin/env bash


export PROJECT_ID=featrs
gcloud config set project --no-user-output-enabled "$PROJECT_ID"

# Tag the last staging image
docker tag gcr.io/featrz-uat/data-service:latest gcr.io/$PROJECT_ID/data-service

# Push the image to Cloud Registry
docker push gcr.io/$PROJECT_ID/data-service

# Deploy & start the image
gcloud beta run deploy --platform managed --no-allow-unauthenticated --image gcr.io/$PROJECT_ID/data-service
