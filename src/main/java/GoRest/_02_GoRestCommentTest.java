package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _02_GoRestCommentTest {
    Faker faker = new Faker();
    int commentID=0;

    RequestSpecification rs;
    @BeforeClass
    public void setup(){

        baseURI = "https://gorest.co.in/public/v2/comments";
        rs = new RequestSpecBuilder()
                .addHeader("Authorization","Bearer e782adaae4ded8fe792e5425ab93f7ee5ca5b05c5b0d50a4143d29360daad740")
                .setContentType(ContentType.JSON)
                .build();
    }
    @Test
    public void createComment(){
        String fName = faker.name().firstName();
        String fEmail = faker.internet().emailAddress();
        String fBody = faker.lorem().paragraph();


        Map<String,String> newComment = new HashMap<>();
        newComment.put("post_id","82454");
        newComment.put("name",fName);
        newComment.put("email",fEmail);
        newComment.put("body",fBody);

        commentID=
        given()
                .spec(rs)
                .body(newComment)


                .when()
                .post("")

                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().path("id")
                ;
    }

    @Test(dependsOnMethods = "createComment")
    public void getCommentByID(){
        given()
                .spec(rs)

                .when()
                .get(""+commentID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(commentID))
                ;
    }

    @Test(dependsOnMethods = "getCommentByID")
    public void updateComment(){
        Map<String,String>updateComment = new HashMap<>();
        updateComment.put("name","Cem Ceminay");

        given()
                .spec(rs)
                .body(updateComment)

                .when()
                .put(""+commentID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id",equalTo(commentID))
                .body("name",equalTo("Cem Ceminay"))
                ;
    }

    @Test(dependsOnMethods = "updateComment")
    public void deleteComment(){
        given()
                .spec(rs)

                .when()
                .delete(""+commentID)

                .then()
                .log().all()
                .statusCode(204)
                ;
    }

    @Test(dependsOnMethods = "deleteComment")
    public void deleteCommentNegative(){
        given()
                .spec(rs)

                .when()
                .delete(""+commentID)

                .then()
                .statusCode(404)
        ;
    }

}
