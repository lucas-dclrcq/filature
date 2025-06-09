package me.ldclrcq.filature.sources.free;

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
public class FreeSourceTypeTests {

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
    void shouldCreateFreeSource() {
        var request = """
                {
                  "login": "test",
                  "password": "password"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/sources/free")
                .then()
                .statusCode(201).extract().response();

        given().when().get("/api/sources")
                .then()
                .statusCode(200)
                .body("[0].name", equalTo("Free"));
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateFreeSourceWithNullLogin() {
        var request = """
                {
                  "password": "password"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/sources/free")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateFreeSourceWithEmptyLogin() {
        var request = """
                {
                  "login": "",
                  "password": "password"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/sources/free")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateFreeSourceWithNullPassword() {
        var request = """
                {
                  "login": "login"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/sources/free")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateFreeSourceWithEmptyPassword() {
        var request = """
                {
                  "login": "login",
                  "password": ""
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/sources/free")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldReturnNotFoundWhenDeletingNonExistentSource() {
        given().when().delete("/api/sources/free/999")
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
                  "login": "test",
                  "password": "password"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().put("/api/sources/free/999")
                .then()
                .statusCode(404);
    }
}