# Dockerfile to build the WSL Language Support JetBrains plugin
# Targets the NEW API: IntelliJ Platform 2025.2 and later (builds 252+)
# For older JetBrains versions (pre-2025.2), use Dockerfile instead.
#
# Usage:
#   Build the image and export the plugin ZIP to ./output:
#     docker build -f Dockerfile.new-api --output=./output --target=export .
#
#   Or just build the builder stage and inspect it interactively:
#     docker build -f Dockerfile.new-api --target=builder -t jetbrains-wsl-plugin-new-api .
#     docker run --rm -v "$(pwd)/output:/output" jetbrains-wsl-plugin-new-api \
#       cp -r /app/build/distributions/. /output

FROM eclipse-temurin:21-jdk AS builder

# Install required tools
RUN apt-get update && \
    apt-get install -y --no-install-recommends unzip wget

# Set working directory
WORKDIR /app

# Copy Gradle wrapper files first for layer caching
COPY 2025.2.gradle/ gradle/
COPY gradlew ./

# Make gradlew executable
RUN chmod +x gradlew

# Download Gradle distribution (warm up the wrapper cache)
RUN ./gradlew --version

# Copy the rest of the project sources
COPY 2025.2.build.gradle.kts settings.gradle.kts 2025.2.gradle.properties ./
RUN mv 2025.2.build.gradle.kts build.gradle.kts && \
    mv 2025.2.gradle.properties gradle.properties

COPY src/ src/

# Build the plugin targeting the new API (IntelliJ Platform 2025.2+)
# Override the gradle.properties defaults to target build 252 and later
RUN ./gradlew buildPlugin --no-daemon --stacktrace \
    -PintellijPlatformVersion=2025.2 \
    -PpluginSinceBuild=252 \
    -PpluginUntilBuild=

# ── Export stage ──────────────────────────────────────────────────────────────
FROM scratch AS export

COPY --from=builder /app/build/distributions/ /
