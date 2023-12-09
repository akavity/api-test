import io.restassured.http.ContentType;
import org.example.models.UserData;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class ReqresTest {
    private static final String URl = "https://reqres.in/";

    @Test
    public void checkId() {
        List<UserData> usersList = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URl + "api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        usersList.stream().forEach(x -> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
    }

    @Test
    public void checkAvatars() {
        List<UserData> userList = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URl + "api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        Assert.assertTrue(userList.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));

        userList.stream().forEach(x -> Assert.assertTrue(x.getEmail().endsWith("@reqres.in")));
    }

    @Test
    public void checkAvatarContainId() {
        List<UserData> userList = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URl + "api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);

        List<String> avatars = userList.stream().map(UserData::getAvatar).collect(Collectors.toList());
        List<String> ids = userList.stream().map(x -> x.getId().toString()).collect(Collectors.toList());
        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }
}
