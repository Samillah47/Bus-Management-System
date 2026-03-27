# Bus Management System

A REST API built with Spring Boot and PostgreSQL, secured with Spring Security, JWT Authentication, and OAuth2 Login (Google & GitHub).

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Security Architecture](#security-architecture)
- [Setup & Configuration](#setup--configuration)
- [API Endpoints](#api-endpoints)
- [Authentication & Authorization](#authentication--authorization)
- [OAuth2 Login](#oauth2-login)
- [Testing](#testing)
- [Exceptions](#exceptions)
- [Relationships](#relationships)

---

## Features

- **CRUD Operations** for Buses, Drivers, Routes, Schedules, Passengers, and Tickets
- **Spring Security** with stateless JWT authentication
- **Role-Based Access Control (RBAC)** with `@PreAuthorize` method-level security
- **OAuth2 Social Login** via Google and GitHub
- **Swagger/OpenAPI** documentation
- **Unit Tests** for secured endpoints using Spring Security Test

---

## Tech Stack

| Technology | Version |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.1 |
| Spring Security | 6.x |
| Spring Data JPA | 3.x |
| PostgreSQL | Runtime |
| JJWT | 0.12.5 |
| SpringDoc OpenAPI | 2.5.0 |
| Lombok | 1.18.30 |
| Maven | 3.x |

---

## Project Structure

```
src/main/java/com/example/BusManagementSystem/
├── config/
│   ├── SecurityConfig.java          # Security filter chain, CORS, auth providers
│   └── SwaggerConfig.java           # OpenAPI/Swagger configuration
├── controller/
│   ├── AuthController.java          # Login, Register, /api/me endpoints
│   ├── UserController.java          # User management (admin-only delete)
│   ├── BusController.java           # Bus CRUD
│   ├── DriverController.java        # Driver CRUD
│   ├── PassengerController.java     # Passenger CRUD
│   ├── RouteController.java         # Route CRUD
│   ├── ScheduleController.java      # Schedule CRUD
│   └── TicketController.java        # Ticket CRUD
├── dto/
│   ├── AuthDTO.java                 # LoginRequest, RegisterRequest, AuthResponse, UserProfileResponse
│   ├── RequestDTOs/                 # Request DTOs for each entity
│   └── ResponseDTOs/                # Response DTOs for each entity
├── exception/
│   ├── BadRequestException.java     # 400 errors
│   ├── ResourceNotFoundException.java # 404 errors
│   ├── DuplicateResourceException.java # 409 errors
│   ├── ErrorResponse.java           # Error response wrapper
│   └── GlobalExceptionHandler.java  # Centralized exception handling
├── model/
│   ├── User.java                    # Implements UserDetails + OAuth2User
│   ├── Role.java                    # Enum: ADMIN, PASSENGER, DRIVER
│   ├── Bus.java
│   ├── Driver.java
│   ├── Passenger.java
│   ├── Route.java
│   ├── Schedule.java
│   └── Ticket.java
├── repository/
│   ├── UserRepository.java
│   ├── BusRepository.java
│   ├── DriverRepository.java
│   ├── PassengerRepository.java
│   ├── RouteRepository.java
│   ├── ScheduleRepository.java
│   └── TicketRepository.java
├── security/
│   ├── JwtTokenProvider.java        # JWT generation, validation, claims extraction
│   ├── JwtAuthenticationFilter.java # OncePerRequestFilter for JWT validation
│   ├── CustomUserDetailsService.java # Loads User from database
│   ├── CustomOAuth2UserService.java # Handles OAuth2 user creation/lookup
│   ├── OAuth2SuccessHandler.java    # Issues JWT after OAuth2 login
│   └── OAuth2FailureHandler.java    # Handles OAuth2 login failures
├── service/                         # Service interfaces
└── serviceImpl/                     # Service implementations
```

---

## Security Architecture

### Filter Chain

Every HTTP request passes through the Spring Security filter chain before reaching controllers:

```
HTTP Request
  ↓
CorsFilter
  ↓
JwtAuthenticationFilter (Before UsernamePasswordAuthenticationFilter)
  ↓
Authorization Filter (URL-level rules from SecurityConfig)
  ↓
@PreAuthorize (Method-level rules on controllers)
  ↓
Controller
```

### SecurityConfig Highlights

- **CSRF Disabled** — acceptable for stateless REST APIs
- **Session Policy** — `STATELESS` (no HTTP sessions)
- **CORS** — enabled for all origins (configure for production)
- **JWT Filter** — registered before `UsernamePasswordAuthenticationFilter`
- **Method Security** — `@EnableMethodSecurity` enables `@PreAuthorize`

### URL-Level Access Rules

| Pattern | Access |
|---|---|
| `/api/auth/**` | Public (no authentication) |
| `/swagger-ui/**`, `/v3/api-docs/**` | Public |
| `/api/admin/**` | `ROLE_ADMIN` only |
| `/api/driver/**` | `ROLE_ADMIN` or `ROLE_DRIVER` |
| `/api/passenger/**` | `ROLE_ADMIN` or `ROLE_PASSENGER` |
| All other `/api/**` | Authenticated (any role) |

---

## Setup & Configuration

### Prerequisites

- Java 17+
- PostgreSQL database running on `localhost:5432`
- Maven 3.x

### Environment Variables

Create a `.env` file or set these environment variables:

```properties
DB_PASSWORD=your_postgres_password
JWT_SECRET=your_base64_encoded_256bit_secret
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
GITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
```

### Generate JWT Secret

```bash
openssl rand -base64 32
```

### Run the Application

```bash
./mvnw spring-boot:run
```

The application starts at `http://localhost:8080`.

### Swagger UI

Access API documentation at: `http://localhost:8080/swagger-ui.html`

---

## API Endpoints

### Authentication

| Method | URL | Description | Auth Required |
|---|---|---|---|
| POST | `/api/auth/register` | Register new user, returns JWT | No |
| POST | `/api/auth/login` | Login, returns JWT | No |
| GET | `/api/auth/me` | Get current user profile | Yes (JWT) |

### Users (Admin Management)

| Method | URL | Description | Auth Required |
|---|---|---|---|
| GET | `/api/users` | Get all users | No |
| GET | `/api/users/{id}` | Get user by ID | Yes |
| DELETE | `/api/users/{id}` | Delete user | Yes (`ROLE_ADMIN`) |

### Buses

| Method | URL | Description |
|---|---|---|
| POST | `/api/buses` | Create bus |
| GET | `/api/buses` | Get all buses |
| GET | `/api/buses/{id}` | Get bus by ID |
| PUT | `/api/buses/{id}` | Update bus |
| DELETE | `/api/buses/{id}` | Delete bus |

### Drivers

| Method | URL | Description |
|---|---|---|
| POST | `/api/drivers` | Create driver |
| GET | `/api/drivers` | Get all drivers |
| GET | `/api/drivers/{id}` | Get driver by ID |
| PUT | `/api/drivers/{id}` | Update driver |
| DELETE | `/api/drivers/{id}` | Delete driver |

### Routes

| Method | URL | Description |
|---|---|---|
| POST | `/api/routes` | Create route |
| GET | `/api/routes` | Get all routes |
| GET | `/api/routes/{id}` | Get route by ID |
| PUT | `/api/routes/{id}` | Update route |
| DELETE | `/api/routes/{id}` | Delete route |

### Schedules

| Method | URL | Description |
|---|---|---|
| POST | `/api/schedules` | Create schedule |
| GET | `/api/schedules` | Get all schedules |
| GET | `/api/schedules/{id}` | Get schedule by ID |
| GET | `/api/schedules/bus/{busId}` | Get schedules by bus |
| GET | `/api/schedules/driver/{driverId}` | Get schedules by driver |
| GET | `/api/schedules/route/{routeId}` | Get schedules by route |
| PUT | `/api/schedules/{id}` | Update schedule |
| DELETE | `/api/schedules/{id}` | Delete schedule |

### Passengers

| Method | URL | Description |
|---|---|---|
| POST | `/api/passengers` | Create passenger |
| GET | `/api/passengers` | Get all passengers |
| GET | `/api/passengers/{id}` | Get passenger by ID |
| PUT | `/api/passengers/{id}` | Update passenger |
| DELETE | `/api/passengers/{id}` | Delete passenger |

### Tickets

| Method | URL | Description |
|---|---|---|
| POST | `/api/tickets` | Book ticket |
| GET | `/api/tickets` | Get all tickets |
| GET | `/api/tickets/{id}` | Get ticket by ID |
| GET | `/api/tickets/passenger/{id}` | Get tickets by passenger |
| GET | `/api/tickets/schedule/{id}` | Get tickets by schedule |
| PUT | `/api/tickets/{id}` | Update ticket |
| DELETE | `/api/tickets/{id}` | Cancel ticket |

---

## Authentication & Authorization

### Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@example.com",
    "password": "password123",
    "role": "PASSENGER"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "john",
  "email": "john@example.com",
  "role": "PASSENGER"
}
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "password123"
  }'
```

### Access Protected Endpoints

```bash
curl http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### Admin-Only Endpoint

```bash
# Only users with ROLE_ADMIN can delete users
curl -X DELETE http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer <admin_jwt_token>"
```

### JWT Token Structure

The JWT contains the following claims:

```json
{
  "sub": "john",
  "roles": ["ROLE_PASSENGER"],
  "iat": 1700000000,
  "exp": 1700086400
}
```

- **sub**: Username (subject)
- **roles**: User's granted authorities
- **iat**: Issued at timestamp
- **exp**: Expiration timestamp (default: 24 hours)

### Role-Based Access Control

| Role | Permissions |
|---|---|
| `ROLE_ADMIN` | Full access to all endpoints, can delete users |
| `ROLE_PASSENGER` | Access to passenger-specific endpoints |
| `ROLE_DRIVER` | Access to driver-specific endpoints |

---

## OAuth2 Login

### Supported Providers

- **Google** — Full profile and email access
- **GitHub** — User email and profile access

### OAuth2 Flow

1. Navigate to: `http://localhost:8080/oauth2/authorization/google` (or `github`)
2. Google/GitHub login page appears
3. User authenticates with their social account
4. Redirected back to your app with an authorization code
5. Backend exchanges code for access token
6. Backend fetches user info and creates/updates local user
7. `OAuth2SuccessHandler` issues a JWT
8. JWT is returned in the JSON response

### OAuth2 Configuration (application.properties)

```properties
# Google
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.scope=email,profile
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google

# GitHub
spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
spring.security.oauth2.client.registration.github.scope=user:email
spring.security.oauth2.client.registration.github.redirect-uri=http://localhost:8080/login/oauth2/code/github
```

### Google Cloud Console Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project (or select existing)
3. Navigate to **APIs & Services → Credentials**
4. Create **OAuth 2.0 Client ID**
5. Set **Authorized redirect URIs**: `http://localhost:8080/login/oauth2/code/google`
6. Copy Client ID and Client Secret

### GitHub OAuth App Setup

1. Go to **GitHub → Settings → Developer Settings → OAuth Apps**
2. Click **New OAuth App**
3. Set **Authorization callback URL**: `http://localhost:8080/login/oauth2/code/github`
4. Copy Client ID and Client Secret

---

## Testing

### Run Tests

```bash
./mvnw test
```

### Test Coverage

| Test Class | What It Tests |
|---|---|
| `UserControllerTest` | Public GET `/api/users`, 403 for USER role on DELETE, 200 for ADMIN role on DELETE |
| `AuthControllerTest` | Login returns JWT on success, 400 on invalid credentials |

### Test Annotations Used

- `@WebMvcTest` — Loads only the web layer (controllers + security)
- `@WithMockUser(roles = "ADMIN")` — Injects a fake admin user into SecurityContext
- `@WithMockUser(roles = "USER")` — Injects a fake regular user
- `MockMvc` — Test HTTP client for calling endpoints and asserting responses

---

## Exceptions

| Exception | HTTP Status | When It's Thrown |
|---|---|---|
| `ResourceNotFoundException` | 404 Not Found | Resource doesn't exist by given ID |
| `DuplicateResourceException` | 409 Conflict | Resource already exists (e.g. duplicate bus number) |
| `BadRequestException` | 400 Bad Request | Invalid operation (e.g. booking a cancelled schedule, no available seats) |
| `Exception` | 500 Internal Server Error | Unexpected server errors |

---

## Relationships

- `Bus` → `Schedule`: One bus can have many schedules (`@OneToMany`)
- `Driver` → `Schedule`: One driver can have many schedules (`@OneToMany`)
- `Route` → `Schedule`: One route can have many schedules (`@OneToMany`)
- `Schedule` → `Ticket`: One schedule can have many tickets (`@OneToMany`)
- `Passenger` → `Ticket`: One passenger can have many tickets (`@OneToMany`)
- `User` → `Passenger`: Many users can reference one passenger (`@ManyToOne`)
- `User` → `Driver`: Many users can reference one driver (`@ManyToOne`)

---

## Submission Checklist

| Deliverable | Status |
|---|---|
| Complete Spring Boot project code (all parts) | ✅ |
| SecurityConfig with JWT filter chain configured | ✅ |
| JwtUtil, JwtAuthenticationFilter, AuthController | ✅ |
| Role-based access control on at least one endpoint | ✅ `@PreAuthorize("hasRole('ADMIN')")` on `DELETE /api/users/{id}` |
| OAuth2 / Google login configured and working | ✅ |
| GET `/api/me` endpoint returning authenticated user | ✅ |
| Unit tests for secured endpoints (Part 5) | ✅ |
| Postman collection or curl screenshots demonstrating: login, JWT usage, admin-only endpoint, Google OAuth2 flow | ⬜ (User to provide) |

---

## Key Learning Resources

- [Spring Security Reference](https://docs.spring.io/spring-security/reference/index.html)
- [Baeldung – Spring Security with JWT](https://www.baeldung.com/spring-security-oauth-jwt)
- [Spring Boot OAuth2 Login Guide](https://docs.spring.io/spring-security/reference/servlet/oauth2/login/index.html)
- [Spring Security Test Reference](https://docs.spring.io/spring-security/reference/servlet/test/index.html)
