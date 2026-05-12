#!/bin/bash
# Generate a self-signed PKCS12 keystore for the backend
RES_DIR="src/main/resources"
mkdir -p "$RES_DIR"

echo "🔐 Generating Backend SSL Keystore..."
# Delete existing alias if it exists to avoid conflicts
keytool -delete -alias ems-backend -keystore "$RES_DIR/keystore.p12" -storepass changeit 2>/dev/null || true

keytool -genkeypair -alias ems-backend -keyalg RSA -keysize 2048 -storetype PKCS12 \
  -keystore "$RES_DIR/keystore.p12" -validity 3650 -storepass changeit \
  -dname "CN=localhost, OU=Backend, O=EMS, L=AddisAbaba, ST=AA, C=ET" \
  -ext "SAN=dns:localhost,ip:127.0.0.1"

echo "✅ Backend Keystore created at $RES_DIR/keystore.p12"
