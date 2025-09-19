# TeLlevo Admin Backend

Spring Boot backend application for TeLlevo administration frontend.

## Project Overview

This is a Spring Boot 3.5.3 application using Java 24 that provides authentication services for the TeLlevo administration frontend.

## 🌍 Environments

### Production
- **Domain**: https://admin.tellevoapp.com
- **API Base URL**: https://admin.tellevoapp.com/api
- **CORS**: Configured for production domain
- **Security**: JWT authentication with HTTPS

### Development
- **URL**: http://localhost:8080
- **API Base URL**: http://localhost:8080/api
- **CORS**: Configured for local Vue.js development server (port 5173)
- **Hot Reload**: Enabled for rapid development

### Environment-Specific Configuration
```properties
# CORS configuration for both environments
cors.allowed-origins=http://localhost:5173,https://admin.tellevoapp.com
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.allow-credentials=true

# Profile-specific settings
spring.profiles.active=${SPRING_PROFILE:dev}
```

## What's Implemented

### ✅ Completed Features

- **Spring Boot 3.5.3** with Java 24
- **JWT Authentication** system
- **CORS Configuration** for Vue.js frontend
- **Login API endpoint** (`POST /api/login`)
- **Health check endpoint** (`GET /api/health`)
- **Security Configuration** with Spring Security
- **Proper error handling** and validation

### 📁 Project Structure

```
backend-admin/
├── src/main/java/cl/tellevo/admin/
│   ├── BackendAdminApplication.java          # Main Spring Boot application
│   ├── controller/
│   │   └── AuthController.java               # REST endpoints
│   ├── dto/
│   │   ├── LoginRequest.java                 # Login request DTO
│   │   └── LoginResponse.java                # Login response DTO
│   ├── security/
│   │   ├── JwtUtil.java                      # JWT utility service
│   │   └── SecurityConfig.java               # Security configuration
│   └── service/
│       └── AuthService.java                  # Authentication service
├── src/main/resources/
│   └── application.properties                # Configuration file
└── pom.xml                                   # Maven configuration
```

## Prerequisites

- **Java 24** ✅ (Already installed)
- **Maven 3.8+** (Required to build and run)

## Installation & Setup

### 1. Install Maven (if not available)

```bash
sudo apt-get update
sudo apt-get install -y maven
```

### 2. Build the Project

```bash
cd backend-admin
mvn clean compile
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

## API Endpoints

### POST /api/login
Authenticates a user and returns a JWT token.

**Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response (Success):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Inicio de sesión exitoso"
}
```

**Response (Error):**
```json
{
  "message": "Credenciales inválidas"
}
```

### GET /api/health
Health check endpoint.

**Response:**
```json
{
  "status": "UP",
  "message": "TeLlevo Admin Backend is running"
}
```

## Default Credentials

- **Username:** `admin@tellevoapp.cl`
- **Password:** `admin123`

## Configuration

The application is configured via `src/main/resources/application.properties`:

```properties
# Server configuration
server.port=8080

# CORS configuration for both environments
cors.allowed-origins=http://localhost:5173,https://admin.tellevoapp.com
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.allow-credentials=true

# JWT configuration
jwt.expiration=86400000
jwt.secret=${JWT_SECRET:mySecretKey123456789012345678901234567890}

# Profile-specific settings
spring.profiles.active=${SPRING_PROFILE:dev}

# Default admin credentials
spring.security.user.name=admin
spring.security.user.password=admin123
```

## Testing the API

### ✅ All Tests Passed!

**Health Check**:
```bash
curl http://localhost:8080/api/health
# Response: {"status":"UP","message":"TeLlevo Admin Backend is running"}
```

**Valid Login**:
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@tellevoapp.cl","password":"admin123"}'
# Response: {"token":"eyJhbGciOiJIUzI1NiJ9...","message":"Inicio de sesión exitoso"}
```

**Invalid Login**:
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@tellevoapp.cl","password":"wrongpassword"}'
# Response: {"message":"Credenciales inválidas"}
```

### Using your Vue.js frontend:

1. Start the backend: `mvn spring-boot:run`
2. Start your Vue.js frontend: `npm run dev` (from the main project directory)
3. Go to `http://localhost:5173` and try to log in with:
   - Username: `admin`
   - Password: `admin123`

## Integration with Frontend

Your Vue.js frontend is already configured to make requests to this backend:

```javascript
// From your LoginView.vue
const response = await axios.post('http://localhost:8080/api/login', {
  username: username.value,
  password: password.value
});
```

The backend will:
1. Validate the credentials
2. Generate a JWT token
3. Return the token and success message
4. Handle CORS properly for your frontend

## Next Steps

- **Database Integration:** Replace hardcoded credentials with database users
- **User Management:** Add user registration and management endpoints
- **Role-based Security:** Implement different user roles
- **Password Encryption:** Hash stored passwords with BCrypt
- **Refresh Tokens:** Implement token refresh mechanism

## Troubleshooting

### Port 8080 already in use:
Change the port in `application.properties`:
```properties
server.port=8081
```

### CORS issues:
Update the allowed origins in `application.properties`:
```properties
cors.allowed-origins=http://localhost:5173,http://localhost:3000
```

### Maven not found:
Install Maven or use a Maven wrapper if available.
