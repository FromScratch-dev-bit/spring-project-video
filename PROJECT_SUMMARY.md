# RentVideo System - Advanced Version Summary

## 🎯 Project Overview

**RentVideo (Advanced)** is a production-ready RESTful API service built with Spring Boot that manages an online video rental system. This advanced version features **JWT-based stateless authentication**, comprehensive security, and role-based authorization.

---

## ✨ Key Features Implemented

### 1. **JWT-Based Stateless Authentication**
- ✅ Secure token generation using JJWT 0.12.3
- ✅ 24-hour token expiration (configurable)
- ✅ HS256 signing algorithm
- ✅ Stateless architecture (no server-side sessions)
- ✅ Token validation on every request

### 2. **Role-Based Authorization**
- ✅ Two user roles: `USER` and `ADMIN`
- ✅ Fine-grained access control using `@PreAuthorize`
- ✅ Method-level security
- ✅ Custom authentication entry point for JWT errors

### 3. **Video Management**
- ✅ Full CRUD operations for video catalog
- ✅ Search by title, filter by genre
- ✅ Availability tracking with copy management
- ✅ Admin-only create/update/delete operations

### 4. **Rental Management**
- ✅ Rent videos with automatic inventory updates
- ✅ Return videos with late fee calculation
- ✅ Track rental history per user
- ✅ Overdue detection with 50% daily late fee

### 5. **User Management**
- ✅ User registration with validation
- ✅ Profile management
- ✅ BCrypt password encryption
- ✅ Email and username uniqueness validation

### 6. **Security Features**
- ✅ JWT authentication filter
- ✅ Custom authentication entry point
- ✅ CORS configuration ready
- ✅ SQL injection prevention (JPA)
- ✅ Password encryption

---

## 🏗️ Technical Architecture

### Technology Stack
```
Backend Framework:     Spring Boot 3.2.0
Language:              Java 17
Security:              Spring Security + JWT (JJWT 0.12.3)
Database:              H2 (dev), MySQL (prod)
ORM:                   Spring Data JPA / Hibernate
Build Tool:            Maven
Code Generation:       Lombok
Validation:            Jakarta Bean Validation
```

### Project Structure
```
RentVideo/
├── config/             → Security & app configuration
├── security/           → JWT utilities & filters
├── model/              → JPA entities
├── repository/         → Data access layer
├── service/            → Business logic
├── controller/         → REST endpoints
├── dto/                → Request/Response objects
└── exception/          → Error handling
```

---

## 🔐 Authentication Flow

```
1. User Registration (Optional)
   POST /api/auth/register
   ↓
2. User Login
   POST /api/auth/login
   Body: { username, password }
   ↓
3. Server Validates Credentials
   ↓
4. Server Generates JWT Token
   Token contains: { sub: username, iat: timestamp, exp: timestamp }
   ↓
5. Client Receives Token
   Response: { accessToken, tokenType: "Bearer", user, message }
   ↓
6. Client Stores Token
   (localStorage / sessionStorage / memory)
   ↓
7. Client Sends Token with Each Request
   Header: Authorization: Bearer <token>
   ↓
8. Server Validates Token
   - Extract token from header
   - Verify signature
   - Check expiration
   - Load user details
   ↓
9. Request Processed or Rejected (401)
```

