#!/bin/bash
set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $SCRIPT_DIR

CONTAINER=mbl-dev

echo "Building image."
cd image
docker build -t mbl-dev:latest .
cd ..

echo "Starting system via docker compose."
docker-compose up -d

echo "Deploying settings file."
docker exec $CONTAINER mkdir -p /opt/mazebert
docker cp settings.json $CONTAINER:/opt/mazebert/settings.json

echo "Deploying war file."
docker cp mbl.war $CONTAINER:/usr/local/tomcat/webapps/ROOT.war