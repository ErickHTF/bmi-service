# API reference

[← Back to README](../README.md)

## Error Handling

The API features a **Global Exception Handler** (`RestControllerAdvice`). Every error uses the same JSON body:

```json
{
  "timestamp": "2025-01-01T12:00:00Z",
  "status": 400,
  "message": "Validation failed",
  "path": "/api/users",
  "fieldErrors": { "height": "must be greater than 0" }
}
```

- **Validation errors:** HTTP 400 with one message per invalid field in `fieldErrors`.
- **Malformed JSON / invalid path parameters:** HTTP 400.
- **ResourceNotFoundException:** HTTP 404 when a user is not found.
- **Generic Exception:** HTTP 500 for unexpected server errors (details are logged, not returned).

## API

For detailed documentation and interactive testing, this project uses Swagger/OpenAPI.

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`

### Endpoints Overview

#### Users (`/api/users`)

| Method   | Route             | Description                                         |
| :------- | :---------------- | :-------------------------------------------------- |
| `GET`    | `/api/users`      | Returns a list of all users.                        |
| `GET`    | `/api/users/{id}` | Retrieves a specific user by ID (`404` if missing). |
| `POST`   | `/api/users`      | Creates a new user (`201` + `Location` header).     |
| `PUT`    | `/api/users/{id}` | Updates an existing user's data.                    |
| `DELETE` | `/api/users/{id}` | Removes a user from the database (`204`).           |

Request body for `POST`/`PUT`: `name` (required, max 100 chars), `age` (1–150), `weight` in kg (> 0, max 500)
and `height` in meters (> 0, max 3.0).

#### BMI Calculation (`/api/bmi`)

| Method | Route      | Description                                                                                   |
| :----- | :--------- | :-------------------------------------------------------------------------------------------- |
| `POST` | `/api/bmi` | Calculates the BMI (`weight / height²`) from a JSON body with `weight` (kg) and `height` (m). |

The classification follows the WHO adult ranges:

| BMI          | Classification    |
| :----------- | :---------------- |
| < 18.5       | Underweight       |
| 18.5 – 24.99 | Normal weight     |
| 25.0 – 29.99 | Overweight        |
| 30.0 – 34.99 | Obesity class I   |
| 35.0 – 39.99 | Obesity class II  |
| ≥ 40.0       | Obesity class III |
