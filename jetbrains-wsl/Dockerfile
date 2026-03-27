# Dockerfile to build the WSL Language Support JetBrains plugin
#
# Usage:
#   Build the image and export the plugin ZIP to ./output:
#     docker build --output=./output --target=export .
#
#   Or just build the builder stage and inspect it interactively:
#     docker build --target=builder -t jetbrains-wsl-plugin .
#     docker run --rm -v "$(pwd)/output:/output" jetbrains-wsl-plugin \
#       cp -r /app/build/distributions/. /output

FROM eclipse-temurin:17-jdk AS builder

# Install required tools
RUN apt-get update && \
    apt-get install -y --no-install-recommends unzip wget && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy Gradle wrapper files first for layer caching
COPY gradle/ gradle/
COPY gradlew ./

# Make gradlew executable
RUN chmod +x gradlew

# Download Gradle distribution (warm up the wrapper cache)
RUN ./gradlew --version

# Copy the rest of the project sources
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY src/ src/

# Build the plugin (produces build/distributions/*.zip)
RUN ./gradlew buildPlugin --no-daemon --stacktrace

# ── Export stage ──────────────────────────────────────────────────────────────
FROM scratch AS export

COPY --from=builder /app/build/distributions/ /
