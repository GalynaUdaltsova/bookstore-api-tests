# Build Stage
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven config and source
COPY pom.xml .
COPY src ./src

# Download dependencies offline
RUN mvn dependency:go-offline -B

# Compile only (no tests during build)
RUN mvn clean compile -B

# Runtime Stage
FROM eclipse-temurin:21-jdk AS runtime

WORKDIR /app

# Install required tools and Allure CLI
RUN apt-get update && apt-get install -y curl bash unzip maven && rm -rf /var/lib/apt/lists/* \
    && curl -Lo allure-commandline.zip https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/2.24.0/allure-commandline-2.24.0.zip \
    && unzip allure-commandline.zip \
    && mv allure-2.24.0 /allure \
    && rm allure-commandline.zip \
    && ln -s /allure/bin/allure /usr/local/bin/allure

# Copy compiled classes, test classes, and results from build
COPY --from=build /app/target ./target
COPY --from=build /app/src ./src
COPY pom.xml .

# Add non-root user
RUN addgroup --gid 1001 testuser && adduser --uid 1001 --ingroup testuser --disabled-password --gecos "" testuser

# Create necessary directories for Allure
RUN mkdir -p /root/.allure /app/target/allure-results /app/target/site && \
    chown -R testuser:testuser /app /root/.allure

USER testuser

# Set env vars
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV ALLURE_RESULTS_DIR="/app/target/allure-results"

# Healthcheck (optional, checks JUnit CLI version)
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD java -cp target/classes:target/dependency/*:target/test-classes org.junit.platform.console.ConsoleLauncher --version || exit 1

# Run tests once
CMD ["mvn", "test"]