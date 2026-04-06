# 💰 Finance Dashboard Backend (Spring Boot)

A robust backend application built using Spring Boot for managing financial records with 
**JWT-based authentication**, **role-based access control**, and **analytics dashboards**.

---

## 🚀 Features

### 👤 User Management
- Create users with roles (ADMIN, ANALYST, EMPLOYEE)
- Enable/disable users
- Role-based access control

---

### 🔐 Authentication & Security
- JWT-based authentication
- Secure APIs using Spring Security
- Stateless session management
- Password encryption using BCrypt

---

### 💰 Financial Records
- Create, update, delete financial records
- Each record linked to a specific user
- Role-based data visibility:
    - ADMIN → full access
    - ANALYST → analytics + read access
    - EMPLOYEE → create transactions + view only own transactions

---

### 🔍 Filtering & Pagination
- Filter by:
    - Type (INCOME / EXPENSE)
    - Category
    - Date range
- Pagination support for all list APIs

---

### 📊 Dashboard Analytics
- Total Income
- Total Expense
- Net Balance
- Category-wise totals
- Monthly trends
- Weekly trends
- Recent transactions

---

### 📄 API Documentation
- Swagger UI available for testing APIs

👉 Open:

http://localhost:8080/swagger-ui/index.html

## 📮 Postman Collection

Import the collection from:
postman/finance-app.postman_collection.json

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- MySQL
- Spring Data JPA
- Swagger (Springdoc OpenAPI)
- Lombok

---

## 📂 Project Structure


com.eadevs.finance_app
│
├── config # Security configuration
├── controller # REST Controllers
│ ├── AuthController
│ ├── UserController
│ └── FinancialRecordController
│
├── dto # Request & Response DTOs
├── exception # Global exception handling
├── model # Entities & enums
│ ├── User
│ ├── FinancialRecord
│ ├── Role
│ └── RecordType
│
├── repository # JPA repositories
├── security # JWT & authentication logic
│ ├── JwtUtil
│ ├── JwtAuthenticationFilter
│ ├── CustomUserDetails
│ └── SecurityUtil
│
├── service # Business logic layer
│ ├── UserService
│ ├── FinancialRecordService
│ └── JwtService
│
└── FinanceAppApplication


---

## 🔑 Roles & Permissions

| Role      | Access                                    |
|----------|-------------------------------------------|
| ADMIN    | Full access (users + records + analytics) |
| ANALYST  | Write own + (Read + analytics) All        |
| EMPLOYEE | Read Write only own records               |

---

## 🔐 Authentication Flow

### 1. Login

POST /auth/login


Request:
```json
{
  "email": "admin@mail.com",
  "password": "123456"
}

Response:

{
  "token": "JWT_TOKEN",
  message: "response",
  username: "username"
}
2. Use Token

Add header in requests:

Authorization: Bearer <JWT_TOKEN>
📌 Main API Endpoints
🔐 Auth
POST /auth/login
👤 Users
POST /auth/users
GET /auth/users
GET /auth/users/{id}
PUT /auth/users/{id}
DELETE /auth/users/{id}
PUT /auth/users/{id}/toggle-status

💰 Financial Records
CRUD
POST /records
GET /records
PUT /records/{id}
DELETE /records/{id}

Filters
GET /records/filter/type
GET /records/filter/category
GET /records/filter/date

Dashboard
GET /records/summary
GET /records/summary/category
GET /records/summary/monthly
GET /records/summary/weekly
GET /records/recent


📦 Pagination Example
GET /records?page=0&size=10

⚙️ Setup & Run
1. Clone Repository
git clone <your-repo-url>

2. Configure Database
Create MySQL database:
CREATE DATABASE finance_db;

3. Update application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=MySecretKeyThatIncludesNumbs1234AndCharsABCD
jwt.expiration=86400000

4. Run Application
mvn spring-boot:run


🧪 Testing APIs
Use:
Postman
Swagger UI


🧠 Design Highlights
Clean layered architecture
DTO-based request/response handling
Role-based authorization using Spring Security
JWT-based stateless authentication
Pagination for scalability
Global exception handling


👨‍💻 Author
Ehtisham Umar Ansari
Backend Developer (Java | Spring Boot)
