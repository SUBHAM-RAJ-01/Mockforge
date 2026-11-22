# MockForge Dockerfile
# Multi-stage build for optimized production image

# Stage 1: Build backend
FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /workspace/backend

# Copy POM first for dependency caching
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY backend/src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Build frontend
FROM node:20-alpine AS frontend-build
WORKDIR /workspace/frontend

# Copy package files for dependency caching
COPY frontend/package*.json ./
RUN npm ci

# Copy source and build
COPY frontend/ ./
RUN npm run build

# Stage 3: Runtime
FROM eclipse-temurin:21-jre-jammy

# Create non-root user
RUN useradd --system --uid 10001 --create-home mockforge

# Set working directory
WORKDIR /opt/mockforge

# Create data directory
RUN mkdir -p /var/lib/mockforge && \
    chown -R mockforge:mockforge /opt/mockforge /var/lib/mockforge

# Copy built artifacts
COPY --from=backend-build --chown=mockforge:mockforge \
    /workspace/backend/target/*.jar ./app.jar

COPY --from=frontend-build --chown=mockforge:mockforge \
    /workspace/frontend/dist ./static

# Switch to non-root user
USER mockforge

# Environment variables
ENV MOCKFORGE_HOME=/var/lib/mockforge \
    MOCKFORGE_PORT=8080 \
    MOCKFORGE_BIND_ADDRESS=0.0.0.0 \
    JAVA_OPTS="-XX:MaxRAMPercentage=70 -XX:+UseG1GC"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
    CMD curl -f http://localhost:8080/_mockforge/health || exit 1

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
