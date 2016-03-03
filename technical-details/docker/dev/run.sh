#!/bin/bash
echo "Starting system via docker compose."
docker-compose up -d

echo "Deploying settings file."
docker exec mazebert-ladder-dev mkdir -p /opt/mazebert
docker cp settings.json mazebert-ladder-dev:/opt/mazebert/settings.json

echo "Packaging maven project."
cd ../../..
mvn clean package -DskipTests=true

echo "Deploying war file."
cd target
docker cp mazebert-ladder-0.1.0.war mazebert-ladder-dev:/usr/local/tomcat/webapps/ROOT.war