# Filature

Filature is an application that automatically synchronizes documents (like utility bills) from various sources (such as
Enercoop and Free) to various targets (like Nextcloud). It provides a web interface for users to configure their
sources, targets, and synchronization settings.

## What Filature Does

- **Automatic Document Synchronization**: Periodically checks for new documents from configured sources and uploads them
  to configured targets
- **Multiple Source Support**: Connect to different utility providers to retrieve your bills and documents
- **Target Integration**: Upload your documents to storage services like Nextcloud
- **Scheduled Synchronization**: Configure how often synchronization should occur
- **User Authentication**: Secure access with OpenID Connect authentication

## Available Connectors

### Sources

| Name     | Description                 | Documents Retrieved |
|----------|-----------------------------|---------------------|
| Enercoop | French electricity provider | Utility bills       |
| Free     | French telecom provider     | Utility bills       |

### Targets

| Name      | Description                                                                              |
|-----------|------------------------------------------------------------------------------------------|
| Nextcloud | Open Source suite of client-server software for creating and using file hosting services |

## Configuration

Use the following environment variables to configure Filature:

| Environment Variable                                    | Description                                          | Required |
|---------------------------------------------------------|------------------------------------------------------|----------|
| QUARKUS_OIDC_AUTH_SERVER_URL                            | OIDC Server URL                                      | true     | 
| QUARKUS_DATASOURCE_JDBC_URL                             | JDBC connection string                               | true     |
| QUARKUS_OIDC_CLIENT_ID                                  | OIDC Client ID                                       | true     |
| QUARKUS_OIDC_CREDENTIALS_SECRET                         | OIDC Client Secret                                   | true     |
| QUARKUS_OIDC_AUTHENTICATION_FORCE_REDIRECT_HTTPS_SCHEME | Force HTTPS for OIDC redirects                       | true     |
| QUARKUS_OIDC_AUTHENTICATION_PKCE_REQUIRED               | Require PKCE for OIDC authentication                 | true     |
| QUARKUS_OIDC_AUTHENTICATION_REDIRECT_PATH               | Path for OIDC redirect                               | true     |
| FILATURE_TEMP_DOWNLOAD_PATH                             | Temporary storage path for downloaded files          | true     |
| FILATURE_SYNCHRONIZE_EVERY                              | Synchronization frequency (e.g., "5m" for 5 minutes) | true     |

## Development

### Requirements

- Docker
- Java 21
- Maven

### Start the app in development mode

Quarkus uses Dev Services to start automatically the dependencies (Keycloak & PostgreSQL) using Docker.

To start the app locally, just do: `mvn quarkus:dev`

You will then be able to access the frontend at `http://localhost:8080`

Connect using alice / alice

Make your modifications, and the app will automatically rebuild with your changes.
