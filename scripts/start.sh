#!/usr/bin/env bash
set -e
if [ ! -f /opt/stoov/.env ]; then
  echo "WARNING: /opt/stoov/.env not found. Create it before starting." >&2
fi
systemctl restart stoov
