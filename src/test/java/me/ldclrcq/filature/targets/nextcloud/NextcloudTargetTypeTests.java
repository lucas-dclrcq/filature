package me.ldclrcq.filature.targets.nextcloud;

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
public class NextcloudTargetTypeTests {
    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldCreateNextcloudTarget() {
        var request = """
                {
                  "username": "test",
                  "password": "password",
                  "url": "https://nextcloud.example.com/"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(201).extract().response();

        given().when().get("/api/targets")
                .then()
                .statusCode(200)
                .body("[0].name", equalTo("Nextcloud"));
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateNextcloudTargetWithNullUsername() {
        var request = """
                {
                  "password": "password",
                  "url": "https://nextcloud.example.com/"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateNextcloudTargetWithEmptyUsername() {
        var request = """
                {
                  "username": "",
                  "password": "password",
                  "url": "https://nextcloud.example.com/"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateNextcloudTargetWithNullPassword() {
        var request = """
                {
                  "username": "username",
                  "url": "https://nextcloud.example.com/"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateNextcloudTargetWithEmptyPassword() {
        var request = """
                {
                  "username": "username",
                  "password": "",
                  "url": "https://nextcloud.example.com/"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateNextcloudTargetWithNullUrl() {
        var request = """
                {
                  "username": "username",
                  "password": "password"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(400);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldFailToCreateNextcloudTargetWithEmptyUrl() {
        var request = """
                {
                  "username": "username",
                  "password": "password",
                  "url": ""
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(400);
    }


    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldUpdateNextcloudTarget() {
        var originalRequest = """
                {
                  "username": "test",
                  "password": "password",
                  "url": "https://nextcloud.example.com/"
                }
                """;

        String location = given().contentType(ContentType.JSON)
                .body(originalRequest).when().post("/api/targets/nextcloud")
                .then()
                .statusCode(201)
                .extract().header("Content-Location");

        var updateRequest = """
                {
                  "username": "updated_username",
                  "password": "updated_password",
                  "url": "https://updated.nextcloud.example.com/"
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
    void shouldDeleteNextcloudTarget() {
        var originalRequest = """
                {
                  "username": "test",
                  "password": "password",
                  "url": "https://nextcloud.example.com/"
                }
                """;

        String location = given().contentType(ContentType.JSON)
                .body(originalRequest).when().post("/api/targets/nextcloud")
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
    void shouldReturnNotFoundWhenDeletingNonExistentTarget() {
        given().when().delete("/api/targets/nextcloud/999")
                .then()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "michel", roles = "user")
    @OidcSecurity(userinfo = {
            @UserInfo(key = "sub", value = "michel")
    })
    void shouldReturnNotFoundWhenUpdatingNonExistentTarget() {
        var request = """
                {
                  "username": "updated_username",
                  "password": "updated_password",
                  "url": "https://updated.nextcloud.example.com/"
                }
                """;

        given().contentType(ContentType.JSON)
                .body(request).when().put("/api/targets/nextcloud/999")
                .then()
                .statusCode(404);
    }
}
