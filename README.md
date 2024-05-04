# Sensitive Words API

This repository contains a Spring Boot application that provides RESTful APIs for managing sensitive words and redacting sensitive content from messages.

## Table of Contents

- [Sensitive Words API](#sensitive-words-api)
    - [Table of Contents](#table-of-contents)
    - [Overview](#overview)
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

## Overview

This application provides two sets of APIs - External and Internal, for different use cases:

- **External API:** Exposes endpoints for redacting sensitive words from messages.
- **Internal API:** Exposes endpoints for managing sensitive words including CRUD operations.

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

- **RedactedMessageDTO:** Data Transfer Object for representing a redacted message.
- **SensitiveWord:** Entity class representing a sensitive word.

## Repositories

- **SensitiveWordRepository:** Interface for CRUD operations on sensitive words.
- **JdbcSensitiveWordRepository:** Implementation of `SensitiveWordRepository` using JDBC template.

## Services

- **SensitiveWordsMaintenanceService:** Service for managing sensitive words including CRUD operations.
- **SensitiveWordsReplacementService:** Service for redacting sensitive words from messages.

## Configuration

- **Database Configuration:** Uses H2 in-memory database with schema and data initialization.
- **Actuator Configuration:** Exposes actuator endpoints for health check and environment information.

### Swagger Documentation

The API documentation is available using Swagger. You can access it by navigating to `/swagger-ui.html` endpoint after starting the application.

### Actuator Endpoints

The application provides Actuator endpoints for monitoring and managing the application. Common endpoints include:

- **/actuator/health:** Provides information about application health.
- **/actuator/info:** Provides custom application information.
- **/actuator/env:** Provides environment information.

### Design and Architectural Approach

The application follows a layered architecture with clear separation of concerns:

- **Controller Layer:** Handles HTTP requests and delegates business logic to the service layer.
- **Service Layer:** Contains business logic and orchestrates interactions between repositories and other components.
- **Repository Layer:** Handles data access and persistence logic, abstracting the underlying data source.
- **Exception Handling:** Custom exception classes and global exception handler are implemented to provide meaningful error responses.
- **DTOs and Entities:** Data Transfer Objects (DTOs) are used for data exchange between layers, and entities represent domain objects.

The application leverages Spring Boot's auto-configuration and dependency injection features for rapid development and easy integration with other Spring components. It also adopts RESTful principles for designing API endpoints, ensuring simplicity, scalability, and ease of use.

For more detailed information on each component and their functionalities, please refer to the corresponding classes in the source code.