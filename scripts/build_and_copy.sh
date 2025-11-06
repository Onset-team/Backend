#!/usr/bin/env bash
set -euo pipefail

# 1) gradlew 또는 mvnw 자동 탐지 (최대 3단계 하위)
WRAPPER_FILE="$(find . -maxdepth 3 -type f \( -name 'gradlew' -o -name 'mvnw' \) | head -n1 || true)"

if [ -z "$WRAPPER_FILE" ]; then
  echo "No mvnw/gradlew found in repository (searched up to depth 3)"
  exit 1
fi

APP_DIR="$(dirname "$WRAPPER_FILE")"
cd "$APP_DIR"

# 2) 실행권한 부여
chmod +x ./gradlew 2>/dev/null || true
chmod +x ./mvnw 2>/dev/null || true

# 3) 빌드
JAR_PATH=""
if [ -f "./gradlew" ]; then
  ./gradlew bootJar -x test --no-daemon
  JAR_PATH="$(ls -1 build/libs/*.jar | head -n1)"
elif [ -f "./mvnw" ]; then
  ./mvnw -DskipTests package
  JAR_PATH="$(ls -1 target/*.jar | head -n1)"
fi

if [ -z "$JAR_PATH" ] || [ ! -f "$JAR_PATH" ]; then
  echo "JAR not found after build"
  exit 1
fi

# 4) 배치
sudo mkdir -p /opt/stoov
sudo cp -f "$JAR_PATH" /opt/stoov/app.jar
echo "Copied $(basename "$JAR_PATH") to /opt/stoov/app.jar"
