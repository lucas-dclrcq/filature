package me.ldclrcq.filature.sources.enercoop;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.OidcSecurity;
import io.quarkus.test.security.oidc.UserInfo;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import me.ldclrcq.filature.connections.Connection;
import me.ldclrcq.filature.sources.Source;
import me.ldclrcq.filature.synchronizations.Synchronization;
import me.ldclrcq.filature.targets.Target;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EnercoopSourceTypeTests {
    @BeforeEach
    @Transactional
    public void cleanup() {
        Synchronization.deleteAll();
        Connection.deleteAll();
        Source.deleteAll();
        Target.deleteAll();
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldCreateEnercoopSource() {
        var request = """
                {
                  "username": "test",
                  "password": "password"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/sources/enercoop")
                .then()
                .statusCode(201).extract().response();

        given().when().get("/api/sources")
                .then()
                .statusCode(200)
                .body("[0].name", equalTo("Enercoop"));

    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateEnercoopSourceWithNullUsername() {
        var request = """
                {
                  "password": "%s"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/sources/enercoop")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateEnercoopSourceWithEmptyUsername() {
        var request = """
                {
                  "username": "",
                  "password": "password"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/sources/enercoop")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateEnercoopSourceWithNullPassword() {
        var request = """
                {
                  "username": "username"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/sources/enercoop")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateEnercoopSourceWithEmptyPassword() {
        var request = """
                {
                  "username": "username",
                  "password": ""
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/sources/enercoop")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldUpdateEnercoopSource() {
        var originalRequest = """
                {
                  "username": "test",
                  "password": "password"
                }
                """;

        String location = given().contentType(ContentType.JSON)
                .body(originalRequest).when().post("/api/sources/enercoop")
                .then()
                .statusCode(201)
                .extract().header("Content-Location");

        var updateRequest = """
                {
                  "username": "updated_username",
                  "password": "updated_password"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(updateRequest).when().put(location)
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldDeleteSource() {
        var originalRequest = """
                {
                  "username": "test",
                  "password": "password"
                }
                """;

        String location = given().contentType(ContentType.JSON)
                .body(originalRequest).when().post("/api/sources/enercoop")
                .then()
                .statusCode(201)
                .extract().header("Content-Location");

        given().when().delete(location)
                .then()
                .statusCode(204);

        given().when().get(location)
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldReturnNotFoundWhenDeletingNonExistentSource() {
        given().when().delete("/999")
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldReturnNotFoundWhenUpdatingNonExistentSource() {
        var request = """
                {
                  "username": "updated_username",
                  "password": "updated_password"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().put("/api/sources/enercoop/999")
                .then()
                .statusCode(404);
    }
}
