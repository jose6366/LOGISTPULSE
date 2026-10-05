#!/usr/bin/env sh
set -eu
echo "Health"
curl -fsS http://localhost:8081/health
echo

echo "Authenticated list"
curl -fsS -u operator:operator123 http://localhost:8081/api/orders
echo
