#!/usr/bin/env bash

_term() {
  echo "Caught SIGTERM signal!"
  kill -TERM "$child" 2>/dev/null
  wait "$child"
}

trap _term SIGTERM SIGINT

APP_HOME=`dirname "$0"`
cd $APP_HOME
envsubst < config/application.tpl.yml > config/application.yml
java  --add-modules java.se --add-exports java.base/jdk.internal.ref=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.management/sun.management=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
  -Dhazelcast.shutdownhook.policy=GRACEFUL \
  -jar app.jar &

child=$!
wait "$child"
