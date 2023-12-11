package org.example;

import org.example.models.Registration;
import org.example.models.SuccessRegistration;
import org.example.models.UnSuccessReg;
import org.example.models.UserData;
import org.example.specificatons.Specifications;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTest {
    private final ResourceBundle bundle = ResourceBundle.getBundle("config");
    private final String URL = bundle.getString("URL");
    Specifications specifications;

    @Test
    public void checkId() {
        specifications = new Specifications(URL, 200);
        List<UserData> usersList = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        usersList.stream().forEach(x -> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
    }

    @Test
    public void checkAvatars() {
        specifications = new Specifications(URL, 200);
        List<UserData> userList = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        Assert.assertTrue(userList.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));
        userList.stream().forEach(x -> Assert.assertTrue(x.getEmail().endsWith("@reqres.in")));
    }

    @Test
    public void checkAvatarContainId() {
        specifications = new Specifications(URL, 200);
        List<UserData> userList = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        List<String> avatars = userList.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = userList.stream().map(x -> x.getId().toString()).collect(Collectors.toList());

        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }

    @Test
    public void successRegistration() {
        specifications = new Specifications(URL, 200);
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Registration user = new Registration("eve.holt@reqres.in", "pistol");
        SuccessRegistration successReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessRegistration.class);

        Assert.assertEquals(token, successReg.getToken());
        Assert.assertEquals(id, successReg.getId());
    }

    @Test
    public void unSuccessRegistration() {
        specifications = new Specifications(URL, 400);
        Registration user = new Registration("eve.holt@reqres.in", "");
        UnSuccessReg unSuccessReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccessReg.class);
        Assert.assertEquals("Missing password", unSuccessReg.getError());
    }

    @Test
    public void deleteUser() {
        specifications = new Specifications(URL, 204);
        given()
                .when()
                .delete("api/users/2")
                .then().log().all();
    }
}