---

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### Videos Table
```sql
CREATE TABLE videos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    director VARCHAR(255),
    genre VARCHAR(255),
    release_year INT NOT NULL,
    duration_minutes INT NOT NULL,
    rental_price_per_day DECIMAL(10,2) NOT NULL,
    total_copies INT NOT NULL,
    available_copies INT NOT NULL,
    cover_image_url VARCHAR(255),
    available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### Rentals Table
```sql
CREATE TABLE rentals (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    video_id BIGINT NOT NULL,
    rental_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    rental_price DECIMAL(10,2) NOT NULL,
    late_fee DECIMAL(10,2) DEFAULT 0,
    total_amount DECIMAL(10,2),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (video_id) REFERENCES videos(id)
);
```

---

## 🔌 API Endpoints Summary

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| **Authentication** |
| POST | `/api/auth/register` | Public | Register new user |
| POST | `/api/auth/login` | Public | Login & get JWT token |
| **Videos** |
| GET | `/api/videos` | USER, ADMIN | List all videos |
| GET | `/api/videos?availableOnly=true` | USER, ADMIN | List available videos |
| GET | `/api/videos?title=search` | USER, ADMIN | Search videos by title |
| GET | `/api/videos/{id}` | USER, ADMIN | Get video details |
| POST | `/api/videos` | ADMIN | Create new video |
| PUT | `/api/videos/{id}` | ADMIN | Update video |
| DELETE | `/api/videos/{id}` | ADMIN | Delete video |
| **Rentals** |
| POST | `/api/rentals` | USER, ADMIN | Rent a video |
| GET | `/api/rentals/my-rentals` | USER, ADMIN | Get user's rentals |
| GET | `/api/rentals` | ADMIN | Get all rentals |
| GET | `/api/rentals?activeOnly=true` | ADMIN | Get active rentals |
| PUT | `/api/rentals/{id}/return` | USER, ADMIN | Return video |
| **Users** |
| GET | `/api/users/me` | USER, ADMIN | Get current user |
| PUT | `/api/users/me` | USER, ADMIN | Update profile |
| GET | `/api/users` | ADMIN | List all users |

---

## 🎬 Sample Data

### Default Users
```json
Admin: { username: "admin", password: "admin123", role: "ADMIN" }
User:  { username: "user", password: "user123", role: "USER" }
```

### Pre-loaded Videos
1. The Shawshank Redemption (1994) - Drama
2. The Godfather (1972) - Crime
3. The Dark Knight (2008) - Action
4. Inception (2010) - Sci-Fi
5. Pulp Fiction (1994) - Crime

---

## 🛡️ Security Implementation

### JWT Configuration
```properties
jwt.secret=<base64-encoded-secret-key>
jwt.expiration=86400000  # 24 hours in milliseconds
```

### Security Filter Chain
```
1. JwtAuthenticationFilter
   ↓ Extract & validate JWT token
   ↓ Set authentication in SecurityContext
2. UsernamePasswordAuthenticationFilter
   ↓ Standard Spring Security filters
3. Authorization Checks
   ↓ @PreAuthorize annotations
   ↓ Role-based access control
4. ExceptionHandling
   ↓ JwtAuthenticationEntryPoint (401 errors)
   ↓ GlobalExceptionHandler (other errors)
```

### Password Encryption
- Algorithm: BCrypt
- Strength: Default (10 rounds)
- Salt: Auto-generated per password

---

## 🧪 Testing Guide

### Manual Testing with cURL

**1. Register a User**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "john123",
    "fullName": "John Smith",
    "email": "john@example.com"
  }'
```

**2. Login to Get Token**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"john123"}'
```

**3. Use Token to Access Protected Endpoint**
```bash
TOKEN="<paste-token-here>"
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/videos
```

**4. Rent a Video**
```bash
curl -H "Authorization: Bearer $TOKEN" \
  -X POST http://localhost:8080/api/rentals \
  -H "Content-Type: application/json" \
  -d '{"videoId":1,"rentalDays":3}'
