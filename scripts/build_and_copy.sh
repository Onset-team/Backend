#!/usr/bin/env bash
set -eux

if [ -x ./mvnw ]; then
  chmod +x mvnw
  ./mvnw -B -DskipTests package
  JAR=$(ls -1 target/*.jar | head -n1)
elif [ -x ./gradlew ]; then
  chmod +x gradlew
  ./gradlew --no-daemon build -x test
  JAR=$(ls -1 build/libs/*.jar | head -n1)
else
  echo "No mvnw/gradlew found in repo root" >&2
  exit 1
fi

install -o stoov -g stoov -m 640 "$JAR" /opt/stoov/app.jar

# .env는 보존하되 권한만 정리(있을 때만)
if [ -f /opt/stoov/.env ]; then
  chown stoov:stoov /opt/stoov/.env
  chmod 640 /opt/stoov/.env
fi