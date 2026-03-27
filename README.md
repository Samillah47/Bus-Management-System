# Bus Management System

A REST API built with Spring Boot and PostgreSQL.

---

## Exceptions

| Exception | HTTP Status | When it's thrown |
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

---

## API Endpoints

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