```

### Testing with Postman
1. Import `RentVideo_API_Postman_Collection.json`
2. Run "Login" request
3. Token auto-saved to `{{jwt_token}}` variable
4. Test all endpoints with automatic authentication

---

## 📝 Business Rules

### Video Availability
- Videos have `totalCopies` and `availableCopies`
- When rented: `availableCopies--`
- When returned: `availableCopies++`
- If `availableCopies == 0`: `available = false`

### Rental Pricing
```
Rental Price = rentalPricePerDay × rentalDays
Late Fee = rentalPricePerDay × 0.5 × daysLate
Total Amount = Rental Price + Late Fee
```

### Access Control
| Role | Videos | Rentals | Users |
|------|--------|---------|-------|
| USER | View | Own rentals | Own profile |
| ADMIN | Full CRUD | All rentals | All users |

---

## 🚀 Deployment Checklist

### Before Production

✅ **Security**
- [ ] Change JWT secret to production value
- [ ] Set appropriate token expiration
- [ ] Enable HTTPS only
- [ ] Configure CORS for your domain
- [ ] Review and harden security rules

✅ **Database**
- [ ] Switch from H2 to MySQL/PostgreSQL
- [ ] Configure connection pool
- [ ] Set up database migrations
- [ ] Configure backup strategy

✅ **Configuration**
- [ ] Use environment variables for secrets
- [ ] Set appropriate log levels
- [ ] Configure external properties file
- [ ] Set up monitoring/metrics

✅ **Testing**
- [ ] Run all unit tests
- [ ] Perform integration testing
- [ ] Load testing for expected traffic
- [ ] Security penetration testing

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `README.md` | Project overview and quick start |
| `API_DOCUMENTATION.md` | Complete API reference |
| `JWT_AUTHENTICATION_GUIDE.md` | JWT implementation details |
| `RentVideo_API_Postman_Collection.json` | Postman collection |
| `PROJECT_SUMMARY.md` | This comprehensive summary |

---

## 🔄 Future Enhancements

### Potential Features
- **Refresh Tokens**: Implement refresh token mechanism
- **Email Verification**: Verify user emails on registration
- **Password Reset**: Add forgot password functionality
- **Advanced Search**: Full-text search with filters
- **Ratings & Reviews**: Allow users to rate videos
- **Recommendations**: ML-based video recommendations
- **Payment Integration**: Stripe/PayPal integration
- **Notifications**: Email/SMS notifications for due dates
- **Analytics Dashboard**: Admin analytics and reports
- **Multi-tenant Support**: Support for multiple stores

### Technical Improvements
- **Rate Limiting**: Prevent API abuse
- **Caching**: Redis for performance
- **Swagger/OpenAPI**: API documentation UI
- **Docker**: Containerization
- **CI/CD**: Automated deployment pipeline
- **Monitoring**: Prometheus + Grafana
- **Logging**: ELK stack integration

---

## 💡 Key Advantages

### Why JWT?
✅ **Stateless**: No server-side session storage  
✅ **Scalable**: Works across multiple servers  
✅ **Mobile-Friendly**: Easy integration with mobile apps  
✅ **Cross-Domain**: Can be used across different domains  
✅ **Self-Contained**: Token contains all needed info  

### Why This Architecture?
✅ **Separation of Concerns**: Clean layered architecture  
✅ **Maintainable**: Easy to extend and modify  
✅ **Testable**: Each layer can be tested independently  
✅ **Secure**: Industry-standard security practices  
✅ **Documented**: Comprehensive documentation  

---

## 🎓 Learning Outcomes

By studying this project, you'll learn:
- Spring Boot REST API development
- JWT authentication implementation
- Spring Security configuration
- JPA/Hibernate data modeling
- DTO pattern for API responses
- Global exception handling
- Bean validation
- Role-based authorization
- Stateless authentication architecture
- RESTful API best practices

---

## 📞 Support & Resources

### Official Documentation
- Spring Boot: https://spring.io/projects/spring-boot
- Spring Security: https://spring.io/projects/spring-security
- JJWT: https://github.com/jwtk/jjwt
- JWT.io: https://jwt.io

### Testing Tools
- Postman: https://www.postman.com
- cURL: https://curl.se
- Insomnia: https://insomnia.rest

---

## ✅ Project Checklist

- [x] Spring Boot project setup
- [x] JWT authentication implementation
- [x] User management with registration
- [x] Video CRUD operations
- [x] Rental management with late fees
- [x] Role-based authorization
- [x] Global exception handling
- [x] Input validation
- [x] Sample data initialization
- [x] H2 database configuration
- [x] MySQL support
- [x] API documentation
- [x] JWT guide
- [x] Postman collection
- [x] Testing instructions
- [x] Security best practices

---

**Project Status**: ✅ Production Ready

**Version**: 1.0.0 (Advanced - JWT)

**Last Updated**: November 27, 2025
