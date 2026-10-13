# Development guide

[← Back to README](../README.md)

## Install

To run this project, you need the following installed:

- Java JDK 17+
- Docker & Docker Compose

Maven does not need to be installed: the project ships with the Maven Wrapper (`./mvnw`).

### Database Setup

The project uses a `docker-compose.yml` file to start the PostgreSQL database. The host mapped port is **5433**.

```bash
# In the project root, run:
docker compose up -d

```

> **Note:** The credentials configured in the container are:
>
> - **User:** `imc_user`
> - **Password:** `imc_pass`
> - **Database:** `crudusers`
> - **Port:** `5433`

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
{ "id": 1, "name": "John Doe", "age": 30, "weight": 80.0, "height": 1.8 }
```

Calculating a BMI (weight in kilograms, height in meters):

```bash
curl -X POST http://localhost:8080/api/bmi \
   -H "Content-Type: application/json" \
   -d '{"weight": 80.0, "height": 1.80}'
```

```json
{ "bmi": 24.69, "classification": "Normal weight" }
```

### Running the tests

```bash
./mvnw verify
```

Unit and web-layer tests run without any infrastructure. The full application test starts a
PostgreSQL container with [Testcontainers](https://testcontainers.com/) and is skipped automatically
when Docker is not available.
