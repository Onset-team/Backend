#!/usr/bin/env bash
set -e
curl -fsS http://localhost:8080/actuator/health | grep -q '"status":"UP"'