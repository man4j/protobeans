#!/bin/bash

docker service create --name my_java_service --log-opt max-size=10m --log-opt max-file=10 \
-e "logdog=javaservice" \
man4j/my_java_service:13