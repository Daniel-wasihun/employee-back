# 🚀 Employee Management System (EMS) — Backend

[![Backend CI](https://github.com/Daniel-wasihun/employee-back/actions/workflows/backend.yml/badge.svg)](https://github.com/Daniel-wasihun/employee-back/actions/workflows/backend.yml)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)

A production-grade, secure, and high-performance backend for the Employee Management System.

---

## ✨ Features

- **🔐 Robust Security**: Stateless JWT authentication with Spring Security 6 and Refresh Token rotation.
- **🛡️ RBAC**: Role-Based Access Control (ADMIN, MANAGER, EMPLOYEE) with granular permissions.
- **⚡ Performance**: Optimized JPA queries, pagination, and database indexing.
- **📈 Analytics**: Real-time dashboard statistics and department-wise reporting.
- **📜 Documentation**: Fully documented REST API using Swagger/OpenAPI 3.
- **🐳 DevOps**: Dockerized environment with automated CI/CD and PostgreSQL health checks.
- **🔒 SSL/TLS**: Native HTTPS support enabled on port 8443 via PKCS12 keystore.

---

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.4.1
- **Language**: Java 21
- **Database**: PostgreSQL 16
- **Security**: Spring Security 6 + JWT
- **Migration**: Flyway/Liquibase support
- **Build Tool**: Maven

---

## 🚦 Quick Start

### Using Docker (Recommended)
```bash
# 1. Clone the repository
git clone https://github.com/Daniel-wasihun/employee-back.git
cd employee-back

# 2. Run with Docker Compose
docker compose up --build
```
The backend will be available at `https://localhost:8443`.

### Manual Setup
1. Ensure **Java 21** and **Postgres** are installed.
2. Update `src/main/resources/application.yml` with your database credentials.
3. Run the app:
```bash
./mvnw spring-boot:run
```

---

## 📂 API Documentation

Once the app is running, access the Swagger UI at:
`https://localhost:8443/swagger-ui/index.html`

---

## 👨‍💻 Author
**Daniel Wasihun** - [GitHub](https://github.com/Daniel-wasihun)
