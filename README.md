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

## API Endpoints
POST /api/users/register
POST /api/users/login
GET /api/auth/verify