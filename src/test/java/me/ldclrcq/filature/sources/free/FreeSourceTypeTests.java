package me.ldclrcq.filature.sources.free;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.OidcSecurity;
import io.quarkus.test.security.oidc.UserInfo;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FreeSourceTypeTests {
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
    void shouldUpdateFreeSource() {
        var originalRequest = """
                {
                  "login": "test",
                  "password": "password"
                }
                """;

        var response = given().contentType(ContentType.JSON)
                .body(originalRequest).when().post("/api/sources/free")
                .then()
                .statusCode(201).extract().response();

        var sourceId = response.getHeader("Content-Location").split("/")[5];

        var updateRequest = """
                {
                  "login": "updated",
                  "password": "updated"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(updateRequest).when().put("/api/sources/free/" + sourceId)
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldDeleteSource() {
        var request = """
                {
                  "login": "test",
                  "password": "password"
                }
                """;

        var response = given().contentType(ContentType.JSON)
                .body(request).when().post("/api/sources/free")
                .then()
                .statusCode(201).extract().response();

        var sourceId = response.getHeader("Content-Location").split("/")[5];

        given().when().delete("/api/sources/free/" + sourceId)
                .then()
                .statusCode(204);

        given().when().get("/api/sources")
                .then()
                .statusCode(200)
                .body("$", hasSize(0));
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