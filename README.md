# Filature

## Available connectors

### Sources

| Name     | Description                 | Documents retrieved |
|----------|-----------------------------|---------------------|
| Enercoop | French electricity provider | Utility bills       |

### Targets

| Name      | Description                                                                              |
|-----------|------------------------------------------------------------------------------------------|
| Nextcloud | Open Source suite of client-server software for creating and using file hosting services |

## Configuration

Use environment variables to configure filature :

| Environment Variable            | Description                                                    | Required | Example                                                          |
|---------------------------------|----------------------------------------------------------------|----------|------------------------------------------------------------------|
| FILATURE_JWT_SECRET             | Secret used to encrypt auth tokens                             | true     | 750d0512ce0af8a780399d6bb8296081efe2f1a98a94aad90cf8f58ba2492513 |
| FILATURE_POSTGRESQL_URL         | Postgresql connection string                                   | true     | postgresql://\<username>:\<password>@\<host>:\<port>/\<database> |
| FILATURE_OIDC_CLIENT_SERVER_URL | OIDC Server URL                                                | true     | https://sso.example.com                                          |
| FILATURE_OIDC_CLIENT_ID         | OIDC Client Id                                                 | true     | filature                                                         |
| FILATURE_OIDC_CLIENT_SECRET     | OIDC Client Secret                                             | true     |                                                                  |
| FILATURE_TEMP_DOWNLOAD_PATH     | Files will be stored in this folder during the synchronization | true     | /tmp/filature/downloads                                          |
| FILATURE_SYNCHRONIZE_EVERY      | Configure the periodicity of the synchronizations              | true     | 5m                                                               |

## Development

### Requirements

- Docker
- Java 21
- Maven

### Start the app in development mode

Quarkus uses Dev Services to start automatically the dependencies (Keycloak & Postgresql) using docker.

To start the app locally, just do : `mvn quarkus:dev`

You will then be able to access the frontend at `http://localhost:8080`

Connect using alice / alice

Make your modifications, and the app will automatically rebuild with your changes.