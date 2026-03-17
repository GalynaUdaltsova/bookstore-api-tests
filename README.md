# Bookstore API Test Automation Framework

API test automation framework for Bookstore REST API built with Java, JUnit 5, RestAssured, Maven, Allure, Docker, and GitHub Actions.

## Tech Stack

- **Java 21**
- **JUnit 5**
- **RestAssured 5.3.0**
- **Maven 3.8+**
- **Jackson 2.15.2**
- **Allure Report 2.24.0**
- **Docker**
- **GitHub Actions**

## Setup

### Requirements:

- Java 21
- Maven 3.8+
- Docker 
- Allure CLI (for local report generation)

### Project Setup

**Clone repository:**
```bash
git clone https://github.com/GalynaUdaltsova/bookstore-api-tests.git
```

**Navigate to project folder:**
```bash
cd bookstore-api-tests
```

**Install dependencies and build project:**
```bash
mvn clean install
```

## Test Design

The framework covers different categories of API tests.

### Happy Path
Verifies successful API behavior with valid input data.

**Example:**
- Create book successfully

### Negative Tests
Verify API error handling with invalid input.

**Example:**
- Invalid publish date
- Missing required fields

### Edge Cases
Test boundary values and unusual input.

**Example:**
- Maximum title length

## Test Data

Test data is generated using Builder pattern, which allows creating flexible and reusable test objects.

**Example:**

```java
Book book = BookBuilder.randomBook();
```

This approach helps generate realistic data for API requests and simplifies test setup.

## Running Tests
### Environment Configuration

The API base URL is stored as a GitHub Secret to demonstrate working with secure configuration.

Secret name:
API_BASE_URL

It is passed as environment variable to Docker container during execution on GitHub Actions

Example value:
https://fakerestapi.azurewebsites.net/api/v1

### Running Tests locally
Locally the local.properties will be loaded as default for simplicity

mvn clean test

**Run all tests:**
```bash
mvn clean test
```

**Run smoke tests:**
```bash
mvn test -Dgroups=smoke
```

**Run tests by tag:**
```bash
mvn test -Dgroups=books
```

**Run a specific test class:**
```bash
mvn test -Dtest=CreateBooksApiTests
```

**Run a single test method:**
```bash
mvn test -Dtest=CreateBooksApiTests#testCreateBook_Success
```

## Allure Reporting

**Run tests and generate results:**
```bash
mvn clean test
```

```bash
mvn allure:serve
```

## Docker Execution

**Build Docker image:**
```bash
docker build -t bookstore-api-tests .
```

**Run tests in container:**
```bash
docker run --rm bookstore-api-tests
```

## CI/CD

Tests are executed using GitHub Actions.

The pipeline includes:
- project build
- test execution inside Docker
- Allure report generation
- GitHub Pages report deployment

Tests execution will be triggered:
- on push to main/develop branches
- on pull request creation
- on schedule (every Monday 2 AM UTC)
- manually via workflow_dispatch with user inputs

### Environment Variables

- `ENV` - Environment (qa/staging/prod)
- `API_BASE_URL` - API endpoint URL
