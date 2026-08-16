#!/bin/bash
# Build and deploy backend to Railway
# Prerequisites: Railway CLI installed and logged in (railway login)

echo "Building backend..."
cd "$(dirname "$0")"
mvn clean package -DskipTests

echo "Deploying to Railway..."
railway up

echo "Done! Your backend is now live."
