package me.ldclrcq.filature.connections;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.OidcSecurity;
import io.quarkus.test.security.oidc.UserInfo;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ConnectionResourceTests {
    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldCreateConnection() {
        var sourceRequest = """
                {
                  "username": "test",
                  "password": "password"
                }
                """;

        String sourceLocation = given().contentType(ContentType.JSON)
                .body(sourceRequest).when().post("/api/sources/enercoop")
                .then()
                .statusCode(201)
                .extract().header("Content-Location");

        String sourceId = sourceLocation.substring(sourceLocation.lastIndexOf("/") + 1);

        var targetRequest = """
                {
                  "username": "test",
                  "password": "password",
                  "url": "https://nextcloud.example.com/",
                  "uploadFolder": "bills"
                }
                """;

        String targetLocation = given().contentType(ContentType.JSON)
                .body(targetRequest).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(201)
                .extract().header("Content-Location");

        String targetId = targetLocation.substring(targetLocation.lastIndexOf("/") + 1);

        var connectionRequest = """
                {
                  "sourceConfigurationId": %s,
                  "targetConfigurationId": %s,
                  "targetUploadPath": "/bills"
                }
                """.formatted(sourceId, targetId);

        String connectionLocation = given().contentType(ContentType.JSON)
                .body(connectionRequest).when().post("/api/connections")
                .then()
                .statusCode(201)
                .extract().header("Content-Location");

        String connectionId = connectionLocation.substring(connectionLocation.lastIndexOf("/") + 1);

        given().when().get("/api/connections/" + connectionId)
                .then()
                .statusCode(200)
                .body("source.id", equalTo(Integer.parseInt(sourceId)))
                .body("target.id", equalTo(Integer.parseInt(targetId)))
                .body("targetUploadPath", equalTo("/bills"));
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldDeleteConnection() {
        var sourceRequest = """
                {
                  "username": "test",
                  "password": "password"
                }
                """;

        String sourceLocation = given().contentType(ContentType.JSON)
                .body(sourceRequest).when().post("/api/sources/enercoop")
                .then()
                .statusCode(201)
                .extract().header("Content-Location");

        String sourceId = sourceLocation.substring(sourceLocation.lastIndexOf("/") + 1);

        var targetRequest = """
                {
                  "username": "test",
                  "password": "password",
                  "url": "https://nextcloud.example.com/",
                  "uploadFolder": "bills"
                }
                """;

        String targetLocation = given().contentType(ContentType.JSON)
                .body(targetRequest).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(201)
                .extract().header("Content-Location");

        String targetId = targetLocation.substring(targetLocation.lastIndexOf("/") + 1);

        var connectionRequest = """
                {
                  "sourceConfigurationId": %s,
                  "targetConfigurationId": %s,
                  "targetUploadPath": "/bills"
                }
                """.formatted(sourceId, targetId);

        String connectionLocation = given().contentType(ContentType.JSON)
                .body(connectionRequest).when().post("/api/connections")
                .then()
                .statusCode(201)
                .extract().header("Content-Location");

        String connectionId = connectionLocation.substring(connectionLocation.lastIndexOf("/") + 1);

        given().when().delete("/api/connections/" + connectionId)
                .then()
                .statusCode(204);

        given().when().get("/api/connections/" + connectionId)
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldReturnNotFoundWhenGettingNonExistentConnection() {
        given().when().get("/api/connections/999")
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldReturnNotFoundWhenDeletingNonExistentConnection() {
        given().when().delete("/api/connections/999")
                .then()
                .statusCode(404);
    }
}