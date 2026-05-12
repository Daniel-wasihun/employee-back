# 🚀 Employee Management System (EMS) — Backend

[![Backend CI](https://github.com/Daniel-wasihun/employee-back/actions/workflows/backend.yml/badge.svg)](https://github.com/Daniel-wasihun/employee-back/actions/workflows/backend.yml)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)

A robust, enterprise-grade backend for the Employee Management System, built with Java 21 and Spring Boot 3.4.

---

## ✨ Features

- **🚀 RESTful API**: Fully documented with Swagger/OpenAPI.
- **🛡️ Security**: Spring Security 6 + JWT (HS256).
- **🗄️ Database**: PostgreSQL with automated schema migrations via Flyway.
- **📊 Auditing**: Automated created/updated timestamp tracking.
- **🌱 Data Seeding**: Automatic sample data initialization on startup.
- **🐳 Dockerized**: Easy deployment with Docker and Docker Compose.

---

## 🛠️ Tech Stack

- **Core**: Java 21 & Spring Boot 3.4
- **ORM**: Spring Data JPA & Hibernate 6
- **Security**: Spring Security 6 + JWT
- **Migration**: Flyway
- **Build Tool**: Maven

---

## 🚦 Quick Start

### Using Docker (Recommended)
```bash
# 1. Clone the repository
git clone https://github.com/Daniel-wasihun/employee-back.git
cd employee-back

# 2. Run with Docker Compose
docker-compose up --build
```
The backend will be available at `https://localhost:8443`.

> [!TIP]
> **SSL Warning**: If your browser shows a "Your connection is not private" error, you need to trust the generated certificate. Refer to the `ems-backend.crt` file or type `thisisunsafe` on the error page in Chrome.

### Manual Setup
1. Ensure **Java 21** and **Postgres** are installed.
2. Update `src/main/resources/application.yml` with your database credentials.
3. Run the app:
```bash
./mvnw spring-boot:run
```

---

## 📂 API Documentation

Once the app is running, access the documentation:
- **Swagger UI**: `https://localhost:8443/swagger-ui/index.html`
- **cURL API Reference**: [api_reference.md](.gemini/antigravity/brain/d97e21f8-91c0-4d36-8f2f-850f987e488c/api_reference.md)

---

## 👨‍💻 Author
**Daniel Wasihun** - [GitHub](https://github.com/Daniel-wasihun)
