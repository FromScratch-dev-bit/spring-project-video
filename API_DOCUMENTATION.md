# RentVideo API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
All endpoints (except registration) require Basic Authentication.

**Header Format:**
```
Authorization: Basic base64(username:password)
```

**Default Credentials:**
- Admin: `admin` / `admin123`
- User: `user` / `user123`

---

## API Endpoints

### Authentication

#### 1. Register New User
**POST** `/auth/register`

**Access:** Public

**Request Body:**
```json
{
  "username": "johndoe",
  "password": "password123",
  "fullName": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "+1234567890"
}
```

**Response (201):**
```json
{
  "message": "User registered successfully",
  "user": {
    "id": 1,
    "username": "johndoe",
    "fullName": "John Doe",
    "email": "john@example.com",
    "phoneNumber": "+1234567890",
    "role": "USER",
    "active": true,
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

#### 2. Login
**POST** `/auth/login`

**Access:** Public (but requires credentials)

**Request Body:**
```json
{
  "username": "user",
  "password": "user123"
}
```

**Response (200):**
```json
{
  "message": "Login successful",
  "user": {
    "id": 2,
    "username": "user",
    "fullName": "John Doe",
    "email": "user@rentvideo.com",
    "role": "USER",
    "active": true
  }
}
```

---

### Videos

#### 3. Get All Videos
**GET** `/videos`

**Access:** USER, ADMIN

**Query Parameters:**
- `availableOnly` (boolean) - Filter only available videos
- `genre` (string) - Filter by genre
- `title` (string) - Search by title

**Examples:**
```
GET /videos
GET /videos?availableOnly=true
GET /videos?genre=Action
GET /videos?title=dark
```

**Response (200):**
```json
[
  {
    "id": 1,
    "title": "The Shawshank Redemption",
    "description": "Two imprisoned men bond over a number of years...",
    "director": "Frank Darabont",
    "genre": "Drama",
    "releaseYear": 1994,
    "durationMinutes": 142,
    "rentalPricePerDay": 3.99,
    "totalCopies": 5,
    "availableCopies": 5,
    "coverImageUrl": null,
    "available": true,
    "createdAt": "2024-01-15T10:00:00"
  }
]
```

#### 4. Get Video by ID
**GET** `/videos/{id}`

**Access:** USER, ADMIN

**Response (200):** Same as single video object above

#### 5. Create Video
**POST** `/videos`

**Access:** ADMIN only

**Request Body:**
```json
{
  "title": "Interstellar",
  "description": "A team of explorers travel through a wormhole in space...",
  "director": "Christopher Nolan",
  "genre": "Sci-Fi",
  "releaseYear": 2014,
  "durationMinutes": 169,
  "rentalPricePerDay": 4.99,
  "totalCopies": 3,
  "coverImageUrl": "https://example.com/cover.jpg"
}
```

**Response (201):** Returns created video object

#### 6. Update Video
**PUT** `/videos/{id}`

**Access:** ADMIN only

**Request Body:** Same as Create Video

**Response (200):** Returns updated video object

#### 7. Delete Video
**DELETE** `/videos/{id}`

**Access:** ADMIN only

**Response (200):**
```json
{
  "message": "Video deleted successfully"
}
```

---

### Rentals

#### 8. Rent a Video
**POST** `/rentals`

**Access:** USER, ADMIN

**Request Body:**
```json
{
  "videoId": 1,
  "rentalDays": 3
}
```

**Response (201):**
```json
{
  "id": 1,
  "userId": 2,
  "username": "user",
  "videoId": 1,
  "videoTitle": "The Shawshank Redemption",
  "rentalDate": "2024-01-15",
  "dueDate": "2024-01-18",
  "returnDate": null,
  "rentalPrice": 11.97,
  "lateFee": 0.00,
  "totalAmount": 11.97,
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00"
}
```

#### 9. Get My Rentals
**GET** `/rentals/my-rentals`

**Access:** USER, ADMIN

**Response (200):** Array of rental objects

#### 10. Get All Rentals
**GET** `/rentals`

**Access:** ADMIN only

**Query Parameters:**
- `activeOnly` (boolean) - Filter only active rentals

**Response (200):** Array of rental objects

#### 11. Return a Video
**PUT** `/rentals/{id}/return`

**Access:** USER (own rentals), ADMIN (any rental)

**Response (200):** Returns updated rental object with return date and late fees (if applicable)

---

### Users

#### 12. Get Current User Profile
**GET** `/users/me`

**Access:** USER, ADMIN

**Response (200):**
```json
{
  "id": 2,
  "username": "user",
  "fullName": "John Doe",
  "email": "user@rentvideo.com",
  "phoneNumber": "+0987654321",
  "role": "USER",
  "active": true,
  "createdAt": "2024-01-15T10:00:00"
}
```

#### 13. Update Current User Profile
**PUT** `/users/me`

**Access:** USER, ADMIN

**Request Body:**
```json
{
  "fullName": "John Smith",
  "email": "john.smith@example.com",
  "phoneNumber": "+1111111111"
}
```

**Response (200):** Returns updated user object

#### 14. Get All Users
**GET** `/users`

**Access:** ADMIN only

**Response (200):** Array of user objects

---

## Error Responses

### 400 Bad Request
```json
{
  "status": 400,
  "message": "Username already exists",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 401 Unauthorized
```json
{
  "status": 401,
  "message": "Invalid username or password",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 403 Forbidden
```json
{
  "status": 403,
  "message": "Access denied. You don't have permission to access this resource.",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 404 Not Found
```json
{
  "status": 404,
  "message": "Video not found with id: 999",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 422 Validation Error
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "username": "Username must be between 3 and 50 characters",
    "email": "Email should be valid"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## Testing with cURL

### Register a new user
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123",
    "fullName": "Test User",
    "email": "test@example.com"
  }'
```

### Get all videos (authenticated)
```bash
curl -u user:user123 http://localhost:8080/api/videos
```

### Rent a video
```bash
curl -u user:user123 -X POST http://localhost:8080/api/rentals \
  -H "Content-Type: application/json" \
  -d '{
    "videoId": 1,
    "rentalDays": 3
  }'
```

### Create a video (admin only)
```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/videos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "New Movie",
    "director": "Director Name",
    "genre": "Action",
    "releaseYear": 2024,
    "durationMinutes": 120,
    "rentalPricePerDay": 4.99,
    "totalCopies": 5
  }'
```

---

## Business Rules

1. **Video Availability**: Videos become unavailable when all copies are rented out
2. **Late Fees**: 50% of daily rental rate per day overdue
3. **User Roles**:
   - **USER**: Can view videos, rent videos, return their own rentals, manage their profile
   - **ADMIN**: Full access to all operations including video management and viewing all rentals
4. **Rental Calculation**: Total price = (rental price per day × number of days)
5. **Return Process**: Video copies are automatically returned to available pool upon return
