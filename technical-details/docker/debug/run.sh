#!/bin/bash
echo "Starting system via docker compose."
docker-compose up -d

echo "Deploying settings file."
docker exec mbl-debug mkdir -p /opt/mazebert
docker cp settings.json mbl-debug:/opt/mazebert/settings.json

echo "Packaging maven project."
cd ../../..
mvn clean package -DskipTests=true

echo "Deploying war file."
cd target
docker cp mbl.war mbl-debug:/usr/local/tomcat/webapps/ROOT.war