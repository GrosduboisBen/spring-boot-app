# API Routes Documentation

This document describes all REST API routes provided by the controllers in `src/main/java/com/example/demo/controller`.

---

## Getting Started

To run this Spring Boot application in development and execute tests, follow these steps:

### Prerequisites

- Java 17 or later installed
- Maven 3.8+ installed

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on [http://localhost:8080](http://localhost:8080) by default.

### Running Tests

```bash
mvn test
```

This command will execute all unit and integration tests.

---

**Base path:** `/api/companies`

| Method | Path              | Description                | Request Body         | Response Body         |
|--------|-------------------|----------------------------|----------------------|-----------------------|
| GET    | `/api/companies`  | List all companies         | -                    | `List<CompanyResponse>` |
| GET    | `/api/companies/{id}` | Get company by ID      | -                    | `CompanyResponse`     |
| POST   | `/api/companies`  | Create a new company       | `CompanyRequest`     | `CompanyResponse`     |
| PUT    | `/api/companies/{id}` | Update company by ID  | `CompanyRequest`     | `CompanyResponse`     |
| DELETE | `/api/companies/{id}` | Delete company by ID  | -                    | - (204 No Content)    |

---

## EvaluationController

**Base path:** `/api/evaluations`

| Method | Path                    | Description                | Request Body           | Response Body           |
|--------|-------------------------|----------------------------|------------------------|-------------------------|
| GET    | `/api/evaluations`      | List all evaluations       | -                      | `List<EvaluationResponse>` |
| GET    | `/api/evaluations/{id}` | Get evaluation by ID       | -                      | `EvaluationResponse`    |
| POST   | `/api/evaluations`      | Create a new evaluation    | `EvaluationRequest`    | `EvaluationResponse`    |
| PUT    | `/api/evaluations/{id}` | Update evaluation by ID    | `EvaluationRequest`    | `EvaluationResponse`    |
| DELETE | `/api/evaluations/{id}` | Delete evaluation by ID    | -                      | - (204 No Content)      |

---

## HelloController

| Method | Path         | Description         | Response Body           |
|--------|--------------|---------------------|-------------------------|
| GET    | `/api/hello` | Simple hello route  | `String`                |

---

## MissionController

**Base path:** `/api/missions`

| Method | Path                  | Description                | Request Body         | Response Body         |
|--------|-----------------------|----------------------------|----------------------|-----------------------|
| GET    | `/api/missions`       | List all missions          | -                    | `List<MissionResponse>` |
| GET    | `/api/missions/{id}`  | Get mission by ID          | -                    | `MissionResponse`     |
| POST   | `/api/missions`       | Create a new mission       | `MissionRequest`     | `MissionResponse`     |
| PUT    | `/api/missions/{id}`  | Update mission by ID       | `MissionRequest`     | `MissionResponse`     |
| DELETE | `/api/missions/{id}`  | Delete mission by ID       | -                    | - (204 No Content)    |

---

## OrderController

**Base path:** `/api/orders`

| Method | Path                | Description                | Request Body         | Response Body         |
|--------|---------------------|----------------------------|----------------------|-----------------------|
| GET    | `/api/orders`       | List all orders            | -                    | `List<OrderResponse>` |
| GET    | `/api/orders/{id}`  | Get order by ID            | -                    | `OrderResponse`       |
| POST   | `/api/orders`       | Create a new order         | `OrderRequest`       | `OrderResponse`       |
| PUT    | `/api/orders/{id}`  | Update order by ID         | `OrderRequest`       | `OrderResponse`       |
| DELETE | `/api/orders/{id}`  | Delete order by ID         | -                    | - (204 No Content)    |

---

## ProjectController

**Base path:** `/api/projects`

| Method | Path                  | Description                | Request Body         | Response Body         |
|--------|-----------------------|----------------------------|----------------------|-----------------------|
| GET    | `/api/projects`       | List all projects          | -                    | `List<ProjectResponse>` |
| GET    | `/api/projects/{id}`  | Get project by ID          | -                    | `ProjectResponse`     |
| POST   | `/api/projects`       | Create a new project       | `ProjectRequest`     | `ProjectResponse`     |
| PUT    | `/api/projects/{id}`  | Update project by ID       | `ProjectRequest`     | `ProjectResponse`     |
| DELETE | `/api/projects/{id}`  | Delete project by ID       | -                    | - (204 No Content)    |

---

## ProviderController

**Base path:** `/api/providers`

| Method | Path                    | Description                | Request Body         | Response Body         |
|--------|-------------------------|----------------------------|----------------------|-----------------------|
| GET    | `/api/providers`        | List all providers         | -                    | `List<ProviderResponse>` |
| GET    | `/api/providers/{id}`   | Get provider by ID         | -                    | `ProviderResponse`    |
| POST   | `/api/providers`        | Create a new provider      | `ProviderRequest`    | `ProviderResponse`    |
| PUT    | `/api/providers/{id}`   | Update provider by ID      | `ProviderRequest`    | `ProviderResponse`    |
| DELETE | `/api/providers/{id}`   | Delete provider by ID      | -                    | - (204 No Content)    |

---

## UserController

**Base path:** `/api/users`

| Method | Path           | Description                | Request Body     | Response Body     |
|--------|---------------|----------------------------|------------------|-------------------|
| GET    | `/api/users`  | List all users             | -                | `List<User>`      |
| POST   | `/api/users`  | Create a new user          | `User`           | `User`            |

---

**Note:**  
- `{id}` denotes a path variable (resource identifier).
- Request/response types like `CompanyRequest`, `OrderResponse`, etc., refer to DTOs defined in your codebase.
- All DELETE endpoints return HTTP 204 No Content on success.
- All endpoints use JSON for request and response bodies.
- Error responses (e.g., 404 Not Found, 400 Bad Request) are returned as appropriate.
