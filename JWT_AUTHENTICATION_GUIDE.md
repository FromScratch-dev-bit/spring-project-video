# JWT Authentication Guide - RentVideo API

## Overview

This API uses **JWT (JSON Web Tokens)** for stateless authentication. This means:
- No session state is stored on the server
- Each request is authenticated independently using the JWT token
- Tokens are cryptographically signed and have an expiration time

## How JWT Works

1. **User Login** → Server generates JWT token
2. **Client stores token** (localStorage, sessionStorage, or memory)
3. **Client sends token** in Authorization header for each request
4. **Server validates token** and processes the request

## Token Structure

A JWT token consists of three parts separated by dots (`.`):

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzAwMDAwMDAwLCJleHAiOjE3MDAwODY0MDB9.signature-hash
```

1. **Header**: Algorithm and token type
2. **Payload**: User data and claims (username, expiration, etc.)
3. **Signature**: Cryptographic signature to verify token integrity

## Authentication Flow

### 1. Register (Optional)

If you're a new user, register first:

```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "fullName": "New User",
  "email": "newuser@example.com",
  "phoneNumber": "+1234567890"
}
```

**Response:**
```json
{
  "message": "User registered successfully. Please login to get your access token.",
  "user": {
    "id": 3,
    "username": "newuser",
    "fullName": "New User",
    "email": "newuser@example.com",
    "role": "USER",
    "active": true
  }
}
```

### 2. Login to Get JWT Token

```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "user",
  "password": "user123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzAwMDAwMDAwLCJleHAiOjE3MDAwODY0MDB9.signature",
  "tokenType": "Bearer",
  "user": {
    "id": 2,
    "username": "user",
    "fullName": "John Doe",
    "email": "user@rentvideo.com",
    "role": "USER",
    "active": true
  },
  "message": "Login successful"
}
```

**Important:** Save the `accessToken` - you'll need it for all subsequent requests!

### 3. Use Token in Requests

Include the token in the `Authorization` header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzAwMDAwMDAwLCJleHAiOjE3MDAwODY0MDB9.signature
```

**Example Request:**
```bash
GET /api/videos
Authorization: Bearer YOUR_JWT_TOKEN
```

## Token Configuration

| Property | Value | Description |
|----------|-------|-------------|
| **Expiration** | 24 hours (86400000 ms) | Token validity period |
| **Algorithm** | HS256 | HMAC with SHA-256 |
| **Token Type** | Bearer | Authorization scheme |

## Frontend Integration Examples

### JavaScript (Fetch API)

```javascript
// 1. Login
async function login(username, password) {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ username, password })
  });
  
  const data = await response.json();
  
  // Store token in localStorage
  localStorage.setItem('accessToken', data.accessToken);
  localStorage.setItem('user', JSON.stringify(data.user));
  
  return data;
}

// 2. Make authenticated request
async function getVideos() {
  const token = localStorage.getItem('accessToken');
  
  const response = await fetch('http://localhost:8080/api/videos', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  return await response.json();
}

// 3. Logout (client-side)
function logout() {
  localStorage.removeItem('accessToken');
  localStorage.removeItem('user');
}
```

### React Example

```jsx
import axios from 'axios';

// Configure axios with interceptor
const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

// Add token to every request
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Handle 401 errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired or invalid - redirect to login
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Usage in components
async function fetchVideos() {
  try {
    const response = await api.get('/videos');
    return response.data;
  } catch (error) {
    console.error('Error fetching videos:', error);
  }
}
```

### Angular Example

```typescript
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/login`, {
      username,
      password
    });
  }

  getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getVideos(): Observable<any> {
    return this.http.get(`${this.baseUrl}/videos`, {
      headers: this.getAuthHeaders()
    });
  }
}
```

## Security Best Practices

### 1. Token Storage

**Recommended Options:**
- **Memory (best for security)**: Store in component state/variable
- **SessionStorage**: Cleared when tab closes
- **LocalStorage**: Persists across sessions

**Never:**
- Store tokens in cookies without HttpOnly flag
- Expose tokens in URLs
- Store tokens in plain text files

### 2. Token Handling

```javascript
// ✅ Good: Store token securely
localStorage.setItem('accessToken', token);

// ❌ Bad: Never expose token in URL
// DON'T: http://example.com/api/videos?token=abc123

// ✅ Good: Clear token on logout
localStorage.removeItem('accessToken');

// ✅ Good: Check token expiration before requests
function isTokenExpired(token) {
  const payload = JSON.parse(atob(token.split('.')[1]));
  return payload.exp * 1000 < Date.now();
}
```

### 3. HTTPS Only

**Always use HTTPS in production** to prevent token interception:
```
https://your-api.com/api/videos
```

## Error Handling

### 401 Unauthorized

Token is missing, invalid, or expired:

```json
{
  "status": 401,
  "message": "Unauthorized: Authentication token is missing or invalid",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/videos"
}
```

**Solution:** Login again to get a new token

### 403 Forbidden

Token is valid but user lacks permissions:

```json
{
  "status": 403,
  "message": "Access denied. You don't have permission to access this resource.",
  "timestamp": "2024-01-15T10:30:00"
}
```

**Solution:** Contact admin or use an account with proper role

## Testing with Postman

### Step 1: Login
1. Create a POST request to `http://localhost:8080/api/auth/login`
2. Set Body → raw → JSON:
```json
{
  "username": "admin",
  "password": "admin123"
}
```
3. Send request and copy the `accessToken` from response

### Step 2: Use Token
1. Create a new request (e.g., GET `http://localhost:8080/api/videos`)
2. Go to **Authorization** tab
3. Select **Type: Bearer Token**
4. Paste your token in the **Token** field
5. Send request

### Step 3: Save Token in Environment (Optional)
1. After login, add this to **Tests** tab:
```javascript
pm.environment.set("jwt_token", pm.response.json().accessToken);
```
2. In other requests, use `{{jwt_token}}` in Authorization

## Token Renewal

Currently, tokens expire after 24 hours. Options:

### Option 1: Login Again
When token expires, user must login again to get a new token.

### Option 2: Refresh Token (Future Enhancement)
Implement a refresh token mechanism for seamless token renewal.

## Troubleshooting

### Problem: "Cannot set user authentication"
**Cause:** Token is malformed or expired  
**Solution:** Login again to get a fresh token

### Problem: "Token validation failed"
**Cause:** Secret key mismatch or token tampering  
**Solution:** Ensure you're using the exact token from login response

### Problem: "User not found"
**Cause:** Username in token doesn't exist  
**Solution:** Login with valid credentials

## JWT Claims

The JWT token contains the following claims:

| Claim | Description | Example |
|-------|-------------|---------|
| `sub` | Subject (username) | "user" |
| `iat` | Issued at timestamp | 1700000000 |
| `exp` | Expiration timestamp | 1700086400 |

Decode your token at [jwt.io](https://jwt.io) to inspect claims (for debugging only).

## Summary

1. ✅ **Login** to get JWT token
2. ✅ **Store** token securely (localStorage/memory)
3. ✅ **Send** token in `Authorization: Bearer <token>` header
4. ✅ **Handle** 401 errors by prompting re-login
5. ✅ **Use HTTPS** in production
6. ✅ **Never** expose tokens in URLs or logs
