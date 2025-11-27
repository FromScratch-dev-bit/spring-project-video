# RentVideo - Video Rental System (Advanced)

A RESTful API service built with Spring Boot for managing an online video rental system with JWT-based stateless authentication and role-based authorization.

## Features

- **Video Management**: CRUD operations for video catalog
- **User Management**: User registration and profile management
- **Rental Management**: Rent, return, and track video rentals
- **Security**: JWT (JSON Web Token) based stateless authentication
- **Authorization**: Role-based access control (ADMIN, USER) with fine-grained permissions

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA**
- **JJWT 0.12.3** (JWT implementation)
- **H2 Database** (Development)
- **MySQL** (Production-ready)
- **Maven**
- **Lombok**

## Project Structure

```
src/main/java/com/rentvideo/
├── VideoRentalApplication.java
├── config/
│   ├── SecurityConfig.java
│   └── DataInitializer.java
├── security/
│   ├── JwtUtil.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtAuthenticationEntryPoint.java
├── model/
│   ├── User.java
│   ├── Video.java
│   ├── Rental.java
│   └── Role.java (enum)
├── repository/
│   ├── UserRepository.java
│   ├── VideoRepository.java
│   └── RentalRepository.java
├── service/
│   ├── UserService.java
│   ├── VideoService.java
│   ├── RentalService.java
│   └── CustomUserDetailsService.java
├── controller/
│   ├── AuthController.java
│   ├── VideoController.java
│   ├── RentalController.java
│   └── UserController.java
├── dto/
│   ├── request/
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── VideoRequest.java
│   │   └── RentalRequest.java
│   └── response/
│       ├── UserResponse.java
│       ├── VideoResponse.java
│       └── RentalResponse.java
└── exception/
    ├── GlobalExceptionHandler.java
    ├── ResourceNotFoundException.java
    ├── VideoNotAvailableException.java
    └── UnauthorizedException.java
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user (Public)
- `POST /api/auth/login` - Login (Public)

### Videos
- `GET /api/videos` - Get all videos (USER, ADMIN)
- `GET /api/videos/{id}` - Get video by ID (USER, ADMIN)
- `POST /api/videos` - Add new video (ADMIN only)
- `PUT /api/videos/{id}` - Update video (ADMIN only)
- `DELETE /api/videos/{id}` - Delete video (ADMIN only)

### Rentals
- `POST /api/rentals` - Rent a video (USER, ADMIN)
- `GET /api/rentals` - Get all rentals (ADMIN only)
- `GET /api/rentals/my-rentals` - Get user's rentals (USER, ADMIN)
- `PUT /api/rentals/{id}/return` - Return a rented video (USER, ADMIN)

### Users
- `GET /api/users` - Get all users (ADMIN only)
- `GET /api/users/me` - Get current user profile (USER, ADMIN)
- `PUT /api/users/me` - Update current user profile (USER, ADMIN)

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL (for production) or use embedded H2

### Installation

1. Clone the repository
```bash
git clone <repository-url>
cd spring-Project-video
```

2. Build the project
```bash
mvn clean install
```

3. Run the application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Default Users

The application creates default users on startup:

**Admin User:**
- Username: `admin`
- Password: `admin123`
- Role: ADMIN

**Regular User:**
- Username: `user`
- Password: `user123`
- Role: USER

## Authentication

This API uses **JWT (JSON Web Token)** for stateless authentication. 

### Getting a Token

1. Login with valid credentials:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

2. You'll receive a response with an access token:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "user": {...},
  "message": "Login successful"
}
```

3. Use the token in subsequent requests:
```
Authorization: Bearer <your-jwt-token>
```

Example:
```bash
curl -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  http://localhost:8080/api/videos
```

**Token Expiration**: Tokens are valid for 24 hours (86400000 ms)

## 📖 Documentation

- **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** - Complete API reference with all endpoints
- **[JWT_AUTHENTICATION_GUIDE.md](JWT_AUTHENTICATION_GUIDE.md)** - Comprehensive JWT authentication guide with frontend examples
- **[RentVideo_API_Postman_Collection.json](RentVideo_API_Postman_Collection.json)** - Ready-to-use Postman collection

## 🧪 Testing with Postman

1. Import `RentVideo_API_Postman_Collection.json` into Postman
2. Run "Login (Admin)" or "Login (User)" request
3. The JWT token will be automatically saved to environment variable `jwt_token`
4. All other requests will use this token automatically

## Database

### H2 Console (Development)
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:rentvideo`
- Username: `sa`
- Password: (leave empty)

### MySQL Configuration (Production)
Update `application.properties` with your MySQL credentials and uncomment MySQL configuration.

## Testing

Run tests with:
```bash
mvn test
```

## License

This project is licensed under the MIT License.
