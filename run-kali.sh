#!/usr/bin/env bash
set -euo pipefail

# Resolve script directory so it works from anywhere
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

ENV_FILE="${1:-.env}"

if [[ ! -f "$ENV_FILE" ]]; then
  echo "ERROR: Env file not found: $ENV_FILE"
  echo "Usage: ./run-kali.sh [path-to-env-file]"
  exit 1
fi

echo "[INFO] Loading environment from: $ENV_FILE"
set -a
# Normalize CRLF -> LF before sourcing to avoid: $'\r': command not found
# shellcheck disable=SC1091
source <(sed -e 's/\r$//' "$ENV_FILE")
set +a

if [[ -x "./mvnw" ]]; then
  echo "[INFO] Starting Spring Boot with Maven Wrapper..."
  ./mvnw spring-boot:run
else
  echo "[INFO] Starting Spring Boot with system Maven..."
  mvn spring-boot:run
fi

