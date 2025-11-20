#!/usr/bin/env bash
set -euo pipefail

URL="http://127.0.0.1:8080/actuator/health"

# 최대 60회(약 2분) 재시도
for i in {1..60}; do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$URL" || echo "000")

  echo "Health check [$i/60] → HTTP $STATUS"

  if [ "$STATUS" = "200" ]; then
    echo "Health OK"
    exit 0
  fi

  sleep 2
done

echo "Health check failed. Recent logs:"
journalctl -u "$SVC" -n 200 --no-pager || true
exit 1
