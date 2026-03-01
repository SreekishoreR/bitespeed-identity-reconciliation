# BiteSpeed Identity Reconciliation Service
A backend task for BiteSpeed that consolidates customer identities across multiple purchases by reconciling contact information based on email and phone number.

# About:
- This service implements the **Identity Reconciliation** feature for BiteSpeed, where,
- When a customer makes multiple purchases using different contact information, this service helps identify and link those purchases to the same customer.

# Core Functionality:
- Accepts customer contact information via `/identify` endpoint
- Links matching contacts based on email or phone number
- Maintains primary-secondary contact relationships
- Returns consolidated contact information for each customer

# Tech Stack:
While the preferred stack for BiteSpeed is Node.js with TypeScript, I have used **Java Spring Boot** for implementation.

# Technologies Used:
- Java 21
- Spring Boot 3
- PostgreSQL
- Spring Data JPA
- Maven
- Deployed on Render

# API Documentation

# Endpoint
POST `/identify`
Reconciles customer identities based on provided contact information.

# Request Body (JSON)
```json
{
  "email": "customer@example.com",
  "phoneNumber": "1234567890"
}
```

# Response (JSON)
{
  "contact": {
    "primaryContactId": 123,
    "emails": ["customer@example.com"],
    "phoneNumbers": ["1234567890"],
    "secondaryContactIds": []
  }
}

# Live Deployment:
- The API is deployed and publicly accessible at: https://bitespeed-identity-reconciliation-u3mh.onrender.com/identify

# Local Setup:
1. Clone Repo;
   - git clone https://github.com/SreekishoreR/bitespeed-identity-reconciliation.git
   - cd bitespeed-identity-reconciliation
2. Set Environment Variables;
   - DB_URL=jdbc:postgresql://localhost:5432/bitespeed
   - DB_USERNAME=postgres
   - DB_PASSWORD=yourpassword
3. Run Application -> ./mvnw spring-boot:run

