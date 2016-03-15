#!/bin/bash
set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $SCRIPT_DIR

HOST=root@mazebert.com
PORT=53480
DEPLOY_DIR=/opt/mbl-dev
URL=https://ladderdev.mazebert.com

echo "Packaging maven project."
cd ../../..
mvn clean package -DskipTests=true
cd $SCRIPT_DIR

echo "Preparing remote directory."
ssh -p $PORT $HOST "rm -rf $DEPLOY_DIR"
ssh -p $PORT $HOST "mkdir -p $DEPLOY_DIR"

echo "Copying files to remote directory."
scp -P $PORT -r image $HOST:$DEPLOY_DIR/image
scp -P $PORT docker-compose.yml $HOST:$DEPLOY_DIR
scp -P $PORT settings.json $HOST:$DEPLOY_DIR
scp -P $PORT run.sh $HOST:$DEPLOY_DIR
scp -P $PORT ../../../target/mbl.war $HOST:$DEPLOY_DIR

echo "Deploying files to container."
ssh -p $PORT $HOST "$DEPLOY_DIR/run.sh"

printf "Wait for service to be alive."
until $(curl --output /dev/null --silent --head --fail $URL/rest/version); do
    printf "."
    sleep 3
done
echo ""
echo "Service can be reached."
echo ""

echo "Fetching ladder version..."
echo $(curl --silent $URL/rest/version)
echo ""

echo "Fetching ladder status..."
echo $(curl --silent $URL/rest/status)
echo ""

echo "Have a nice day and happy building :-)"