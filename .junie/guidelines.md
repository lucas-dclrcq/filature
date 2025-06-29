# Filature Development Guidelines

## Project Overview
Filature is an application that automatically synchronizes documents (like utility bills) from various sources to various targets. It provides a web interface for users to configure their sources, targets, and synchronization settings.

## Tech Stack
- **Backend**: Java 21 with Quarkus 3.24.1
- **Database**: PostgreSQL with Flyway for migrations and Hibernate ORM with Panache
- **Frontend**: React with Quinoa integration
- **Authentication**: OpenID Connect (OIDC)
- **API Documentation**: Swagger UI / OpenAPI
- **Web Scraping**: Playwright for browser automation
- **Containerization**: Docker

## Project Structure
```
filature/
├── src/
│   ├── main/
│   │   ├── docker/           # Docker configuration files
│   │   ├── java/             # Java source code
│   │   │   └── me/ldclrcq/filature/
│   │   │       ├── configuration/    # Application configuration
│   │   │       ├── connections/      # Connection management
│   │   │       ├── notifiers/        # Notification system
│   │   │       ├── sources/          # Source connectors
│   │   │       ├── synchronizations/ # Synchronization logic
│   │   │       ├── targets/          # Target connectors
│   │   │       └── temp_storage/     # Temporary storage
│   │   ├── resources/        # Configuration files
│   │   └── webui/            # Frontend code
│   └── test/                 # Test code
```

## Key Components
1. **Sources**: Connectors to external services (e.g., Free, Enercoop) to download documents
2. **Targets**: Connectors to storage services (e.g., Nextcloud) to upload documents
3. **Synchronizations**: Core logic for synchronizing documents from sources to targets
4. **Connections**: Manages the relationship between sources and targets
5. **Notifiers**: Sends notifications about synchronization results

## Development Workflow
1. **Setup**: 
   - Ensure Java 21, Maven, and Docker are installed
   - Clone the repository

2. **Running Locally**:
   - Run `mvn quarkus:dev` to start the application in development mode
   - Access the frontend at http://localhost:8080
   - Default credentials: alice / alice

3. **Making Changes**:
   - Backend changes: Modify Java code in `src/main/java`
   - Frontend changes: Modify code in `src/main/webui`
   - Configuration changes: Modify `src/main/resources/application.properties`

4. **Testing**:
   - Run tests with `mvn test`
   - Quarkus Dev Services automatically starts dependencies (Keycloak & PostgreSQL)

5. **Building**:
   - Run `mvn package` to build the application
   - The application can be run with `java -jar target/quarkus-app/quarkus-run.jar`

## Best Practices
1. **Code Organization**:
   - Follow the package structure for new components
   - Use dependency injection with CDI (Context and Dependency Injection)
   - Implement interfaces for new connectors (SourceConnector, TargetConnector)

2. **Error Handling**:
   - Use proper exception handling and logging
   - Record synchronization failures for user feedback

3. **Security**:
   - Always use OIDC for authentication
   - Secure sensitive data (credentials) appropriately

4. **Performance**:
   - Be mindful of resource usage in connectors
   - Clean up temporary files after use

5. **Documentation**:
   - Document new connectors in the README.md
   - Use OpenAPI annotations for REST endpoints

## Adding New Connectors
1. **Source Connectors**:
   - Implement the `SourceConnector` interface
   - Add the new source type to the `SourceType` enum
   - Register the connector with CDI using `@ApplicationScoped`
   - Create the resources and DTOs to manage the new connector
   - In the webui directory, run `npm run generate-api` to generate the frontend api client using orval
   - Create the views to manage the connector

2. **Target Connectors**:
   - Implement the `TargetConnector` interface
   - Add the new target type to the `TargetType` enum
   - Register the connector with CDI using `@ApplicationScoped`
   - Create the resources and DTOs to manage the new connector
   - In the webui directory, run `npm run generate-api` to generate the frontend api client using orval
   - Create the views to manage the connector

## Deployment
- The application can be containerized using the provided Dockerfiles
- Configure the application using environment variables as described in the README.md