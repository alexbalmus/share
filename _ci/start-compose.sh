#!/usr/bin/env bash

export DOCKER_COMPOSE_PATH=$1

if [ -z "$DOCKER_COMPOSE_PATH" ]
then
  echo "Please provide path to docker-compose.yml: \"${0##*/} /path/to/docker-compose.yml\""
  exit 1
fi

echo "Starting Share stack in ${DOCKER_COMPOSE_PATH}"

# substitude all '/' to '-' as Docker doesn't allow it
TAG_NAME=`echo $TRAVIS_BRANCH | tr / -

# Change tag if you are on a branch
if [ ! -z "$TRAVIS_BRANCH" -a "$TRAVIS_BRANCH" != "master" ]; then
  sed  -i "s/image: alfresco/alfresco-share:latest/image: alfresco/alfresco-share:latest-$TAG_NAME/" ${DOCKER_COMPOSE_PATH}
fi

# .env files are picked up from project directory correctly on docker-compose 1.23.0+
docker-compose --file "${DOCKER_COMPOSE_PATH}" --project-directory $(dirname "${DOCKER_COMPOSE_PATH}") up -d

if [ $? -eq 0 ]
then
  echo "Docker Compose started ok"
else
  echo "Docker Compose failed to start" >&2
  exit 1
fi