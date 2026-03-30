# Smart Service Booking

Smart Service Booking is a role-based web platform for discovering local services and booking providers.

It includes:
- A Java Spring Boot backend with JWT authentication and PostgreSQL persistence.
- A static HTML/CSS/JavaScript frontend with separate dashboards for users, providers, and admins.

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.4.3
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Gradle

### Frontend
- HTML5
- CSS3
- Vanilla JavaScript

## Project Structure

```text
Smart Service Booking/
|- backend/      # Spring Boot API and business logic
|- frontend/     # Static client app (role-based pages)
```

## Key Features

- Authentication and authorization with JWT.
- Role-based flows for:
  - User: browse services, create bookings, manage profile.
  - Provider: manage offered services and bookings.
  - Admin: manage users/providers/bookings/reviews.
- Service browsing and search.
- Booking lifecycle management.
- Ratings and reviews.

## Prerequisites

Install the following before running:
- Java 21
- PostgreSQL (running locally)
- A browser
- Optional (recommended): VS Code Live Server extension for frontend

## Configuration

Main backend configuration is in:
- `backend/src/main/resources/application.properties`

Current defaults include:
- Server port: `8080`
- Database URL: `jdbc:postgresql://localhost:5432/smart-service-booking`
- API base expected by frontend: `http://localhost:8080/api`

### 1) Create PostgreSQL Database

Create the database used by the app:

```sql
CREATE DATABASE "smart-service-booking";
```

### 2) Update Database Credentials

Edit `backend/src/main/resources/application.properties` and set:

- `spring.datasource.username`
- `spring.datasource.password`

## Run the Backend

From the `backend` directory:

### Windows (PowerShell)

```powershell
.\gradlew.bat bootRun
```

### macOS/Linux

```bash
./gradlew bootRun
```

Backend starts at:
- `http://localhost:8080`

## Run the Frontend

Serve the `frontend` directory with a static server.

### Option A: VS Code Live Server
1. Open the `frontend` folder in VS Code.
2. Start Live Server on `index.html`.

### Option B: Python HTTP Server

From the `frontend` directory:

```bash
python -m http.server 5500
```

Then open:
- `http://localhost:5500`

## Default Frontend Entry Points

- Landing page: `frontend/index.html`
- User pages: `frontend/user/`
- Provider pages: `frontend/provider/`
- Admin pages: `frontend/admin/`

## API Notes

The frontend uses:
- `frontend/assets/js/api.js`

with base URL:
- `http://localhost:8080/api`

If backend host/port changes, update `API_BASE` in that file.

## Build and Test (Backend)

From `backend`:

```bash
./gradlew test
```

or on Windows:

```powershell
.\gradlew.bat test
```

## Security Note

`application.properties` currently contains a plain JWT secret and local DB credentials. For production:
- Move secrets to environment variables or a secure secret manager.
- Use strict CORS origin settings.
- Use HTTPS and production-grade database credentials.

## License

Add your preferred license information here.
