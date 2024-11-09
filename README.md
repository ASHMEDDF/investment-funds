# Funds Management System

This project is a Spring Boot application designed to manage client subscriptions to financial funds. It includes features for subscribing to and unsubscribing from funds, checking transaction history, and sending email notifications to clients upon subscription actions.

## Project Structure

The application is divided into several key components:
- **Controller**: Handles HTTP requests and routes.
- **Service**: Contains business logic for managing funds and transactions.
- **Repository**: Manages interactions with the MongoDB database.
- **Notification Service**: Sends email notifications upon specific actions.

## Dependencies

This project uses the following dependencies:
- **Spring Boot Starter Web**: For creating REST APIs.
- **Spring Boot Starter Data MongoDB**: For MongoDB integration.
- **Lombok**: For reducing boilerplate code.
- **Spring Boot Starter Mail**: For sending email notifications.
- **Spring Boot DevTools**: For developer productivity.

See `pom.xml` for the full list of dependencies.

## Database Configuration

The application uses MongoDB to store fund and transaction data. Configuration settings for MongoDB are located in `application.properties`:

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=fundsdb
```

# Docker Setup
This project includes a `docker-compose.yml` file to facilitate setting up the application and MongoDB with Docker.

## Run with Docker
To build and run the application with Docker:

```bash
docker-compose up --build
```

This will start both the Spring Boot application and MongoDB service.

## Email Notifications

The application uses Spring Boot's mail starter to send email notifications. Ensure your email credentials are configured in `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## API Endpoints

The following endpoints are available in the `FundController`:

- `POST /api/fondos/suscribir`: Subscribes a user to a fund.
- `POST /api/fondos/nuevo`: Creates a new fund.
- `GET /api/fondos/{id}`: Retrieves fund details by ID.
- `GET /api/fondos/todos`: Retrieves a list of all funds.
- `POST /api/fondos/cancelar/{fundId}`: Unsubscribes a user from a fund.
- `GET /api/fondos/historial`: Retrieves a transaction history.

## Custom Exceptions

The project defines custom exceptions for handling specific error cases:

- `FundAlreadyExistsException`: Thrown when a fund with the same ID and name already exists.
- `FundNotFoundException`: Thrown when a fund cannot be found.
- `InsufficientFundsException`: Thrown when a user does not have sufficient funds to subscribe.

## Services
- `FundService`: Handles fund creation, subscription, unsubscription, and transaction history retrieval.
- `NotificationService`: Sends email notifications to clients.

## Running the Application

- Configure the email settings in `application.properties.`
- Make sure MongoDB is running on `localhost:27017` or use Docker Compose as described above.
- Start the application:

```bash
./mvnw spring-boot:run
```
The application will be available at http://localhost:8080.