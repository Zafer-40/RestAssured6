package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.sql.rowset.spi.SyncResolver;
import java.lang.management.ManagementPermission;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _01_GoRestUserTest {

    Faker randomUreteci = new Faker();
    int userID = 0;

    RequestSpecification reqSpec;
    @BeforeClass
    public void setup(){
        baseURI="https://gorest.co.in/public/v2/users";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization","Bearer e782adaae4ded8fe792e5425ab93f7ee5ca5b05c5b0d50a4143d29360daad740")
                .setContentType(ContentType.JSON)
                .build();

    }



    @Test(enabled = false)
    public void createUserJSon() {

        String rndFullName = randomUreteci.name().fullName();
        String rndEmail = randomUreteci.internet().emailAddress();

        userID =
                given() // giden body, token, contentType
                        .header("Authorization", "Bearer e782adaae4ded8fe792e5425ab93f7ee5ca5b05c5b0d50a4143d29360daad740")
                        .spec(reqSpec)
                        .body("{\"name\":\"" + rndFullName + "\", \"gender\":\"male\", \"email\":\"" + rndEmail + "\", \"status\":\"active\"}") // giden body
                        .contentType(ContentType.JSON)

                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;
        System.out.println("userID = " + userID);
    }

    @Test
    public void createUserMAP() {

        String rndFullName = randomUreteci.name().fullName();
        String rndEmail = randomUreteci.internet().emailAddress();

        Map<String,String> newUser = new HashMap<>();
        newUser.put("name",rndFullName);
        newUser.put("gender","male");
        newUser.put("email",rndEmail);
        newUser.put("status","active");

        userID =
                given() // giden body, token, contentType
                        .header("Authorization", "Bearer e782adaae4ded8fe792e5425ab93f7ee5ca5b05c5b0d50a4143d29360daad740")
                        .body(newUser)
                        .contentType(ContentType.JSON)

                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
        ;
        System.out.println("userID = " + userID);
    }
    @Test(dependsOnMethods = "createUserMAP")
    public void getUserByID(){
        given()
                .header("Authorization", "Bearer e782adaae4ded8fe792e5425ab93f7ee5ca5b05c5b0d50a4143d29360daad740")

                .when()
                .get(""+userID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(userID))
                ;
    }
    @Test(dependsOnMethods ="getUserByID" )
    public void updateUser(){
        Map<String,String> updateUser = new HashMap<>();
        updateUser.put("name","Cenk Tosun");

        given()
                .spec(reqSpec)
                .body(updateUser)

                .when()
                .put(""+userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id",equalTo(userID))
                .body("name",equalTo("Cenk Tosun"))

                ;
    }
    @Test(dependsOnMethods = "updateUser")
    public void deleteUser(){

        given()
                .spec(reqSpec)
                .when()
                .delete(""+userID)

                .then()
                .statusCode(204)
                ;
    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegative(){

        given()
                .spec(reqSpec)
                .when()
                .delete(""+userID)

                .then()
                .statusCode(404)
        ;
    }

}
