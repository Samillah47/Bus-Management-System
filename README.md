# 🚌 Bus Management System — Security & Authentication

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-6DB33F?style=flat&logo=springsecurity)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=jsonwebtokens)
![OAuth2](https://img.shields.io/badge/OAuth2-000000?style=flat&logo=auth0)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat&logo=postgresql)
![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat&logo=openjdk)

## 📋 Table of Contents

- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Part 1: Spring Security](#-part-1-spring-security)
- [Part 2: JWT Authentication](#-part-2-jwt-authentication)
- [Part 3: Role-Based Access Control](#-part-3-role-based-access-control)
- [Part 4: OAuth2 Social Login](#-part-4-oauth2-social-login)
- [Part 5: Unit Testing](#-part-5-unit-testing)
- [API Endpoints](#-api-endpoints)

---

##  Overview

A secure REST API for managing buses, drivers, and passengers. Built with Spring Boot and secured using:

-  JWT stateless authentication
-  Role-Based Access Control (ADMIN, DRIVER, PASSENGER)
-  OAuth2 Social Login (Google + GitHub)
-  BCrypt password hashing
-  Environment variable secret management

---

##  Tech Stack

| Technology | Purpose |
|------------|---------|
| Spring Boot 3.x | Core framework |
| Spring Security 6.x | Authentication & Authorization |
| JWT (JJWT 0.12.3) | Stateless token authentication |
| OAuth2 Client | Google & GitHub social login |
| PostgreSQL | Database |

---

##  Project Structure

```
src/main/java/com/example/BusManagementSystem/
├── config/
│   └── SecurityConfig.java           ← Filter chain, OAuth2, RBAC
├── controller/
│   └── AuthController.java           ← All auth endpoints
├── model/
│   ├── User.java                     ← Implements UserDetails + OAuth2User
│   ├── Role.java                     ← ADMIN, DRIVER, PASSENGER
├── security/
│   ├── JwtTokenProvider.java         ← Generate & validate JWT
│   ├── JwtAuthenticationFilter.java  ← Reads Bearer token per request
│   ├── CustomUserDetailsService.java ← Loads user from DB by username
│   ├── CustomOAuth2UserService.java  ← Handles Google/GitHub user info
│   ├── OAuth2SuccessHandler.java     ← Issues JWT after social login
│   └── OAuth2FailureHandler.java     ← Handles OAuth2 errors
├── repository/
│   └── UserRepository.java           ← DB queries
└── dto/
    └── AuthDTO.java                  ← Request/Response objects
```

---

##  Part 1: Spring Security

### How it Works

![App Startup](screenshots/01-startup.png)

Every HTTP request passes through the Security Filter Chain before reaching your controllers.

```
Incoming Request
      │
      ▼
JwtAuthenticationFilter   ← Reads & validates Bearer token
      │
      ▼
AuthorizationFilter        ← Checks roles & permissions
      │
      ▼
Your Controller            ← Only reached if authorized
```

### Key Concepts Implemented

| Concept | Implementation |
|---------|----------------|
| Security Filter Chain | SecurityConfig.java |
| SecurityContext | Set in JwtAuthenticationFilter |
| UserDetailsService | CustomUserDetailsService.java |
| PasswordEncoder | BCryptPasswordEncoder |
| Session Management | IF_REQUIRED (needed for OAuth2 handshake) |


---

##  Part 2: JWT Authentication

### JWT Structure

```
┌─────────────────────────────────────────────────────────┐
│                    JWT Token Structure                   │
├─────────────────┬───────────────────┬───────────────────┤
│     HEADER      │      PAYLOAD      │     SIGNATURE     │
│                 │                   │                   │
│ {               │ {                 │ HMACSHA256(       │
│  "alg":"HS256"  │  "sub":"john",    │   base64(header)  │
│  "typ":"JWT"    │  "roles":         │   + "." +         │
│ }               │  ["ROLE_ADMIN"],  │   base64(payload) │
│                 │  "iat":1700000000 │   secret          │
│                 │  "exp":1700086400 │ )                 │
│                 │ }                 │                   │
└─────────────────┴───────────────────┴───────────────────┘
```

### Token Lifecycle

```
POST /api/auth/login
        │
        ▼
Server validates credentials
        │
        ▼
JWT Token issued (24 hour expiry)
        │
        ▼
Client sends: Authorization: Bearer <token>
        │
        ▼
JwtAuthenticationFilter validates on every request
        │
        ▼
Access granted or 401 Unauthorized
```

### Key Methods

```java
generateToken(UserDetails user)               // Creates signed JWT
extractUsername(String token)                 // Reads username from token
validateToken(String token, UserDetails user) // Checks signature + expiry
```

---

##  Part 3: Role-Based Access Control

### Role Permissions

```
┌─────────────────────────────────────────────┐
│              ROLE PERMISSIONS               │
├────────────┬────────────────────────────────┤
│   ADMIN    │ Full access to everything       │
│            │ /api/admin/**                   │
│            │ /api/driver/**                  │
│            │ /api/passenger/**               │
│            │ DELETE /api/users/{id}          │
├────────────┼────────────────────────────────┤
│   DRIVER   │ /api/driver/**                  │
│            │ Read-only on buses/routes       │
├────────────┼────────────────────────────────┤
│ PASSENGER  │ /api/passenger/**               │
│            │ View schedules, book tickets    │
└────────────┴────────────────────────────────┘
```

### Two Levels of Protection

**URL-Level (in SecurityConfig):**

```java
.requestMatchers("/api/admin/**").hasRole("ADMIN")
.requestMatchers("/api/driver/**").hasAnyRole("ADMIN", "DRIVER")
.requestMatchers("/api/passenger/**").hasAnyRole("ADMIN", "PASSENGER")
```

**Method-Level (on each endpoint):**

```java
@DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    // Only ADMIN reaches here
}

@GetMapping("/{id}")
@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
public ResponseEntity<?> getUserById(@PathVariable Long id) {
    // ADMIN sees any user, others see only themselves
}
```

### Role Enum

```java
public enum Role {
    ADMIN,      // Full system access
    PASSENGER,  // Can book tickets, view schedules
    DRIVER      // Can view routes, manage trips
}
```

---

##  Part 4: OAuth2 Social Login

### Supported Providers

![Google Login Page](screenshots/09-google-login-page.png)

| Provider | Trigger URL | Callback URL |
|----------|-------------|--------------|
| Google | `/oauth2/authorization/google` | `/login/oauth2/code/google` |
| GitHub | `/oauth2/authorization/github` | `/login/oauth2/code/github` |

### Full OAuth2 Flow

```
1. Open browser → /oauth2/authorization/google
         │
         ▼
2. Redirected to Google login page
         │
         ▼
3. Login with Google credentials
   (Your app NEVER sees the password)
         │
         ▼
4. Google redirects back with authorization code
         │
         ▼
5. CustomOAuth2UserService runs:
   ┌─────────────────────────────────────┐
   │ Email found in DB? → Return user    │
   │ Email NOT found?   → Create user    │
   └─────────────────────────────────────┘
         │
         ▼
6. OAuth2SuccessHandler generates JWT
         │
         ▼
7. Browser shows:
   {
     "token": "eyJhbGci...",
     "username": "yourname",
     "email": "you@gmail.com",
     "role": "PASSENGER"
   }
```

### Account Merging

```
GitHub Login  ──┐
                ├── Same Email ── Same User in DB ── Same JWT ✅
Google Login  ──┘

A user will NEVER accidentally create two accounts
by switching between Google and GitHub login.
```

![Google Token Response](screenshots/10-google-token-response.png)

![GitHub Token Response](screenshots/11-github-token-response.png)

![Database OAuth2 Users](screenshots/12-database-oauth2-users.png)

---

## 🧪 Part 5: Unit Testing

### Test Strategy

```
@WebMvcTest          ← Load only web layer (no full app)
@WithMockUser        ← Simulate authenticated user
MockMvc              ← Send fake HTTP requests
```

### Tests Written

```java
@WebMvcTest(UserController.class)
class UserControllerTest {

    // Test 1: Public endpoint works without auth
    @Test
    void getAllUsers_shouldReturn200_withoutAuthentication()

    // Test 2: PASSENGER cannot delete (403)
    @Test
    @WithMockUser(roles = "USER")
    void deleteUser_shouldReturn403_whenUserHasUserRole()

    // Test 3: ADMIN can delete (200)
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_shouldReturn200_whenUserHasAdminRole()

    // Test 4: Login returns JWT token
    @Test
    void login_shouldReturnJwt_whenCredentialsAreValid()
}
```

![Tests Passing](screenshots/13-tests-passing.png)
---

##  API Endpoints

| Method | URL | Auth | Role |
|--------|-----|------|------|
| POST | `/api/auth/register` | ❌ None | — |
| POST | `/api/auth/login` | ❌ None | — |
| GET | `/api/auth/me` | ✅ JWT | Any |
| GET | `/api/users` | ✅ JWT | ADMIN only |
| GET | `/api/users/{id}` | ✅ JWT | ADMIN / Own ID |
| PUT | `/api/users/{id}` | ✅ JWT | ADMIN / Own ID |
| DELETE | `/api/users/{id}` | ✅ JWT | ADMIN only |
| GET | `/api/buses` | ✅ JWT | Any |
| GET | `/api/buses/{id}` | ✅ JWT | Any |
| POST | `/api/buses` | ✅ JWT | ADMIN only |
| PUT | `/api/buses/{id}` | ✅ JWT | ADMIN only |
| DELETE | `/api/buses/{id}` | ✅ JWT | ADMIN only |
| GET | `/oauth2/authorization/google` | ❌ Browser | — |
| GET | `/oauth2/authorization/github` | ❌ Browser | — |

---

##  Testing Guide

### 1. Regular Authentication (Postman)

```
Step 1: Register
POST http://localhost:8080/api/auth/register
Body: {"username":"admin","email":"admin@example.com","password":"admin123","role":"ADMIN"}
Expected: 201 Created + JWT token

Step 2: Login
POST http://localhost:8080/api/auth/login
Body: {"username":"admin","password":"admin123"}
Expected: 200 OK + JWT token

Step 3: Use Token
GET http://localhost:8080/api/buses
Header: Authorization: Bearer eyJhbGci...
Expected: 200 OK + list of buses
```

![Register](screenshots/02-register.png)

![Login](screenshots/03-login.png)

![Protected Endpoint](screenshots/05-protected-endpoint.png)

![No Token 401](screenshots/06-no-token-401.png)

### 2. Role-Based Access (Postman)

```
Test ADMIN access (should succeed):
DELETE http://localhost:8080/api/users/2
Header: Authorization: Bearer <admin_token>
Expected: 200 OK

Test PASSENGER access (should fail):
DELETE http://localhost:8080/api/users/2
Header: Authorization: Bearer <passenger_token>
Expected: 403 Forbidden
```

![Admin Delete 200](screenshots/07-admin-delete-200.png)

![Passenger Delete 403](screenshots/08-passenger-delete-403.png)

### 3. OAuth2 Flow (Browser)

```
Step 1: Open browser
        → http://localhost:8080/oauth2/authorization/google
        → http://localhost:8080/oauth2/authorization/github

Step 2: Log in with your Google/GitHub account

Step 3: Receive JSON with JWT token in browser

Step 4: Copy token → Use in Postman for protected endpoints
```

### 4. Postman Auto-Token Setup (Pro Tip)

In the Tests tab of your Login/OAuth2 request:

```javascript
var jsonData = pm.response.json();
if (jsonData.token) {
    pm.environment.set("jwt_token", jsonData.token);
    console.log("Token saved automatically!");
}
```

Then in all other requests, set Authorization to `Bearer {{jwt_token}}`.

### Quick Status Reference

| HTTP Status | Meaning | Action |
|-------------|---------|--------|
| 200 OK | ✅ Success | All good |
| 201 Created | ✅ Resource created | Registration worked |
| 401 Unauthorized | ❌ No/invalid token | Re-login and get new token |
| 403 Forbidden | ❌ Wrong role | User doesn't have permission |
| 404 Not Found | ❌ Wrong URL | Check the endpoint URL |

---

##  Security Notes

| Consideration | Implementation in This Project |
|---------------|--------------------------------|
| Password Storage | BCrypt hashing  |
| Token Expiry | 24 hours (86400000ms)  |
| Secret Management | Environment variables  |
| OAuth2 Passwords | Never seen by our app (Google/GitHub handles it) |
| SQL Injection | Protected by JPA/Hibernate parameterized queries |



---

##  Dependencies

```xml
<!-- Core -->
<dependency>spring-boot-starter-web</dependency>
<dependency>spring-boot-starter-data-jpa</dependency>
<dependency>postgresql</dependency>

<!-- Security -->
<dependency>spring-boot-starter-security</dependency>
<dependency>spring-boot-starter-oauth2-client</dependency>

<!-- JWT -->
<dependency>jjwt-api:0.12.3</dependency>
<dependency>jjwt-impl:0.12.3</dependency>
<dependency>jjwt-jackson:0.12.3</dependency>

<!-- Utilities -->
<dependency>lombok</dependency>
<dependency>spring-boot-starter-validation</dependency>

<!-- Testing -->
<dependency>spring-boot-starter-test</dependency>
<dependency>spring-security-test</dependency>
```

---

**She Can Code — Week 10 Assignment | Spring Boot Security: Authentication & OAuth2**
