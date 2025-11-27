# RentVideo API - Quick Reference Card

## 🚀 Quick Start (30 seconds)

### 1. Start Application
```bash
cd spring-Project-video
mvn spring-boot:run
```

### 2. Get JWT Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 3. Save Token & Test
```bash
TOKEN="<your-token-here>"
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/videos
```

---

## 🔑 Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| User | `user` | `user123` |

---

## 📍 Essential Endpoints

### Authentication (Public)
```bash
# Register
POST /api/auth/register
Body: {"username":"...", "password":"...", "fullName":"...", "email":"..."}

# Login (Returns JWT)
POST /api/auth/login
Body: {"username":"...", "password":"..."}
```

### Videos (Requires JWT)
```bash
# List all
GET /api/videos
Header: Authorization: Bearer <token>

# Get one
GET /api/videos/{id}

# Create (ADMIN only)
POST /api/videos
Body: {"title":"...", "genre":"...", "releaseYear":2024, ...}

# Update (ADMIN only)
PUT /api/videos/{id}

# Delete (ADMIN only)
DELETE /api/videos/{id}
```

### Rentals (Requires JWT)
```bash
# Rent video
POST /api/rentals
Body: {"videoId":1, "rentalDays":3}

# My rentals
GET /api/rentals/my-rentals

# All rentals (ADMIN only)
GET /api/rentals

# Return video
PUT /api/rentals/{id}/return
```

### Users (Requires JWT)
```bash
# My profile
GET /api/users/me

# Update profile
PUT /api/users/me
Body: {"fullName":"...", "email":"...", "phoneNumber":"..."}

# All users (ADMIN only)
GET /api/users
```

---

## 🔐 JWT Header Format

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzAwMDAwMDAwLCJleHAiOjE3MDAwODY0MDB9.signature
```

---

## 🎯 Common Use Cases

### Scenario 1: New User Signs Up & Rents Video
```bash
# 1. Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"john123","fullName":"John Doe","email":"john@email.com"}'

# 2. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"john123"}'
# Save the accessToken

# 3. Browse videos
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/videos

# 4. Rent a video
curl -H "Authorization: Bearer TOKEN" \
  -X POST http://localhost:8080/api/rentals \
  -H "Content-Type: application/json" \
  -d '{"videoId":1,"rentalDays":3}'
```

### Scenario 2: Admin Adds New Video
```bash
# 1. Login as admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 2. Add video
curl -H "Authorization: Bearer ADMIN_TOKEN" \
  -X POST http://localhost:8080/api/videos \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Avatar 2",
    "director":"James Cameron",
    "genre":"Sci-Fi",
    "releaseYear":2022,
    "durationMinutes":192,
    "rentalPricePerDay":5.99,
    "totalCopies":10
  }'
```

### Scenario 3: User Returns Video
```bash
# 1. Check my rentals
curl -H "Authorization: Bearer TOKEN" \
  http://localhost:8080/api/rentals/my-rentals

# 2. Return video (use rental ID from above)
curl -H "Authorization: Bearer TOKEN" \
  -X PUT http://localhost:8080/api/rentals/1/return
```

---

## ⚠️ Common Errors

### 401 Unauthorized
```json
{"status":401,"message":"Unauthorized: Authentication token is missing or invalid"}
```
**Fix**: Login again to get a new token

### 403 Forbidden
```json
{"status":403,"message":"Access denied. You don't have permission..."}
```
**Fix**: Use an admin account or check your role

### 404 Not Found
```json
{"status":404,"message":"Video not found with id: 999"}
```
**Fix**: Check the ID exists

### 400 Bad Request
```json
{"status":400,"message":"Video is not available for rental"}
```
**Fix**: Check video availability

---

## 🗄️ Database Access (Development)

**H2 Console**: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:rentvideo`
- Username: `sa`
- Password: (empty)

---

## 📦 Postman Testing

1. Import `RentVideo_API_Postman_Collection.json`
2. Run "Login (Admin)" or "Login (User)"
3. Token auto-saved to `{{jwt_token}}`
4. All requests use token automatically

---

## 🎨 Sample Video Data

| ID | Title | Genre | Year | Price/Day |
|----|-------|-------|------|-----------|
| 1 | The Shawshank Redemption | Drama | 1994 | $3.99 |
| 2 | The Godfather | Crime | 1972 | $4.99 |
| 3 | The Dark Knight | Action | 2008 | $3.99 |
| 4 | Inception | Sci-Fi | 2010 | $4.49 |
| 5 | Pulp Fiction | Crime | 1994 | $3.49 |

---

## 💰 Pricing Rules

```
Rental Cost = Price/Day × Days
Late Fee = Price/Day × 0.5 × Days Late
Total = Rental Cost + Late Fee
```

**Example**: $3.99/day × 3 days = $11.97
If returned 2 days late: +($3.99 × 0.5 × 2) = +$3.99
Total: $15.96

---

## 🔒 Security Notes

- ✅ Tokens expire in 24 hours
- ✅ Passwords are BCrypt encrypted
- ✅ All requests are stateless
- ✅ Use HTTPS in production
- ⚠️ Never share your JWT token
- ⚠️ Store tokens securely

---

## 📱 Frontend Integration

```javascript
// Login
const response = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: {'Content-Type': 'application/json'},
  body: JSON.stringify({username:'admin', password:'admin123'})
});
const {accessToken} = await response.json();
localStorage.setItem('token', accessToken);

// Use token
fetch('http://localhost:8080/api/videos', {
  headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
});
```

---

## 📞 Need Help?

- 📖 **Full API Docs**: `API_DOCUMENTATION.md`
- 🔐 **JWT Guide**: `JWT_AUTHENTICATION_GUIDE.md`
- 📊 **Project Details**: `PROJECT_SUMMARY.md`
- 📋 **README**: `README.md`

---

## ⚡ One-Liner Commands

```bash
# Start app
mvn spring-boot:run

# Login as admin
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"username":"admin","password":"admin123"}'

# Login as user
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"username":"user","password":"user123"}'

# Get videos (replace TOKEN)
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/videos

# Rent video (replace TOKEN)
curl -H "Authorization: Bearer TOKEN" -X POST http://localhost:8080/api/rentals -H "Content-Type: application/json" -d '{"videoId":1,"rentalDays":3}'
```

---

**Version**: 1.0.0 | **Port**: 8080 | **Environment**: Development
