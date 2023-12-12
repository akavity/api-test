package org.example;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.specificatons.Specifications;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static io.restassured.RestAssured.given;

public class ReqrestNoPojoTest {
    private final ResourceBundle bundle = ResourceBundle.getBundle("config");
    private final String URL = bundle.getString("URL");
    Specifications specifications;

    @Test
    public void checkAvatarsContainsIdNoPojo() {
        specifications = new Specifications(URL, 200);
        Response response = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .body("data.id", Matchers.notNullValue())
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        List<String> avatars = jsonPath.getList("data.avatar");
        List<Integer> ids = jsonPath.getList("data.id");

        for (int i = 0; i < ids.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i).toString()));
        }
    }

    @Test
    public void checkEmailsContainsDogsNoPojo() {
        specifications = new Specifications(URL, 200);
        Response response = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .body("data.email", Matchers.notNullValue())
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        List<String> emails = jsonPath.getList("data.email");
        for (String email : emails) {
            Assert.assertTrue(email.endsWith("@reqres.in"));
        }

        Assert.assertTrue(emails.stream().allMatch(x -> x.endsWith("@reqres.in")));
        emails.stream().forEach(x -> Assert.assertTrue(x.endsWith("@reqres.in")));
    }

    @Test
    public void successRegNoPojoNoResponse() {
        specifications = new Specifications(URL, 200);
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        user.put("password", "pistol");
        given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .body("id", Matchers.equalTo(4))
                .body("token", Matchers.equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void successRegNoPojoNo() {
        specifications = new Specifications(URL, 200);
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        user.put("password", "pistol");
        Response response = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        int id = jsonPath.get("id");
        String token = jsonPath.get("token");

        Assert.assertEquals(id, 4);
        Assert.assertEquals(token, "QpwL5tke4Pnpja7X4");
    }
}

