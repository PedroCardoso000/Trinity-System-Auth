# Trinity Manager - Auth Service 🥋

The **Trinity Auth Service** is the security and gateway module of the Trinity Academy ecosystem. It is responsible for identity management, handling user authentication (Login) and registration for both Administrators and Students.

## 🚀 What it does

* **Security & Authentication**: Implements JWT (JSON Web Token) to provide secure access to the system.
* **User Registration**: Handles the creation of new accounts for Academy Administrators (Adm) and Students (Alunos).
* **Ecosystem Integration**: Automatically notifies the Core Service whenever a new user or academy unit is created via RabbitMQ events.
* **Access Control**: Manages user roles and permissions to ensure data integrity across the platform.

## ⚙️ How to Set Up

### 1. Prerequisites

* **JDK 17**
* **Maven**
* **Docker** (to run the database and message broker)

### 2. Infrastructure

This service requires PostgreSQL and RabbitMQ to be running. You can start the infrastructure using the centralized `Trinity-System-Infra` repository.

```
# From your infra folder:
docker-compose up -d

```

### 3. Running the Service

1. Ensure a database named `trinity-auth-service` exists in your PostgreSQL instance.
2. From the project root, run:

```
mvn spring-boot:run

```

The service starts on port **8080** by default.

### 4. API Documentation

The service includes interactive documentation where you can test the login and registration endpoints:
**URL:** http://localhost:8080/swagger-ui.html

## 🏗️ Architecture Role

The Auth Service acts as the producer of truth for user data. When a registration occurs here, it dispatches messages to the **aluno.created.queue**, allowing the Core Service to build the necessary business profiles automatically.

---

Developed for **Trinity Academy**.