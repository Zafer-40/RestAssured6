package Campus;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {

    RequestSpecification rs;
    Faker faker = new Faker();
    String countryID="";
    String name="";
    String code="";


    @BeforeClass
    public void setup(){
        baseURI="https://test.mersys.io/";
        Map<String,String>userCredential = new HashMap<>();
        userCredential.put("username","turkeyts");
        userCredential.put("password","TechnoStudy123");
        userCredential.put("rememberMe","true");

        Cookies cookies=
                given()
                        .body(userCredential)
                        .contentType(ContentType.JSON)

                        .when()
                        .post("/auth/login")

                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();
                ;
            rs = new RequestSpecBuilder()
                    .addCookies(cookies)
                    .setContentType(ContentType.JSON)
                    .build();
    }

    @Test
    public void createCountry(){
        name = faker.country().name()+faker.country().countryCode3();
        code = faker.country().countryCode2();

        Map<String,String>newCountry = new HashMap<>();
        newCountry.put("name",name);
        newCountry.put("code",code);


        countryID=
        given()
                .spec(rs)
                .body(newCountry)

                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")

                ;

    }
    // Aynı countryName ve code gönderildiğinde kayıt yapılmadığını yani
    // createCountryNegative testini yapınız.donen mesajin already kelimesini icerdigini test edin
    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative(){
        Map<String,String>newCountry = new HashMap<>();
        newCountry.put("name",name);
        newCountry.put("code",code);

                given()
                        .spec(rs)
                        .body(newCountry)

                        .when()
                        .post("school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message",containsString("already"))
        ;
    }

    @Test(dependsOnMethods = "createCountryNegative")
    public void updateCountry(){
        Map<String,String>updateCountry = new HashMap<>();
        updateCountry.put("id",countryID);
        updateCountry.put("name","UpdateCountryName09");
        updateCountry.put("code","UpdateCode007");

        given()
                .spec(rs)
                .body(updateCountry)

                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo("UpdateCountryName09"))
        ;
    }

    // Delete Country testini yapınız
    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry(){
        given()
                .spec(rs)

                .when()
                .delete("school-service/api/countries/"+countryID)

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    // Delete Country testinin Negative test halini yapınız
    // dönen mesajın "Country not found" olduğunu doğrulayınız
    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative(){
        given()
                .spec(rs)

                .when()
                .delete("school-service/api/countries/"+countryID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Country not found"))
        ;
    }


}
