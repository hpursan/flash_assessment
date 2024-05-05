# Sensitive Words API

This repository hosts a Spring Boot application that offers RESTful APIs for sensitive word management and redaction of sensitive content from messages.

## Table of Contents

- [Overview](#overview)
- [Endpoints](#endpoints)
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

The application facilitates two distinct sets of APIs:

- **External API:** Used for redacting sensitive words from messages.
- **Internal API:** Provides CRUD operations for managing sensitive words.

## Endpoints

### External API

- **POST /api/v1/external:** Redacts sensitive words from the provided input message.

### Internal API

- **GET /api/v1/internal:** Retrieves all sensitive words.
- **GET /api/v1/internal/{id}:** Retrieves a sensitive word by its ID.
- **POST /api/v1/internal:** Creates a new sensitive word.
- **PUT /api/v1/internal/{id}:** Updates a sensitive word.
- **DELETE /api/v1/internal/{id}:** Deletes a sensitive word.

## Exception Handling

Custom exceptions are employed:

- `SensitiveWordAlreadyExistsException`: Raised when attempting to create an existing sensitive word.
- `SensitiveWordNotFoundException`: Raised when a sensitive word with the given ID is not found.

## DTOs and Entities

- **RedactedMessageDTO:** Represents a redacted message.
- **SensitiveWord:** Entity class for a sensitive word.

## Repositories

- **SensitiveWordRepository:** Interface for CRUD operations on sensitive words.
- **MssqlJdbcSensitiveWordRepository:** MSSql Implementation of `SensitiveWordRepository` using JDBC template.

## Services

Services are divided into interfaces and implementations for modularity:

- **SensitiveWordsMaintenanceService:** Manages sensitive words with CRUD operations.
- **SensitiveWordsReplacementService:** Handles redaction of sensitive words from messages.

## Configuration

- **Database Configuration:** Utilizes Microsoft SQL Server with schema initialization.
- **Actuator Configuration:** Exposes actuator endpoints for health checks and environment information.

### Swagger Documentation

Swagger provides API documentation accessible via the `/swagger-ui.html` endpoint post-application startup.

### Actuator Endpoints

Actuator endpoints offer monitoring and management capabilities:

- **/actuator/health:** Indicates application health.
- **/actuator/info:** Supplies custom application information.
- **/actuator/env:** Presents environment information.

### Design and Architectural Approach

The application follows a layered architecture:

- **Controller Layer:** Handles HTTP requests and delegates logic to services.
- **Service Layer:** Contains business logic and orchestrates interactions.
- **Repository Layer:** Manages data access and persistence logic.
- **Exception Handling:** Custom exceptions and global handler provide error responses.
- **DTOs and Entities:** Facilitate data exchange and represent domain objects.

### Further Improvements

Additional enhancements for future consideration:

- **Containerization and Deployment:** Splitting the application into separate services, enabling independent scaling and containerization.
- **Database Functionality:** Extending compatibility to other databases, implementing caching, sharding, and resilience measures.
- **Microservices:** Utilizing messaging services for inter-service communication.
- **Security:** Implementing authentication and authorization for production readiness.

For detailed insights into each component, refer to the corresponding classes in the source code.