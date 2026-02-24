Auth Service Infrastructure:

PostgreSQL: Running at localhost:5430 (Database: trinity-auth-service).

RabbitMQ: Management panel at http://localhost:15672 (guest/guest).

2. Application Configuration
   Main settings are located in src/main/resources/application.properties. Ensure the database credentials match your Docker environment.

🔌 API Endpoints
Swagger UI: While the service is running, access  for detailed technical documentation.

🏗️ Event Architecture
Upon successful registration, the service publishes events to RabbitMQ exchanges:

UserCreatedEvent

AcademicCreatedEvent

Alunocreatedevent

These events allow the Core microservice to automatically create business entities related to the new user.

⚠️ Troubleshooting
Connection Failure: Ensure the trinity-auth-db container is running. Spring Boot will fail to start if it cannot connect to the database on port 5430.

403 Forbidden: Ensure you are sending the JWT Token correctly in the Header (Authorization: Bearer <token>) for protected routes.

Messaging Issues: Check the RabbitMQ panel to verify if queues were created and if there are active consumers from the Core service.

Developed for the Trinity Academy ecosystem 🥋