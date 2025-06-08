package me.ldclrcq.filature.targets;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.OidcSecurity;
import io.quarkus.test.security.oidc.UserInfo;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class TargetTypeResourceTests {

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldListTargets() {
        var request = """
                {
                  "username": "test",
                  "password": "password",
                  "url": "https://nextcloud.example.com/",
                  "uploadFolder": "bills"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(201);

        given().when().get("/api/targets")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(1)))
                .body("[0].name", equalTo("NEXTCLOUD"));
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldGetTargetById() {
        var request = """
                {
                  "username": "test",
                  "password": "password",
                  "url": "https://nextcloud.example.com/",
                  "uploadFolder": "bills"
                }
                """;

        String location = given().contentType(ContentType.JSON)
                .body(request).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(201)
                .extract().header("Content-Location");

        String id = location.substring(location.lastIndexOf("/") + 1);

        given().when().get("/api/targets/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("NEXTCLOUD"));
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldDeleteTargetById() {
        var request = """
                {
                  "username": "test",
                  "password": "password",
                  "url": "https://nextcloud.example.com/",
                  "uploadFolder": "bills"
                }
                """;

        String location = given().contentType(ContentType.JSON)
                .body(request).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(201)
                .extract().header("Content-Location");

        String id = location.substring(location.lastIndexOf("/") + 1);

        given().when().delete("/api/targets/" + id)
                .then()
                .statusCode(204);

        given().when().get("/api/targets/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldReturnNotFoundWhenGettingNonExistentTarget() {
        given().when().get("/api/targets/999")
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldReturnNotFoundWhenDeletingNonExistentTarget() {
        given().when().delete("/api/targets/999")
                .then()
                .statusCode(404);
    }
}