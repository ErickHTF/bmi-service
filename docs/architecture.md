# Architecture

[← Back to README](../README.md)

## Project Structure

The project follows the Spring Boot layered architecture (base package `com.bmiservice`):

- `controller`: Entry layer (REST endpoints).
- `service`: Business logic (user management, BMI calculation).
- `repository`: Spring Data JPA repositories.
- `model`: JPA entities and domain types (`User`, `BmiClassification`).
- `dto`: Request/response records validated with Bean Validation (e.g., `UserRequest`, `BmiResponse`).
  JPA entities are never exposed directly by the API.
- `exception`: Global error handling and custom exceptions.
