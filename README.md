# Sensitive Words API

This repository contains a Spring Boot application that provides RESTful APIs for managing sensitive words and redacting sensitive content from messages.

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Endpoints](#endpoints)
    - [External API](#external-api)
    - [Internal API](#internal-api)
- [Exception Handling](#exception-handling)
- [DTOs and Entities](#dtos-and-entities)
- [Repositories](#repositories)
- [Services](#services)
- [Configuration](#configuration)
- [Swagger Documentation](#swagger-documentation)
- [Actuator Endpoints](#actuator-endpoints)
- [Design and Architectural Approach](#design-and-architectural-approach)
- [Further Improvements](#further-improvements)



## Overview

This application provides two sets of APIs:

- **External API:** Exposes endpoints for redacting sensitive words from messages.
- **Internal API:** Exposes endpoints for managing sensitive words including CRUD operations.

## Prerequisites

The application requires a connection to a MSSQL database. To set up the database:

- Run the MSSQL server in a Docker container locally using the following command:

```shell
docker run -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=P4$$word123' -p 1433:1433 -d --name sql_server_container mcr.microsoft.com/mssql/server:latest
```

- The database schema and initial data setup are included in the `schema.sql` script located in the `resources` directory.
- Update the connection string in `application.yml` with the appropriate database URL:

```yaml
datasource:
  url: jdbc:sqlserver://localhost:1433;databaseName=flash;schema=dbo;encrypt=false
  driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
```

## Endpoints

### External API

- **POST /api/v1/external:** Redact sensitive words from the given input message.

### Internal API

- **GET /api/v1/internal:** List all sensitive words.
- **GET /api/v1/internal/{id}:** Get a sensitive word by its ID.
- **POST /api/v1/internal:** Create a new sensitive word.
- **PUT /api/v1/internal/{id}:** Update a sensitive word.
- **DELETE /api/v1/internal/{id}:** Delete a sensitive word.

## Exception Handling

The application handles two custom exceptions:

- `SensitiveWordAlreadyExistsException`: Thrown when attempting to create a sensitive word that already exists.
- `SensitiveWordNotFoundException`: Thrown when a sensitive word with the given ID is not found.

## DTOs and Entities

- **RedactedMessageDTO:** Data Transfer Object representing a redacted message.
- **SensitiveWord:** Entity class representing a sensitive word.

## Repositories

- **SensitiveWordRepository:** Interface for CRUD operations on sensitive words.
- **MssqlJdbcSensitiveWordRepository:** MSSql Implementation of `SensitiveWordRepository` using JDBC template.

## Services

The services are divided into interfaces and implementations for better modularity, flexibility, and testability:

- **SensitiveWordsMaintenanceService:** Interface for managing sensitive words including CRUD operations.
- **SensitiveWordsMaintenanceServiceImpl:** Implementation of SensitiveWordsMaintenanceService.
- **SensitiveWordsReplacementService:** Interface for redacting sensitive words from messages.
- **SensitiveWordsReplacementServiceImpl:** Implementation of SensitiveWordsReplacementService.

## Configuration

- **Database Configuration:** Utilizes Microsoft SQL Server as the database with schema initialization.
- **Actuator Configuration:** Exposes actuator endpoints for health check and environment information.

### Swagger Documentation

The API documentation is available using Swagger. Access it by navigating to the `/swagger-ui.html` endpoint after starting the application.

### Actuator Endpoints

The application provides Actuator endpoints for monitoring and managing the application:

- **/actuator/health:** Provides information about application health.
- **/actuator/info:** Provides custom application information.
- **/actuator/env:** Provides environment information.

### Design and Architectural Approach

The application follows a layered architecture for clear separation of concerns:

- **Controller Layer:** Handles HTTP requests and delegates business logic to the service layer.
- **Service Layer:** Contains business logic and orchestrates interactions between repositories and other components.
- **Repository Layer:** Handles data access and persistence logic, abstracting the underlying data source.
- **Exception Handling:** Custom exception classes and global exception handler provide meaningful error responses.
- **DTOs and Entities:** Data Transfer Objects (DTOs) facilitate data exchange between layers, and entities represent domain objects.

The separation of services into interfaces and implementations promotes modularity, flexibility, and testability. It adheres to best practices in software design, allowing for easier maintenance and extensibility.

The application leverages Spring Boot's auto-configuration and dependency injection features for rapid development and easy integration with other Spring components. It also adopts RESTful principles for designing API endpoints, ensuring simplicity, scalability, and ease of use.

## Further Improvements

Consider the following enhancements for future iterations:

- **Containerization and Deployment:** Split the application into separate services for Maintenance and Redaction, enabling independent scaling and containerization.
- **Database Functionality:** Extend compatibility to other databases, implement caching, sharding, and resilience measures.
- **Microservices:** Utilize messaging services for inter-service communication.
- **Security:** Implement authentication and authorization for production readiness.

For detailed insights into each component and their functionalities, refer to the corresponding classes in the source code.