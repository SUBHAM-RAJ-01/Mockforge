#!/bin/bash
# Test backend build locally

echo "Testing Maven build..."
cd backend

echo "Step 1: Downloading dependencies..."
mvn dependency:resolve -B

echo "Step 2: Running tests..."
mvn test -B

echo "Step 3: Building package..."
mvn package -DskipTests -B

echo "✅ Build successful!"
echo "JAR file created at: target/*.jar"
