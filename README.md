# Filature

## Configuration

Use environment variables to configure filature :

| Environment Variable            | Description                        | Required | Example                                                          |
|---------------------------------|------------------------------------|----------|------------------------------------------------------------------|
| FILATURE_JWT_SECRET             | Secret used to encrypt auth tokens | true     | 750d0512ce0af8a780399d6bb8296081efe2f1a98a94aad90cf8f58ba2492513 |
| FILATURE_POSTGRESQL_URL         | Postgresql connection string       | true     | postgresql://\<username>:\<password>@\<host>:\<port>/\<database> |
| FILATURE_OIDC_CLIENT_SERVER_URL | OIDC Server URL                    | true     | https://sso.example.com                                          |
| FILATURE_OIDC_CLIENT_ID         | OIDC Client Id                     | true     | filature                                                         |
| FILATURE_OIDC_CLIENT_SECRET     | OIDC Client Secret                 | true     |                                                                  |

## Development

### Requirements
- Docker
- Java 21
- Maven

### Start the app in development mode

Quarkus uses Dev Services to start automatically the dependencies (Keycloak & Postgresql) using docker.

To start the app locally, just do : `mvn quarkus:dev`
Make your modifications, and the app will automatically rebuild with your changes.