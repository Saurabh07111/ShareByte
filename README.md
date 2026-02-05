# ShareByte
Spring Boot backend for ShareByte – a real-time food redistribution platform connecting restaurants with NGOs.

# ShareByte Backend


## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Maven

## Project Structure
com.sharebyte
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── config
 └── ShareByteApplication.java


## Features Implemented
- User Registration
- Email uniqueness validation
- Password encryption
- DTO-based validation
- Global exception handling
- Email Verification
- Jwt Authentication
- Profile image upload
- User profile retrieval
- Change Password

- Admin User Listing 


## API Endpoints
POST /auth/register
POST /auth/login
GET /auth/verify

POST /user/uppload-image
GET /user/me
GET /user/{user-id}/profile
GET /file/image

PUT /user/profile
PUT /user/change-password

GET /admin/users
    

