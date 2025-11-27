# RentVideo - Video Rental System

A RESTful API service built with Spring Boot for managing an online video rental system with Basic Authentication and role-based authorization.

## Features

- **Video Management**: CRUD operations for video catalog
- **User Management**: User registration and profile management
- **Rental Management**: Rent, return, and track video rentals
- **Security**: Basic Authentication with role-based access control (ADMIN, USER)
- **Authorization**: Fine-grained permissions based on user roles

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (Basic Authentication)
- **Spring Data JPA**
- **H2 Database** (Development)
- **MySQL** (Production-ready)
- **Maven**
- **Lombok**

## Project Structure

```
src/main/java/com/rentvideo/
├── VideoRentalApplication.java
├── config/
│   └── SecurityConfig.java
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

This API uses **Basic Authentication**. Include credentials in the Authorization header:

```
Authorization: Basic base64(username:password)
```

Example using curl:
```bash
curl -u admin:admin123 http://localhost:8080/api/videos
```

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
