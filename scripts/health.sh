#!/usr/bin/env bash
set -euo pipefail

SVC="stoov"
URL="http://127.0.0.1:8080/actuator/health"

# 최대 60회(약 2분) 재시도
for i in {1..60}; do
  # 서비스가 살아있는지 확인
  if systemctl is-active --quiet "$SVC"; then
    if curl -fsS --max-time 2 "$URL" | grep -q '"status":"UP"'; then
      echo "Health OK"
      exit 0
    fi
  fi
  echo "[$i/60] waiting for $SVC health..."
  sleep 2
done

echo "Health check failed. Recent logs:"
journalctl -u "$SVC" -n 200 --no-pager || true
exit 1