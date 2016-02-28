#!/bin/bash
docker-compose up -d

cd ../../..
mvn clean package -DskipTests=true

cd target

echo "Deploying war file."

docker cp mazebert-ladder-0.1.0.war mazebert-ladder-dev:/usr/local/tomcat/webapps/ROOT.war