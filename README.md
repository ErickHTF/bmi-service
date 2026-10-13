# bmi-service
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
[![CI](https://github.com/ErickHTF/bmi-service/actions/workflows/ci.yml/badge.svg)](https://github.com/ErickHTF/bmi-service/actions/workflows/ci.yml)

RESTful API built with Java Spring Boot for user management and BMI calculation using PostgreSQL and Docker.

This project was developed for educational purposes, focusing on applying software development best practices, containerization,
and clean architecture. It provides a full CRUD for user management and a specific logic for calculating Body Mass Index (BMI)
with its WHO classification.

## Table of Contents

- [Background](#background)
- [Install](#install)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Error Handling](#error-handling)
- [API](#api)


## Background

The main motivation for this repository is to practice and demonstrate proficiency in backend development using the Spring Ecosystem. It ensures a consistent development environment through Docker containerization for the database, avoiding local installation issues.

It addresses the need for a simple, reliable API to manage user data and perform health-related calculations (BMI) following the REST architectural style.

## Install

To run this project, you need the following installed:
* Java JDK 17+
* Docker & Docker Compose

Maven does not need to be installed: the project ships with the Maven Wrapper (`./mvnw`).



### Database Setup

The project uses a `docker-compose.yml` file to start the PostgreSQL database. The host mapped port is **5433**.

```bash
# In the project root, run:
docker compose up -d

```
>   **Note:** The credentials configured in the container are:
>   * **User:** `imc_user`
>   * **Password:** `imc_pass`
>   * **Database:** `crudusers`
>   * **Port:** `5433`

## Usage

With the database running, you can start the application via Maven wrapper:
```Bash
./mvnw spring-boot:run
```
The application will be accessible at `http://localhost:8080`.

### CLI (Curl Examples)

You can interact with the API using `curl`. Here is an example of creating a user:

```bash
curl -i -X POST http://localhost:8080/api/users \
   -H "Content-Type: application/json" \
   -d '{"name": "John Doe", "age": 30, "weight": 80.0, "height": 1.80}'
```

The API answers `201 Created` with a `Location: /api/users/{id}` header and the created user:

```json
{"id": 1, "name": "John Doe", "age": 30, "weight": 80.0, "height": 1.8}
```

Calculating a BMI (weight in kilograms, height in meters):

```bash
curl -X POST http://localhost:8080/api/bmi \
   -H "Content-Type: application/json" \
   -d '{"weight": 80.0, "height": 1.80}'
```

```json
{"bmi": 24.69, "classification": "Normal weight"}
```

### Running the tests

```bash
./mvnw verify
```

Unit and web-layer tests run without any infrastructure. The full application test starts a
PostgreSQL container with [Testcontainers](https://testcontainers.com/) and is skipped automatically
when Docker is not available.

## Project Structure

The project follows the Spring Boot layered architecture (base package `com.bmiservice`):

  * `controller`: Entry layer (REST endpoints).
  * `service`: Business logic (user management, BMI calculation).
  * `repository`: Spring Data JPA repositories.
  * `model`: JPA entities and domain types (`User`, `BmiClassification`).
  * `dto`: Request/response records validated with Bean Validation (e.g., `UserRequest`, `BmiResponse`).
    JPA entities are never exposed directly by the API.
  * `exception`: Global error handling and custom exceptions.

## Error Handling

The API features a **Global Exception Handler** (`RestControllerAdvice`). Every error uses the same JSON body:

```json
{
  "timestamp": "2025-01-01T12:00:00Z",
  "status": 400,
  "message": "Validation failed",
  "path": "/api/users",
  "fieldErrors": {"height": "must be greater than 0"}
}
```

  * **Validation errors:** HTTP 400 with one message per invalid field in `fieldErrors`.
  * **Malformed JSON / invalid path parameters:** HTTP 400.
  * **ResourceNotFoundException:** HTTP 404 when a user is not found.
  * **Generic Exception:** HTTP 500 for unexpected server errors (details are logged, not returned).

## API

For detailed documentation and interactive testing, this project uses Swagger/OpenAPI.

  * **Swagger UI:** `http://localhost:8080/swagger-ui.html`

### Endpoints Overview

#### Users (`/api/users`)

| Method | Route | Description |
| :--- | :--- | :--- |
| `GET` | `/api/users` | Returns a list of all users. |
| `GET` | `/api/users/{id}` | Retrieves a specific user by ID (`404` if missing). |
| `POST` | `/api/users` | Creates a new user (`201` + `Location` header). |
| `PUT` | `/api/users/{id}` | Updates an existing user's data. |
| `DELETE` | `/api/users/{id}` | Removes a user from the database (`204`). |

Request body for `POST`/`PUT`: `name` (required, max 100 chars), `age` (1–150), `weight` in kg (> 0, max 500)
and `height` in meters (> 0, max 3.0).

#### BMI Calculation (`/api/bmi`)

| Method | Route | Description |
| :--- | :--- | :--- |
| `POST` | `/api/bmi` | Calculates the BMI (`weight / height²`) from a JSON body with `weight` (kg) and `height` (m). |

The classification follows the WHO adult ranges:

| BMI | Classification |
| :--- | :--- |
| < 18.5 | Underweight |
| 18.5 – 24.99 | Normal weight |
| 25.0 – 29.99 | Overweight |
| 30.0 – 34.99 | Obesity class I |
| 35.0 – 39.99 | Obesity class II |
| ≥ 40.0 | Obesity class III |
